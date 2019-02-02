package com.cying.ideatools.codemaker.crud.action;

import com.cying.ideatools.codemaker.crud.CodeTemplate;
import com.cying.ideatools.codemaker.crud.CrudCodeMakerSettings;
import com.cying.ideatools.codemaker.crud.MapperEntry;
import com.cying.ideatools.codemaker.crud.utils.CodeMakerUtil;
import com.cying.ideatools.codemaker.crud.utils.VelocityUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.JavaProjectRootsUtil;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.refactoring.PackageWrapper;
import com.intellij.refactoring.move.moveClassesOrPackages.MoveClassesOrPackagesUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author chengying13378 chengying13378@hundsun.com
 * @ClassName CrudCodeMakerAction
 * @Description TODO
 * @date 2019-01-26 23:39
 */
public class CrudCodeMakerAction extends AnAction implements DumbAware {


    private Logger LOGGER = LoggerFactory.getLogger(CrudCodeMakerAction.class);

    private String templateKey;

    private CrudCodeMakerSettings settings;


    public CrudCodeMakerAction(String templateKey) {
        this.settings = ServiceManager.getService(CrudCodeMakerSettings.class);
        this.templateKey = templateKey;
        getTemplatePresentation().setDescription("description");
        getTemplatePresentation().setText(templateKey, false);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {

        Project project = anActionEvent.getProject();
        if (project == null) {
            return;
        }
        DumbService dumbService = DumbService.getInstance(project);
        if (dumbService.isDumb()) {
            dumbService.showDumbModeNotification("CodeMaker plugin is not available during indexing");
            return;
        }
        PsiElement psiElement = anActionEvent.getData(LangDataKeys.PSI_ELEMENT);
        PsiClass currentPsiClass = null;
        if (psiElement != null && psiElement instanceof PsiClass) {
            currentPsiClass = (PsiClass) psiElement;
        }
        // 选取mapper类
        PsiClass psiMapperClass = CodeMakerUtil.chooseMapperClass(project, currentPsiClass);
        if (psiMapperClass == null) {
            return;
        }
        MapperEntry mapperEntry = MapperEntry.create(psiMapperClass);

        if (mapperEntry == null) {
            Messages.showMessageDialog(project, "No Classes found", "Generate Failed", null);
            return;
        }

        try {

            // 可以考虑选取多个mapper文件 TODO
            Map<String, Object> context = createContext(mapperEntry);
            context.put("createDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            CodeTemplate codeTemplate = settings.getCodeTemplate(templateKey + "-interface");
            String content = VelocityUtil.evaluate(codeTemplate.getCodeTemplate(), context);
            String className = VelocityUtil.evaluate(codeTemplate.getClassNameVm(), context);
            context.put("BR", "\n");

            // 获取代码生成根路径(包含包名)
            VirtualFile sourceRoot = findSourceRoot(mapperEntry, project, psiMapperClass);

            if (sourceRoot != null) {
                String language = psiMapperClass.getLanguage().getID().toLowerCase();
                String sourcePath = sourceRoot.getPath() + File.separator + mapperEntry.getMapperPackages().replace(".", File.separator);
                String targetPath = CodeMakerUtil.generateClassPath(sourcePath, className, language);

                VirtualFileManager manager = VirtualFileManager.getInstance();
                VirtualFile virtualFile = manager
                        .refreshAndFindFileByUrl(VfsUtil.pathToUrl(targetPath));

                if (virtualFile == null || !virtualFile.exists() || userConfirmedOverride()) {
                    // async write action
                    ApplicationManager.getApplication().runWriteAction(
                            new CreateFileAction(targetPath, content, "UTF-8", anActionEvent
                                    .getDataContext()));
                }
            }

        } catch (Exception e) {
            Messages.showMessageDialog(project, e.getMessage(), "Generate Failed", null);
        }

    }

    private Map<String, Object> createContext(MapperEntry mapperEntry) {

        Map<String, Object> context = new HashMap<>(10);
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

    /**
     * 选择代码生成路径
     *
     * @param mapperEntry
     * @param project
     * @param psiElement
     * @return
     */
    private VirtualFile findSourceRoot(MapperEntry mapperEntry, Project project, PsiElement psiElement) {

        String packageName = mapperEntry.getMapperPackages();
        final PackageWrapper targetPackage = new PackageWrapper(PsiManager.getInstance(project), packageName);
        List<VirtualFile> suitableRoots = JavaProjectRootsUtil.getSuitableDestinationSourceRoots(project);
        if (suitableRoots.size() > 1) {
            return MoveClassesOrPackagesUtil.chooseSourceRoot(targetPackage, suitableRoots,
                    psiElement.getContainingFile().getContainingDirectory());

        } else if (suitableRoots.size() == 1) {
            return suitableRoots.get(0);
        }
        return null;
    }


    private boolean userConfirmedOverride() {
        return Messages.showYesNoDialog("Overwrite?", "File Exists", null) == Messages.OK;
    }


    public static class Utils {
        public String mkString(Collection<?> list, String delimiter, String prefix, String suffix) {
            if (list.isEmpty()) {

                return "";
            } else {
                return list.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(delimiter, prefix, suffix));
            }
        }

        public String delim(Collection<?> list, int velocityCount, String delim) {
            if (velocityCount < list.size()) {
                return delim;
            } else {
                return "";
            }
        }

    }

}
