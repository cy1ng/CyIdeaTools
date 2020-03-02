package com.cying.ideatools.gaps.toolwindow;


import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowType;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import gherkin.lexer.Tr;
import org.jetbrains.annotations.NotNull;

/**
 * @author chengying13378 chengying13378@hundsun.com
 * @description TODO
 * @date 2019-11-13 11:00
 */
public class GapsToolWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        final Content toolContent = contentFactory.createContent(
                new GapsToolWindowUI(project).rootJpanel, "GapsFlow", true);

        ContentManager contentManager = toolWindow.getContentManager();
        contentManager.addContent(toolContent);
        toolWindow.setTitle("GapsViewer");
        toolWindow.setType(ToolWindowType.DOCKED, null);
    }

}
