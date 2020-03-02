package com.cying.ideatools.dtotransformer.action;

import com.cying.ideatools.gaps.GapsCompProviderVisitor;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chengying13378 chengying13378@hundsun.com
 * @description excel转dto action
 * @date 2019-04-13 15:42
 */
public class CreateMicroComponentAction extends AnAction implements DumbAware {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {

        PsiElement psiElement = anActionEvent.getData(LangDataKeys.PSI_ELEMENT);
        Project project = anActionEvent.getProject();
        // 解析出文件路径
        PsiClass currentPsiClass = null;
        if (psiElement != null && psiElement instanceof PsiClass) {
            currentPsiClass = (PsiClass) psiElement;
        } else {
            ApplicationManager.getApplication().invokeLater(() -> Messages.showMessageDialog("Please Select Provider Interface Class", "Generate Failed", null));
            return;
        }
        String javaSrcPath = currentPsiClass.getContainingFile().getVirtualFile().getCanonicalPath();
        if (!javaSrcPath.endsWith("Provider.java")) {
            ApplicationManager.getApplication().invokeLater(() -> Messages.showMessageDialog("Java File Not Provider", "Generate Failed", null));
            return;
        }
        try {
            FileInputStream in = new FileInputStream(javaSrcPath);
            CompilationUnit compilationUnit = StaticJavaParser.parse(in);
            compilationUnit.accept(new GapsCompProviderVisitor(null), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            ApplicationManager.getApplication().invokeLater(() -> Messages.showMessageDialog(e.getMessage(), "Generate Failed", null));
            return;
        }

        ApplicationManager.getApplication().invokeLater(() -> Messages.showMessageDialog("Component Or Flow File In User's Home Named GapsGen", "Generate Success", null));

    }


    @Data
    @AllArgsConstructor
    public static class ReqInfo {

        private String reqParamName;

        private String reqClassName;
    }
}
