package com.cying.ideatools.codemaker.crud.action;

import com.cying.ideatools.codemaker.crud.CodeTemplate;
import com.cying.ideatools.codemaker.crud.CrudCodeMakerSettings;
import com.cying.ideatools.codemaker.crud.MethodEntry;
import com.cying.ideatools.codemaker.crud.ui.SelectMethodDlg;
import com.cying.ideatools.codemaker.crud.ui.ShowCodeBlockDlg;
import com.cying.ideatools.codemaker.crud.ui.ShowTextDlg;
import com.cying.ideatools.codemaker.crud.utils.CodeMakerUtil;
import com.cying.ideatools.codemaker.crud.utils.VelocityUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chengying13378 chengying13378@hundsun.com
 * @ClassName CodeBlockMakerAction
 * @Description 代码块生成
 * @date 2019-01-29 21:22
 */
public class CodeBlockMakerAction extends AnAction implements DumbAware {

    private Logger LOGGER = LoggerFactory.getLogger(CrudCodeMakerAction.class);

    private String templateKey;

    private CrudCodeMakerSettings settings;


    public CodeBlockMakerAction(String templateKey) {
        this.settings = ServiceManager.getService(CrudCodeMakerSettings.class);
        this.templateKey = templateKey;
        getTemplatePresentation().setDescription("description");
        getTemplatePresentation().setText(templateKey, false);
    }


    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {

        Project project = anActionEvent.getProject();
        if (project == null) {
            return;
        }
        DumbService dumbService = DumbService.getInstance(project);
        if (dumbService.isDumb()) {
            dumbService.showDumbModeNotification("CodeMaker plugin is not available during indexing");
            return;
        }
        PsiElement psiElement = anActionEvent.getData(LangDataKeys.PSI_ELEMENT);
        PsiClass currentPsiClass = null;
        if (psiElement != null && psiElement instanceof PsiClass) {
            currentPsiClass = (PsiClass) psiElement;
        }
        // 选取mapper类
        PsiClass psiMapperClass = CodeMakerUtil.chooseMapperClass(project, currentPsiClass);
        if (psiMapperClass == null) {
            return;
        }
        String modelName = getModelName(psiMapperClass);
        // 选取方法,过滤掉Object里面的方法
        SelectMethodDlg dlg = new SelectMethodDlg(project, psiMapperClass.getMethods(), "Select One Or More Method");
        dlg.show();
        // 选择条件。目前不自动生成条件,详细条件需要自己手动更改,example会生成具体实体类 TODO
        // 根据选择的方法自动生成代码块
        int[] selectedIndexs = dlg.getSelectionIdx();
        if (selectedIndexs.length < 1) {
            return;
        }
        StringBuilder allContent = new StringBuilder();
        for (int index : selectedIndexs) {
            MethodEntry methodEntry = MethodEntry.create(psiMapperClass.getMethods()[index]);
            CodeTemplate codeTemplate = settings.getCodeTemplate(templateKey);
            if (codeTemplate == null) {
                continue;
            }
            // 如果是example,生成方式会有差别 TODO
            boolean exampleFlag = false;
            if (methodEntry.getMethodName().endsWith("ByExample")) {
                exampleFlag = true;
            }
            // 渲染模板
            String content = VelocityUtil.evaluate(codeTemplate.getCodeTemplate(), createContext(methodEntry, modelName, exampleFlag));
            allContent.append(content).append("\r\n");
        }
        // 展示类容
        ShowCodeBlockDlg showCodeBlockDlg = new ShowCodeBlockDlg(project, allContent, "Copy Created Code");
        showCodeBlockDlg.show();
//        ShowTextDlg showTextDlg = new ShowTextDlg(allContent.toString());
//        showTextDlg.setVisible(true);
    }


    private Map<String, Object> createContext(MethodEntry methodEntry, String modelName, boolean exampleFlag) {

        Map<String, Object> context = new HashMap<>(3);
        context.put("methodName", methodEntry.getMethodName());
        context.put("parameters", methodEntry.getParameters());
        context.put("returnType", methodEntry.getReturnType());
        context.put("modelName", modelName);
        context.put("modelNameCamel", CodeMakerUtil.toCamel(modelName));
        context.put("exampleFlag", exampleFlag);
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
