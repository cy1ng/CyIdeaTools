package com.cying.ideatools.codemaker.crud;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.util.xmlb.XmlSerializerUtil;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chengying13378 chengying13378@hundsun.com
 * @ClassName CrudCodeMakerSettings
 * @Description TODO
 * @date 2019-01-26 23:51
 */
@State(name = "CodeMakerSettings", storages = {@Storage(file = "$APP_CONFIG$/CodeMaker-settings.xml")})
public class CrudCodeMakerSettings implements PersistentStateComponent<CrudCodeMakerSettings> {

    private Logger LOGGER = LoggerFactory.getLogger(CrudCodeMakerSettings.class);

    @Setter
    private Map<String, CodeTemplate> codeTemplates;

    private void loadDefaultSettings() {
        try {
            Map<String, CodeTemplate> codeTemplates = new HashMap<>();
            //TODO
            codeTemplates.put("manager-code-block",
                    createCodeTemplate("manager-code-block.vm", "", CodeTemplate.DEFAULT_ENCODING));
            codeTemplates.put("service-code-block",
                    createCodeTemplate("service-code-block.vm", "", CodeTemplate.DEFAULT_ENCODING));
            codeTemplates.put("controller-code-block",
                    createCodeTemplate("controller-code-block.vm", "", CodeTemplate.DEFAULT_ENCODING));
            codeTemplates.put("manager-interface",
                    createCodeTemplate("manager-interface.vm", "I${modelName}Manager", CodeTemplate.DEFAULT_ENCODING));
            codeTemplates.put("manager-impl",
                    createCodeTemplate("manager-impl.vm", "${modelName}ManagerImpl", CodeTemplate.DEFAULT_ENCODING));
            codeTemplates.put("service-interface",
                    createCodeTemplate("service-interface.vm", "I${modelName}ServiceProvider", CodeTemplate.DEFAULT_ENCODING));
            codeTemplates.put("service-impl",
                    createCodeTemplate("service-impl.vm", "${modelName}ServiceProviderImpl", CodeTemplate.DEFAULT_ENCODING));
            this.codeTemplates = codeTemplates;
        } catch (Exception e) {
            LOGGER.error("loadDefaultSettings failed", e);
        }
    }

    @NotNull
    private CodeTemplate createCodeTemplate(String sourceTemplateName, String classNameVm, String fileEncoding) throws IOException {
        String velocityTemplate = FileUtil.loadTextAndClose(CrudCodeMakerSettings.class.getResourceAsStream("/template/crud/" + sourceTemplateName));
        return new CodeTemplate(sourceTemplateName,
                classNameVm, velocityTemplate, fileEncoding);
    }

    public CodeTemplate getCodeTemplate(String template) {
        return codeTemplates.get(template);
    }

    public void removeCodeTemplate(String template) {
        codeTemplates.remove(template);
    }

    public Map<String, CodeTemplate> getCodeTemplates() {
        if (codeTemplates == null) {
            loadDefaultSettings();
        }
        return codeTemplates;
    }

    @Nullable
    @Override
    public CrudCodeMakerSettings getState() {
        if (this.codeTemplates == null) {
            loadDefaultSettings();
        }
        return this;
    }

    @Override
    public void loadState(@NotNull CrudCodeMakerSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
