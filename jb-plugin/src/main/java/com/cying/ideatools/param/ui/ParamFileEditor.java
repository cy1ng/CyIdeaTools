package com.cying.ideatools.param.ui;

import com.cying.ideatools.param.data.Category;
import com.cying.ideatools.param.data.ParamFileBean;
import com.cying.ideatools.param.data.Parameter;
import com.cying.ideatools.param.data.RootCategory;
import com.intellij.codeHighlighting.BackgroundEditorHighlighter;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorLocation;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.UserDataHolderBase;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.tabs.impl.JBEditorTabs;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.beans.PropertyChangeListener;

/**
 * @author chengying13378 chengying13378@hundsun.com
 * @ClassName ParamFileEditor
 * @Description TODO
 * @date 2018-12-30 01:47
 */
public class ParamFileEditor extends UserDataHolderBase implements FileEditor {


    private final JComponent myComponent;

    public ParamFileEditor(@NotNull Project project, @NotNull VirtualFile file) {

        XStream xStream = new XStream(new DomDriver("UTF-8",new XmlFriendlyNameCoder("-_","_")));
        xStream.setClassLoader(ParamFileEditor.class.getClassLoader());
        xStream.processAnnotations(RootCategory.class);
        xStream.autodetectAnnotations(true);
        xStream.ignoreUnknownElements();
        ParamFileBean fileBean = new ParamFileBean();
        try{
            RootCategory root = (RootCategory)xStream.fromXML(file.getInputStream());
            fileBean.setRootCategory(root);
        }catch(Exception e){
            e.printStackTrace(); //TODO
            throw new RuntimeException("xml解析异常",e);
        }
        ParamFileEditorUi paramFileEditorUi = new ParamFileEditorUi();
        paramFileEditorUi.bindData(fileBean);
        myComponent = paramFileEditorUi.$$$getRootComponent$$$();
    }

    @NotNull
    @Override
    public JComponent getComponent() {
        return myComponent;
    }

    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        return myComponent;
    }

    @NotNull
    @Override
    public String getName() {
        return "配置查看器";
    }

    @Override
    public void setState(@NotNull FileEditorState state) {

    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public void selectNotify() {

    }

    @Override
    public void deselectNotify() {

    }

    @Override
    public void addPropertyChangeListener(@NotNull PropertyChangeListener listener) {

    }

    @Override
    public void removePropertyChangeListener(@NotNull PropertyChangeListener listener) {

    }

    @Nullable
    @Override
    public BackgroundEditorHighlighter getBackgroundHighlighter() {
        return null;
    }

    @Nullable
    @Override
    public FileEditorLocation getCurrentLocation() {
        return null;
    }

    @Override
    public void dispose() {

    }
}
