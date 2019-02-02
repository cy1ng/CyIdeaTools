package com.cying.ideatools.codemaker.crud.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

/**
 * @author chengying13378 chengying13378@hundsun.com
 * @ClassName SelectMethodDlg
 * @Description TODO
 * @date 2019-01-30 10:29
 */
public class TemplateSelectDlg extends DialogWrapper {

    private TemplateSelectUI templateSelectUI;

    public TemplateSelectDlg(@Nullable Project project, String title) {
        super(project, true);
        templateSelectUI = new TemplateSelectUI();
        setTitle(title);
        init();
    }


    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return templateSelectUI.$$$getRootComponent$$$();
    }

    public List<String> getTemplateName() {
        return templateSelectUI.getSelTemplateNames();
    }

    public String getTemplateType() {
        return templateSelectUI.getTemplateType();
    }
}
