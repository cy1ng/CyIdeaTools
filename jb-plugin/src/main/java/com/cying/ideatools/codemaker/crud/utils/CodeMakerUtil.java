package com.cying.ideatools.codemaker.crud.utils;

import com.cying.ideatools.codemaker.crud.ClassEntry;
import com.cying.ideatools.codemaker.crud.MapperEntry;
import com.google.common.collect.Lists;
import com.intellij.ide.util.TreeClassChooser;
import com.intellij.ide.util.TreeClassChooserFactory;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.JavaProjectRootsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.PackageWrapper;
import com.intellij.refactoring.move.moveClassesOrPackages.MoveClassesOrPackagesUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author cy1ng
 */
public class CodeMakerUtil {

    public static Logger getLogger(Class clazz) {
        return Logger.getInstance(clazz);
    }

    public static PsiClass chooseClass(Project project, PsiClass defaultClass) {
        TreeClassChooser chooser = TreeClassChooserFactory.getInstance(project)
                .createProjectScopeChooser("Select mapper class", defaultClass);

        chooser.showDialog();

        return chooser.getSelected();
    }


    public static PsiClass chooseMapperClass(Project project,PsiClass defaultPsiClass) {

        PsiClass baseClass = JavaPsiFacade.getInstance(project).findClass(CommonClassNames.JAVA_LANG_OBJECT, GlobalSearchScope.allScope(project));
        TreeClassChooser chooser = TreeClassChooserFactory.getInstance(project).
            createInheritanceClassChooser( "Select mapper class",
                    GlobalSearchScope.allScope(project),
                    baseClass,
                    defaultPsiClass,
                    psiClass -> psiClass.getName().endsWith("Mapper"));
        chooser.showDialog();
        return chooser.getSelected();
    }

    public static PsiClass chooseMapperMethod(Project project,PsiClass defaultPsiClass) {
        //TODO
       return null;
    }

    public static String getSourcePath(PsiClass clazz) {
        PsiFile containingFile = clazz.getContainingFile();
        return getSourcePath(containingFile);
    }

    @NotNull
    public static String getSourcePath(PsiFile psiFile) {
        String classPath = psiFile.getVirtualFile().getPath();
        return classPath.substring(0, classPath.lastIndexOf('/'));
    }

    public static String generateClassPath(String sourcePath, String className) {
        return generateClassPath(sourcePath, className, "java");
    }

    public static String generateClassPath(String sourcePath, String className, String extension) {
        return sourcePath + File.separator + className + "." + extension;
    }

    public static List<String> getImportList(PsiJavaFile javaFile) {
        PsiImportList importList = javaFile.getImportList();
        if (importList == null) {
            return new ArrayList<>();
        }
        return Arrays.stream(importList.getImportStatements())
                .map(PsiImportStatement::getQualifiedName).collect(Collectors.toList());
    }

//    public static List<String> getScalaImportList(ScalaFile scalaFile) {
//        List<ScImportStmt> scImportStmts = seqAsJavaList(scalaFile.importStatementsInHeader());
//        return scImportStmts.stream()
//                .flatMap(stmt -> seqAsJavaList(stmt.importExprs()).stream()
//                        .map(expr -> expr.getText())).collect(Collectors.toList());
//    }

    public static List<ClassEntry.Field> getFields(PsiClass psiClass) {
        return Arrays
                .stream(psiClass.getFields())
                .map(
                        psiField -> new ClassEntry.Field(psiField.getType().getPresentableText(), psiField
                                .getName(), psiField.getModifierList() == null ? "" : psiField
                                .getModifierList().getText())).collect(Collectors.toList());
    }

    public static List<ClassEntry.Field> getAllFields(PsiClass psiClass) {
        return Arrays
                .stream(psiClass.getAllFields())
                .map(
                        psiField -> new ClassEntry.Field(psiField.getType().getPresentableText(), psiField
                                .getName(), psiField.getModifierList() == null ? "" : psiField
                                .getModifierList().getText())).collect(Collectors.toList());
    }

    public static List<ClassEntry.Method> getMethods(PsiClass psiClass) {
        return Arrays
                .stream(psiClass.getMethods())
                .map(
                        psiMethod -> {
                            String returnType = psiMethod.getReturnType() == null ? "" : psiMethod
                                    .getReturnType().getPresentableText();
                            return new ClassEntry.Method(psiMethod.getName(), psiMethod.getModifierList()
                                    .getText(), returnType, psiMethod.getParameterList().getText());
                        }).collect(Collectors.toList());
    }

    public static List<ClassEntry.Method> getAllMethods(PsiClass psiClass) {
        return Arrays
                .stream(psiClass.getAllMethods())
                .map(
                        psiMethod -> {
                            String returnType = psiMethod.getReturnType() == null ? "" : psiMethod
                                    .getReturnType().getPresentableText();
                            return new ClassEntry.Method(psiMethod.getName(), psiMethod.getModifierList()
                                    .getText(), returnType, psiMethod.getParameterList().getText());
                        }).collect(Collectors.toList());
    }


    /**
     * find the method belong to  name
     *
     * @return null if not found
     */
    public static String findClassNameOfSuperMethod(PsiMethod psiMethod) {
        PsiMethod[] superMethods = psiMethod.findDeepestSuperMethods();
        if (superMethods.length == 0 || superMethods[0].getContainingClass() == null) {
            return null;
        }
        return superMethods[0].getContainingClass().getQualifiedName();
    }

    /**
     * Gets all classes in the element.
     *
     * @param element the Element
     * @return the Classes
     */
    public static List<PsiClass> getClasses(PsiElement element) {
        List<PsiClass> elements = Lists.newArrayList();
        List<PsiClass> classElements = PsiTreeUtil.getChildrenOfTypeAsList(element, PsiClass.class);
        elements.addAll(classElements);
        for (PsiClass classElement : classElements) {
            elements.addAll(getClasses(classElement));
        }
        return elements;
    }

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }

        int length = str.length();

        for (int i = 0; i < length; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

//    public static List<ClassEntry.Field> getScalaClassFields(ScClass scalaClass) {
//        return seqAsJavaList(scalaClass.allVals())
//                .stream()
//                .filter(tuple -> tuple._1() instanceof ScClassParameter)
//                .map(tuple -> {
//                    ScClassParameter val = (ScClassParameter) tuple._1();
//                    return new ClassEntry.Field(val.paramType().get().getText(),
//                            val.name(), val.getModifierList().getText());
//                })
//                .collect(Collectors.toList());
//    }

    public static List<String> getClassTypeParameters(PsiClass psiClass) {
        return Arrays.stream(psiClass.getTypeParameters()).map(PsiNamedElement::getName).collect(Collectors.toList());
    }

    public static String toCamel(String str) {
        if (str == null) {
            return null;
        }
        String first = str.substring(0, 1).toLowerCase();
        return first + str.substring(1);
    }

    /**
     * 选择代码生成路径
     *
     * @param mapperEntry
     * @param project
     * @param psiElement
     * @return
     */
    public static VirtualFile findSourceRoot(MapperEntry mapperEntry, Project project, PsiElement psiElement) {

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

}
