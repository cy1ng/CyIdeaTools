package com.cying.ideatools.codemaker.crud;

import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chengying13378 chengying13378@hundsun.com
 * @ClassName MapperEntry
 * @Description TODO
 * @date 2019-01-27 22:18
 */
@Data
public class MapperEntry {

    private String mapperPackages;

    private String modelPackages;

    private String user;

    private String mail;

    private String modelName;

    private String modelNameCamel;

    private String modelKeyType;

    private String modelKeyCamel;

    private List<Field> modelAllFields;

    public static MapperEntry create(PsiClass mapperPsiClass) {

        MapperEntry entry = new MapperEntry();
        PsiMethod[] selPsiMethods = mapperPsiClass.findMethodsByName("selectByPrimaryKey", false);
        if (selPsiMethods != null && selPsiMethods.length > 0) {
            PsiMethod selPsiMethod = selPsiMethods[0];

            PsiType modeType = selPsiMethod.getReturnTypeElement().getType();
            PsiParameter parameter = selPsiMethod.getParameterList().getParameters()[0];
            entry.modelName = modeType.getPresentableText();
            entry.modelNameCamel = toCamel(entry.modelName);
            // TODO 可能要考虑联合主键
            entry.modelKeyType = parameter.getType().getPresentableText();
            entry.modelKeyCamel = toCamel(parameter.getName());
            // 查看model的详细信息
            PsiJavaFile mapperJavaFile = (PsiJavaFile) mapperPsiClass.getContainingFile();
            entry.mapperPackages = mapperJavaFile.getPackageName();
            // 根据model的完全限定名查询类
            String modeFullName = modeType.getCanonicalText();
            PsiClass modeClass = JavaPsiFacade.getInstance(mapperPsiClass.getProject()).findClass(modeFullName,
                    GlobalSearchScope.projectScope(mapperPsiClass.getProject()));
            // TODO 是否可能找到多个
            if(modeClass == null){
                throw new RuntimeException("找不到model类");
            }
            entry.modelPackages = ((PsiJavaFile) modeClass.getContainingFile()).getPackageName();
            entry.modelAllFields = getAllFields(modeClass);
        }
        return entry;
    }


    public static String toCamel(String str) {
        if (str == null) {
            return null;
        }
        String first = str.substring(0, 1).toLowerCase();
        return first + str.substring(1);
    }

    @Data
    @AllArgsConstructor
    public static class Field {
        /**
         * field type
         */
        private String type;

        /**
         * field name
         */
        private String name;

        /**
         * the field modifier, like "private",or "@Setter private" if include annotations
         */
        private String modifier;

    }


    public static List<MapperEntry.Field> getAllFields(PsiClass psiClass) {
        return Arrays
                .stream(psiClass.getAllFields())
                .map(
                        psiField -> new MapperEntry.Field(psiField.getType().getPresentableText(), psiField
                                .getName(), psiField.getModifierList() == null ? "" : psiField
                                .getModifierList().getText())).collect(Collectors.toList());
    }

}
