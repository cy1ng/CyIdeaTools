package com.cying.ideatools.param.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author chengying13378 chengying13378@hundsun.com
 * @ClassName ParamFileBean
 * @Description 参数文件bean
 * @date 2019-01-01 15:21
 */
public class ParamFileBean {


    @XStreamAlias("Category")
    private RootCategory rootCategory;

    public RootCategory getRootCategory() {
        return rootCategory;
    }

    public void setRootCategory(RootCategory rootCategory) {
        this.rootCategory = rootCategory;
    }
}
