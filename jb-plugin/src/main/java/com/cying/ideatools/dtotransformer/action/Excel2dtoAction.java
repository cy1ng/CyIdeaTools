package com.cying.ideatools.dtotransformer.action;

import com.cying.ideatools.dtotransformer.ui.Excel2dtoSelectDlg;
import com.cying.ideatools.dtotransformer.ui.Excel2dtoSelectUI;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import org.jetbrains.annotations.NotNull;

/**
 * @author chengying13378 chengying13378@hundsun.com
 * @description excel转dto action
 * @date 2019-04-13 15:42
 */
public class Excel2dtoAction extends AnAction implements DumbAware {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        // 1. 选取excel、设置起始页数和结束页数
        // 2. 设置目标pojo文件存放目录
        Excel2dtoSelectDlg dlg = new Excel2dtoSelectDlg(e.getProject(),"设置");
        dlg.show();
    }
}
