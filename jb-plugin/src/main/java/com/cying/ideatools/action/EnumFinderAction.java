package com.cying.ideatools.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.AnnotatedElementsSearch;
import org.jetbrains.annotations.NotNull;


public class EnumFinderAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);

        String title = "标题";
        String msg = "2018,起航";

        Messages.showMessageDialog(project, msg, title, Messages.getInformationIcon());
        String annotationClassName = NotNull.class.getCanonicalName();
        PsiClass annotationPsiClass = JavaPsiFacade.getInstance(project).findClass(annotationClassName, GlobalSearchScope.projectScope(project));
        AnnotatedElementsSearch.Parameters parameters =
                new AnnotatedElementsSearch.Parameters(annotationPsiClass, GlobalSearchScope.projectScope(project));
        AnnotatedElementsSearch.searchElements(parameters);
    }
}
