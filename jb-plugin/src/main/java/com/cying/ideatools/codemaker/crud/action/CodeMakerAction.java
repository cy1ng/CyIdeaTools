package com.cying.ideatools.codemaker.crud.action;

import com.cying.ideatools.codemaker.crud.action.creator.CodeBlockMaker;
import com.cying.ideatools.codemaker.crud.action.creator.FileMaker;
import com.cying.ideatools.codemaker.crud.action.creator.IMaker;
import com.cying.ideatools.codemaker.crud.ui.TemplateSelectDlg;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author chengying13378 chengying13378@hundsun.com
 * @ClassName CodeMakerAction
 * @Description TODO
 * @date 2019-01-31 00:31
 */
public class CodeMakerAction extends AnAction implements DumbAware {


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
        // 1.模板选择
        TemplateSelectDlg templateSelectDlg = new TemplateSelectDlg(project, "Select Template");
        templateSelectDlg.show();
        if (!templateSelectDlg.isOK()) {
            return;
        }
        // 2.获取选择的构建类型以及模板列表
        String type = templateSelectDlg.getTemplateType();
        List<String> templateNames = templateSelectDlg.getTemplateName();
        // 3.根据选择内容选择处理器执行
        IMaker creator = null;
        if (IMaker.FILE_TYPE.equals(type)) {
            creator = new FileMaker(project, currentPsiClass, templateNames, anActionEvent.getDataContext());
        } else if (IMaker.BLOCK_TYPE.equals(type)) {
            creator = new CodeBlockMaker(project, currentPsiClass, templateNames);
        } else {
            return;
        }
        creator.make();
    }
}
