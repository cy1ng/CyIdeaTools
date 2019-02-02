package com.cying.ideatools.codemaker.crud.action;

import com.cying.ideatools.codemaker.crud.CrudCodeMakerSettings;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.DumbAware;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Arrays;
import static com.cying.ideatools.codemaker.crud.CodeTemplateNameConsts.*;

/**
 * @author hansong.xhs
 * @version $Id: CodeMakerGroup.java, v 0.1 2017-01-28 9:25 hansong.xhs Exp $$
 */
public class CodeMakerGroup extends ActionGroup implements DumbAware {


    private CrudCodeMakerSettings settings;

    public CodeMakerGroup() {
        settings = ServiceManager.getService(CrudCodeMakerSettings.class);
    }

    /**
     * @see ActionGroup#getChildren(AnActionEvent)
     */
    @NotNull
    @Override
    public AnAction[] getChildren(@Nullable AnActionEvent anActionEvent) {

        return Arrays.asList(/**getOrCreateAction(AllDefault),*/
                getOrCreateAction(ManagerDefault),
                getOrCreateAction(ServiceDefault),
                getOrCreateAction(ControllerDefault),
                getOrCreateAction(ManagerCodeBlock),
                getOrCreateAction(ServiceCodeBlock),
                getOrCreateAction(ControllerCodeBlock)).toArray(new AnAction[0]);
    }

    private AnAction getOrCreateAction(String templateName) {
        final String actionId = "CrudCodeMaker.Menu.Action." + templateName;
        AnAction action = ActionManager.getInstance().getAction(actionId);
        if(action != null){
            return action;
        }
        if(templateName.endsWith(CodeBlockSuffix)){
            action = new CodeBlockMakerAction(templateName);
        }else{
            action = new CrudCodeMakerAction(templateName);
        }
        ActionManager.getInstance().registerAction(actionId, action);
        return action;
    }
}
