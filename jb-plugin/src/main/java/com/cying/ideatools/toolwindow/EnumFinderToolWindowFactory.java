package com.cying.ideatools.toolwindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowType;
import com.intellij.ui.content.Content;
import org.jetbrains.annotations.NotNull;

public class EnumFinderToolWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull final Project project, @NotNull final ToolWindow toolWindow) {
        final Content toolContent = toolWindow.getContentManager().getFactory().createContent(
                new EnumFinderToolWindowPanel(toolWindow, project), "CyIdeaTools", false);
        toolWindow.getContentManager().addContent(toolContent);

        toolWindow.setTitle("CyIdeaTools");
        toolWindow.setType(ToolWindowType.DOCKED, null);
    }

}
