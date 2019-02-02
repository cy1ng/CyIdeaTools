package com.cying.ideatools.codemaker.crud.action.creator;

import com.cying.ideatools.codemaker.crud.CodeTemplate;
import com.cying.ideatools.codemaker.crud.CrudCodeMakerSettings;
import com.cying.ideatools.codemaker.crud.MapperEntry;
import com.cying.ideatools.codemaker.crud.action.CreateFileAction;
import com.cying.ideatools.codemaker.crud.utils.CodeMakerUtil;
import com.cying.ideatools.codemaker.crud.utils.VelocityUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiClass;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chengying13378 chengying13378@hundsun.com
 * @ClassName FileCreator
 * @Description TODO
 * @date 2019-01-31 00:23
 */
public class FileMaker implements IMaker {

    private CrudCodeMakerSettings settings;

    private Project project;
    private List<String> templateNames;
    private PsiClass currentPsiClass;
    private DataContext dataContext;


    public FileMaker(@NotNull Project project,
                     @Nullable PsiClass currentPsiClass,
                     List<String> templateNames,
                     DataContext dataContext) {
        this.project = project;
        this.templateNames = templateNames;
        this.settings = ServiceManager.getService(CrudCodeMakerSettings.class);
        this.currentPsiClass = currentPsiClass;
        this.dataContext = dataContext;
    }

    @Override
    public void make() {

        // 1.选取mapper类
        PsiClass psiMapperClass = CodeMakerUtil.chooseMapperClass(project, currentPsiClass);
        if (psiMapperClass == null) {
            return;
        }
        MapperEntry mapperEntry = MapperEntry.create(psiMapperClass);

        if (mapperEntry == null) {
            Messages.showMessageDialog(project, "No Classes found", "Generate Failed", null);
            return;
        }
        // 2.根据每个模板生成该mapper类
        if (CollectionUtils.isEmpty(templateNames)) {
            return;
        }
        // 3.选取存储路径
        VirtualFile sourceRoot = CodeMakerUtil.findSourceRoot(mapperEntry, project, psiMapperClass);
        if (sourceRoot == null) {
            return;
        }
        String language = psiMapperClass.getLanguage().getID().toLowerCase();
        String targetBasePath = new StringBuilder(sourceRoot.getPath())
                .append(File.separator)
                .append(mapperEntry.getMapperPackages().replace(".", File.separator)).toString();

        for (String templateName : templateNames) {
            // 可以考虑选取多个mapper文件 TODO
            Map<String, Object> context = createContext(mapperEntry);
            CodeTemplate codeTemplate = settings.getCodeTemplate(templateName);
            String content = VelocityUtil.evaluate(codeTemplate.getCodeTemplate(), context);
            String className = VelocityUtil.evaluate(codeTemplate.getClassNameVm(), context);
            String path = targetBasePath + File.separator + className + "." + language;
            VirtualFileManager manager = VirtualFileManager.getInstance();
            VirtualFile virtualFile = manager
                    .refreshAndFindFileByUrl(VfsUtil.pathToUrl(path));

            if (virtualFile == null || !virtualFile.exists() || userConfirmedOverride()) {
                // async write action
                ApplicationManager.getApplication().runWriteAction(
                        new CreateFileAction(path, content, "UTF-8", dataContext));
            }
        }


    }


    private Map<String, Object> createContext(MapperEntry mapperEntry) {

        Map<String, Object> context = new HashMap<>(10);
        context.put("createDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        context.put("user", mapperEntry.getUser());
        context.put("mail", "cy70624523@163.com");
        context.put("mapperPackages", mapperEntry.getMapperPackages());
        context.put("modelPackages", mapperEntry.getModelPackages());
        context.put("modelName", mapperEntry.getModelName());
        context.put("modelNameCamel", mapperEntry.getModelNameCamel());
        context.put("modelKeyType", mapperEntry.getModelKeyType());
        context.put("modelKeyCamel", mapperEntry.getModelKeyCamel());
        return context;
    }

    private boolean userConfirmedOverride() {
        return Messages.showYesNoDialog("Overwrite?", "File Exists", null) == Messages.OK;
    }
}
