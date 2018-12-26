package com.cying.ideatools.toolwindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;
import java.awt.*;

/**
 * @author chengying13378 chengying13378@hundsun.com
 * @ClassName EnumFinderToolWindow
 * @Description 工具窗口
 * @date 2018-12-23 17:32
 */
public class EnumFinderToolWindowPanel extends JPanel {

    private final Project project;

    private final ToolWindow toolWindow;

    public EnumFinderToolWindowPanel(final ToolWindow toolWindow, final Project project) {

        super(new BorderLayout());

        this.toolWindow = toolWindow;
        this.project = project;
    }
}