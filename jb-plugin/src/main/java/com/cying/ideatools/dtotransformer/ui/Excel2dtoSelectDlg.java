package com.cying.ideatools.dtotransformer.ui;

import com.excel.parse.UPPSExcelParsePojoUtils;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;
import java.io.File;

/**
 * @author chengying13378 chengying13378@hundsun.com
 * @ClassName Excel2dtoSelectDlg
 * @Description excel转dto
 * @date 2019-01-30 10:29
 */
public class Excel2dtoSelectDlg extends DialogWrapper {

    private Excel2dtoSelectUI excel2dtoSelectUI;

    public Excel2dtoSelectDlg(@Nullable Project project, String title) {
        super(project, true);
        excel2dtoSelectUI = new Excel2dtoSelectUI(project);
        setTitle(title);
        init();
    }

    @Override
    protected void doOKAction() {

        // 开始创建类
        String excelAbsPath = excel2dtoSelectUI.getExcelPath();
        String javaFileAbsPath = excel2dtoSelectUI.getPojoBasePath();
        int startPage = excel2dtoSelectUI.getStartPage();
        int endPage = excel2dtoSelectUI.getEndPage();
        String reqAbsPath = javaFileAbsPath + File.separator + "req";
        String respAbsPath = javaFileAbsPath + File.separator + "resp";
        String author = excel2dtoSelectUI.getAuthor();
        String packages = excel2dtoSelectUI.getPackages();
        try {
            new File(reqAbsPath).mkdirs();
            new File(respAbsPath).mkdirs();
            UPPSExcelParsePojoUtils.genPojoFile(excelAbsPath,packages,author,reqAbsPath,respAbsPath,startPage,endPage);
        } catch (Throwable e) {
            e.printStackTrace();
            super.doCancelAction();
            ApplicationManager.getApplication().invokeLater(() -> Messages.showMessageDialog("Fail due to: " + e.getMessage(), "Generate Failed", null));
        }
        super.doOKAction();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return excel2dtoSelectUI.$$$getRootComponent$$$();
    }

}
