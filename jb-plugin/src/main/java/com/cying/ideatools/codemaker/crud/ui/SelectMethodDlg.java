package com.cying.ideatools.codemaker.crud.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiMethod;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

/**
 * @author chengying13378 chengying13378@hundsun.com
 * @ClassName SelectMethodDlg
 * @Description TODO
 * @date 2019-01-30 10:29
 */
public class SelectMethodDlg extends DialogWrapper {

    private SelectMethodUI selectMethodComponent;

    public SelectMethodDlg(@Nullable Project project, PsiMethod[] psiMethods, String title) {
        super(project, true);
        // 将PsiMethod转换成字符串
        selectMethodComponent = new SelectMethodUI(psiMethods);
        setTitle(title);
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return selectMethodComponent.$$$getRootComponent$$$();
    }

    public int[] getSelectionIdx() {
        if (!isOK()) {
            return null;
        }
        return selectMethodComponent.getMyList().getSelectedIndices();
    }

}
