package com.cying.ideatools.codemaker.crud;

import com.google.common.collect.Maps;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author chengying13378 chengying13378@hundsun.com
 * @ClassName MethodEntry
 * @Description TODO
 * @date 2019-01-30 13:38
 */
@Data
public class MethodEntry {

    private static final Map<String, MethodDecorate> map = Maps.newHashMap();

    private boolean uptExampleFlag = false;

    private boolean selExampleFlag = false;

    private String methodName;

    private String returnType;

    private List<Parameter> parameters;

    public static MethodEntry create(PsiMethod psiMethod) {

        if (psiMethod == null) {
            return null;
        }
        MethodEntry entry = new MethodEntry();
        entry.methodName = psiMethod.getName();
        if ("selectByExample".equals(entry.methodName)) {
            entry.selExampleFlag = true;
        } else if ("updateByExampleSelective".equals(entry.methodName)
                || "updateByExample".equals(entry.methodName)) {
            entry.uptExampleFlag = true;
        }
        entry.returnType = psiMethod.getReturnType().getPresentableText();
        PsiParameterList parameterList = psiMethod.getParameterList();
        if (!parameterList.isEmpty()) {
            entry.parameters = new ArrayList<>();
            for (PsiParameter psiParameter : parameterList.getParameters()) {
                Parameter parameter = new Parameter(psiParameter.getType().getPresentableText(), psiParameter.getName());
                entry.parameters.add(parameter);
            }
        }
        MethodDecorate methodDecorate = map.get(entry.methodName);
        if (methodDecorate == null) {
            methodDecorate = DEFAULT;
        }
        methodDecorate.decorate(entry);
        return entry;
    }

    @Data
    @AllArgsConstructor
    public static class Parameter {

        private String type;

        private String name;
    }


    @FunctionalInterface
    public interface MethodDecorate {
        void decorate(MethodEntry methodEntry);
    }

    public static MethodDecorate DEFAULT = methodEntry -> {
        //no op
    };

    static {
        map.put("countByExample", methodEntry -> {
            methodEntry.setMethodName("count$!{modelName}");
        });
        map.put("deleteByExample", methodEntry -> {
            methodEntry.setMethodName("del$!{modelName}");
        });
        map.put("deleteByPrimaryKey", methodEntry -> {
            methodEntry.setMethodName("del$!{modelName}ByKey");
        });
        map.put("insert", methodEntry -> {
            methodEntry.setMethodName("add$!{modelName}");
        });
        map.put("insertSelective", methodEntry -> {
            methodEntry.setMethodName("add$!{modelName}");
        });
        map.put("selectByExample", methodEntry -> {
            methodEntry.setMethodName("get$!{modelName}ByPage");
            methodEntry.setParameters(Arrays.asList(new Parameter("*Bean", "bean"),
                    new Parameter("PageRequest", "page")));
        });
        map.put("selectByPrimaryKey", methodEntry -> {
            methodEntry.setMethodName("get$!{modelName}ByKey");
        });
        map.put("updateByExampleSelective", methodEntry -> {
            methodEntry.setMethodName("upt$!{modelName}");
            //TODO
        });
        map.put("updateByExample", methodEntry -> {
            methodEntry.setMethodName("upt$!{modelName}");
            //TODO
        });
        map.put("updateByPrimaryKeySelective", methodEntry -> {
            methodEntry.setMethodName("upt$!{modelName}ByKey");
        });
        map.put("updateByPrimaryKey", methodEntry -> {
            methodEntry.setMethodName("upt$!{modelName}ByKey");
        });
        map.put("selectList", methodEntry -> {
            methodEntry.setMethodName("get$!{modelName}List");
        });
        map.put("selectMiddleLike", methodEntry -> {
            methodEntry.setMethodName("getMiddleLike");
        });
    }
}
