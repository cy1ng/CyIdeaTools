package com.cying.ideatools.dtotransformer.action;

import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.search.GlobalSearchScope;
import com.pojo.format.UPPSPojoStruture;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chengying13378 chengying13378@hundsun.com
 * @description excel转dto action
 * @date 2019-04-13 15:42
 */
public class Dto2excelAction extends AnAction implements DumbAware {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {

        PsiElement psiElement = anActionEvent.getData(LangDataKeys.PSI_ELEMENT);
        Project project = anActionEvent.getProject();
        PsiClass currentPsiClass = null;
        if (psiElement != null && psiElement instanceof PsiClass) {
            currentPsiClass = (PsiClass) psiElement;
        }
        if (currentPsiClass == null) {
            ApplicationManager.getApplication().invokeLater(() -> Messages.showMessageDialog("Selected File Not A Class", "Generate Failed", null));
            return;
        }
        PsiJavaFile psiJavaFile = ((PsiJavaFile) currentPsiClass.getContainingFile());
        PackageChooserDialog pcd = new PackageChooserDialog("请选择dto根路径", project);
        pcd.show();
        PsiPackage psiPackage = pcd.getSelectedPackage();
        ApplicationManager.getApplication().invokeLater(() -> Messages.showMessageDialog(psiPackage.getName(), "Generate Failed", null));
        PsiPackage[] children = psiPackage.getSubPackages(GlobalSearchScope.moduleScope(anActionEvent.getData(LangDataKeys.MODULE)));
        if (children.length < 2) {
            ApplicationManager.getApplication().invokeLater(() -> Messages.showMessageDialog(psiPackage.getQualifiedName() + "的子包不合法", "Generate Failed", null));
        }
        List<UPPSPojoStruture> pojoStrutures = new ArrayList<>();
        PsiPackage reqPackage = null, respPackage = null, beanPackage = null;
        for (PsiPackage child : children) {
            if ("req".equals(child.getName())) {
                reqPackage = child;
            } else if ("resp".equals(child.getName())) {
                respPackage = child;
            } else if ("bean".equals(child.getName())) {
                beanPackage = child;
            }
        }
        if(reqPackage == null || respPackage == null){
            ApplicationManager.getApplication().invokeLater(() -> Messages.showMessageDialog(psiPackage.getQualifiedName() + "的子包不包含req和resp", "Generate Failed", null));
        }
        // 解析请求包和响应包的类
        for(PsiClass psiClass : reqPackage.getClasses()){

        }
    }
}
