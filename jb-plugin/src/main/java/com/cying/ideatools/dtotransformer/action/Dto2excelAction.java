package com.cying.ideatools.dtotransformer.action;

import com.excel.parse.JavaFileToStruture;
import com.excel.parse.UPPSPojoParseExcelUtils;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.pojo.format.UPPSPojoStruture;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author chengying13378 chengying13378@hundsun.com
 * @description excel转dto action
 * @date 2019-04-13 15:42
 */
public class Dto2excelAction extends AnAction implements DumbAware {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {

        PsiElement psiElement = anActionEvent.getData(LangDataKeys.PSI_ELEMENT);
        Project project = anActionEvent.getProject();

        FileChooserDescriptor singleFileOrDirDescriptor = FileChooserDescriptorFactory.createSingleFileOrFolderDescriptor();
        VirtualFile virtualFile = FileChooser.chooseFile(singleFileOrDirDescriptor, project, null);
        if (!virtualFile.isDirectory()) {
            ApplicationManager.getApplication().invokeLater(() -> Messages.showMessageDialog("请选择一个目录", "Generate Failed", null));
            return;
        }

        String path = virtualFile.getPath();
        // 拿到dto的绝对路径
        String directoryPath = path;

        String toExcelBasePath = System.getProperty("user.home");
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String toExcelPath = toExcelBasePath + File.separator + dateTimeFormatter.format(now) + "api.xlsx";
        String templatePath = "/excel/api_template.xlsx";
        List<UPPSPojoStruture> pojoList = null;
        // 创建目录和文件
        File targetExcel = new File(toExcelBasePath);
        targetExcel.mkdirs();
        try (InputStream in = getClass().getResourceAsStream(templatePath)) {
            pojoList = JavaFileToStruture.createPojoList(directoryPath);
            XSSFWorkbook fromBook = new XSSFWorkbook(in);
            UPPSPojoParseExcelUtils.createPojoExcel(pojoList, fromBook, toExcelPath);
        } catch (Exception e) {
            e.printStackTrace();
            ApplicationManager.getApplication().invokeLater(() -> Messages.showMessageDialog(e.getMessage(), "Generate Failed", null));
        }
        ApplicationManager.getApplication().invokeLater(() -> Messages.showMessageDialog("API Excel生成路径:" + toExcelPath, "Generate Success", null));

//        ApplicationManager.getApplication().invokeLater(() -> Messages.showMessageDialog(psiPackage.getName(), "Generate Failed", null));
//        PsiPackage[] children = psiPackage.getSubPackages(GlobalSearchScope.moduleScope(anActionEvent.getData(LangDataKeys.MODULE)));
//        if (children.length < 2) {
//            ApplicationManager.getApplication().invokeLater(() -> Messages.showMessageDialog(psiPackage.getQualifiedName() + "的子包不合法", "Generate Failed", null));
//        }
//        List<UPPSPojoStruture> pojoStrutures = new ArrayList<>();
//        PsiPackage reqPackage = null, respPackage = null, beanPackage = null;
//        for (PsiPackage child : children) {
//            if ("req".equals(child.getName())) {
//                reqPackage = child;
//            } else if ("resp".equals(child.getName())) {
//                respPackage = child;
//            } else if ("bean".equals(child.getName())) {
//                beanPackage = child;
//            }
//        }
//        if(reqPackage == null || respPackage == null){
//            ApplicationManager.getApplication().invokeLater(() -> Messages.showMessageDialog(psiPackage.getQualifiedName() + "的子包不包含req和resp", "Generate Failed", null));
//        }
//        // 解析请求包和响应包的类
//        for(PsiClass psiClass : reqPackage.getClasses()){
//
//        }
    }
}
