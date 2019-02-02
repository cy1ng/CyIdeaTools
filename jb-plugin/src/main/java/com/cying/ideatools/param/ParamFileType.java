package com.cying.ideatools.param;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;

/**
 * @author chengying13378 chengying13378@hundsun.com
 * @ClassName ParamFileType
 * @Description 参数配置文件
 * @date 2018-12-30 01:32
 */
public class ParamFileType extends LanguageFileType {

    public static final ParamFileType INSTANCE = new ParamFileType();

    public ParamFileType(){
        super(ParamLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "parameter";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "local config file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "parameter";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return IconLoader.getIcon("/cyingtools.png");
    }
}
