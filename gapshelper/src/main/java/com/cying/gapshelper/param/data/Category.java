package com.cying.gapshelper.param.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * @author chengying13378 chengying13378@hundsun.com
 * @ClassName Category
 * @Description TODO
 * @date 2019-01-01 15:28
 */
public class Category {

    @XStreamAsAttribute
    @XStreamAlias("fileDes")
    private String fileDes ;

    @XStreamAsAttribute
    @XStreamAlias("id")
    private String id;

    @XStreamAsAttribute
    @XStreamAlias("name")
    private String name;


    @XStreamImplicit(itemFieldName = "Parameter")
    private List<Parameter> parameters;

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

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }
}
