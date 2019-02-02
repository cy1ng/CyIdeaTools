package com.cying.ideatools.param.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * @author chengying13378 chengying13378@hundsun.com
 * @ClassName ParamCategory
 * @Description TODO
 * @date 2019-01-01 15:22
 */
@XStreamAlias("Category")
public class RootCategory {

    @XStreamImplicit(itemFieldName = "Category")
    private List<Category> categories;

    @XStreamAsAttribute
    @XStreamAlias("fileDes")
    private String fileDes ;

    @XStreamAsAttribute
    @XStreamAlias("id")
    private String id;

    @XStreamAsAttribute
    @XStreamAlias("name")
    private String name;

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getFileDes() {
        return fileDes;
    }

    public void setFileDes(String fileDes) {
        this.fileDes = fileDes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
