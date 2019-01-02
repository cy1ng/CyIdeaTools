package com.cying.gapshelper.param.ui;

import com.cying.gapshelper.param.ParamFileType;
import com.intellij.openapi.fileEditor.AsyncFileEditorProvider;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

/**
 * @author chengying13378 chengying13378@hundsun.com
 * @ClassName ParamFileEditorProvider
 * @Description 编辑器提供者
 * @date 2018-12-30 01:19
 */
public class ParamFileEditorProvider implements AsyncFileEditorProvider {

    @NotNull
    @Override
    public Builder createEditorAsync(@NotNull Project project, @NotNull VirtualFile file) {
        return null;
    }

    @Override
    public boolean accept(@NotNull Project project, @NotNull VirtualFile file) {
        return file.getFileType() == ParamFileType.INSTANCE;
    }

    @NotNull
    @Override
    public FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile file) {
//          return new ParamFileEditorUi();
        return new ParamFileEditor(project,file);
    }

    @NotNull
    @Override
    public String getEditorTypeId() {
        return "param-tab-editor";
    }

    @NotNull
    @Override
    public FileEditorPolicy getPolicy() {
        return FileEditorPolicy.PLACE_BEFORE_DEFAULT_EDITOR;
    }
}
