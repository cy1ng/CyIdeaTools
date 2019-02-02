package com.cying.ideatools.codemaker.crud.action.creator;

import com.cying.ideatools.codemaker.crud.CodeTemplate;
import com.cying.ideatools.codemaker.crud.CrudCodeMakerSettings;
import com.cying.ideatools.codemaker.crud.MethodEntry;
import com.cying.ideatools.codemaker.crud.ui.SelectMethodDlg;
import com.cying.ideatools.codemaker.crud.ui.ShowCodeBlockDlg;
import com.cying.ideatools.codemaker.crud.utils.CodeMakerUtil;
import com.cying.ideatools.codemaker.crud.utils.VelocityUtil;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chengying13378 chengying13378@hundsun.com
 * @ClassName CodeBlockCreator
 * @Description TODO
 * @date 2019-01-31 00:25
 */
public class CodeBlockMaker implements IMaker {

    private CrudCodeMakerSettings settings;

    private Project project;
    private List<String> templateNames;
    private PsiClass currentPsiClass;

    public CodeBlockMaker(@NotNull Project project, @Nullable PsiClass currentPsiClass, List<String> templateNames) {
        this.project = project;
        this.templateNames = templateNames;
        this.settings = ServiceManager.getService(CrudCodeMakerSettings.class);
        this.currentPsiClass = currentPsiClass;
    }

    @Override
    public void make() {

        // 1.选取mapper类
        PsiClass psiMapperClass = CodeMakerUtil.chooseMapperClass(project, currentPsiClass);
        if (psiMapperClass == null) {
            return;
        }
        // 2.选取该mapper类方法
        String modelName = this.getModelName(psiMapperClass);
        // 选取方法,过滤掉Object里面的方法
        SelectMethodDlg dlg = new SelectMethodDlg(project, psiMapperClass.getMethods(), "Select One Or More Method");
        dlg.show();
        // 3.生成代码并展示
        int[] selectedIndexs = dlg.getSelectionIdx();
        if (selectedIndexs.length < 1) {
            return;
        }
        StringBuilder allContent = new StringBuilder();
        for (int index : selectedIndexs) {
            MethodEntry methodEntry = MethodEntry.create(psiMapperClass.getMethods()[index]);
            CodeTemplate codeTemplate = settings.getCodeTemplate(templateNames.get(0));
            if (codeTemplate == null) {
                continue;
            }
            // 渲染模板 TODO 注意基本类型不能加DTO哦
            String content = VelocityUtil.evaluate(codeTemplate.getCodeTemplate(), createContext(methodEntry, modelName));
            allContent.append(content).append("=============================\r\n");
        }
        // 展示类容
        ShowCodeBlockDlg showCodeBlockDlg = new ShowCodeBlockDlg(project, allContent, "Copy Created Code");
        showCodeBlockDlg.show();
    }

    private Map<String, Object> createContext(MethodEntry methodEntry, String modelName) {

        Map<String, Object> context = new HashMap<>(3);
        context.put("methodName", methodEntry.getMethodName());
        context.put("parameters", methodEntry.getParameters());
        context.put("returnType", methodEntry.getReturnType());
        context.put("modelName", modelName);
        context.put("modelNameCamel", CodeMakerUtil.toCamel(modelName));
        context.put("selExampleFlag", methodEntry.isSelExampleFlag());
        context.put("uptExampleFlag", methodEntry.isUptExampleFlag());
        return context;
    }

    private String getModelName(PsiClass mapperPsiClass) {

        PsiMethod[] selPsiMethods = mapperPsiClass.findMethodsByName("selectByPrimaryKey", false);
        if (selPsiMethods.length < 1) {
            throw new RuntimeException("无效的Mapper类,找不到方法selectByPrimaryKey");
        }
        PsiMethod psiMethod = selPsiMethods[0];
        return psiMethod.getReturnType().getPresentableText();
    }

}
