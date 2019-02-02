package com.cying.ideatools.codemaker.crud;

import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;

/**
 * @author chengying13378 chengying13378@hundsun.com
 * @ClassName CrudCodeMaker
 * @Description TODO
 * @date 2019-01-26 21:58
 */
public class CrudCodeMakerApp implements ApplicationComponent {

    public CrudCodeMakerApp() {
    }

    @Override
    public void initComponent() {
    }

    @Override
    public void disposeComponent() {
    }

    @Override
    @NotNull
    public String getComponentName() {
        return "com.cying.codemaker.crud.CodeMaker";
    }

}
