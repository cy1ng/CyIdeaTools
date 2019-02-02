package com.cying.ideatools.codemaker.crud.ui;

import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiMethod;
import com.intellij.util.ui.TextTransferable;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author chengying13378 chengying13378@hundsun.com
 * @ClassName SelectMethodDlg
 * @Description TODO
 * @date 2019-01-30 10:29
 */
public class ShowCodeBlockDlg extends DialogWrapper {

    private ShowCodeBlockUI showCodeBlockUI;

    public ShowCodeBlockDlg(@Nullable Project project, StringBuilder content, String title) {
        super(project, true);
        showCodeBlockUI = new ShowCodeBlockUI(content.toString());
        setTitle(title);
        init();
        // 重置名称
        myOKAction.putValue(Action.NAME,"Copy To Clipboard");
    }

    @Override
    protected void doOKAction() {
        CopyPasteManager.getInstance().setContents(new TextTransferable(showCodeBlockUI.getText()));
        //禁用父类的OK处理
//        super.doOKAction();
    }


    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return showCodeBlockUI.$$$getRootComponent$$$();
    }


}
