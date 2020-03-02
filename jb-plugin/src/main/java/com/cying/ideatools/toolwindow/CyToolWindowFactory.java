package com.cying.ideatools.toolwindow;

import com.cying.ideatools.toolwindow.ui.CyToolWindowUI;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowType;
import com.intellij.openapi.wm.impl.content.ToolWindowContentUi;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;

public class CyToolWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull final Project project, @NotNull final ToolWindow toolWindow) {

        final Content toolContent = toolWindow.getContentManager().getFactory().createContent(
                new CyToolWindowPanel(toolWindow, project), "Enum Code Finder", false);
        final Content toolContent2 = toolWindow.getContentManager().getFactory().createContent(
                new CyToolWindowPanel(toolWindow, project), "CyIdeaTools2", false);
        final Content toolContent3 = toolWindow.getContentManager().getFactory().createContent(
                new CyToolWindowUI().$$$getRootComponent$$$(), "CyIdeaTools3", true);
//        ToolWindowContentUi ui = new ToolWindowContentUi()
        ContentManager contentManager = toolWindow.getContentManager();
        contentManager.addContent(toolContent);
        contentManager.addContent(toolContent2);
        contentManager.addContent(toolContent3);
        toolWindow.setTitle("CyIdeaTools");
        toolWindow.setType(ToolWindowType.DOCKED, null);
    }

}
