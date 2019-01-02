package com.cying.gapshelper.param.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * @author chengying13378 chengying13378@hundsun.com
 * @ClassName Parameter
 * @Description TODO
 * @date 2019-01-01 15:34
 */
public class Parameter {

    @XStreamAsAttribute
    @XStreamAlias("fileDes")
    private String fileDes ;

    @XStreamAsAttribute
    @XStreamAlias("id")
    private String id;

    @XStreamAsAttribute
    @XStreamAlias("key")
    private String key;

    @XStreamAsAttribute
    @XStreamAlias("value")
    private String value;

    @XStreamAsAttribute
    @XStreamAlias("title")
    private String title;

    @XStreamAsAttribute
    @XStreamAlias("effectiveAfterRestart")
    private String effectiveAfterRestart;

    @XStreamAlias("des")
    private String des;


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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEffectiveAfterRestart() {
        return effectiveAfterRestart;
    }

    public void setEffectiveAfterRestart(String effectiveAfterRestart) {
        this.effectiveAfterRestart = effectiveAfterRestart;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
