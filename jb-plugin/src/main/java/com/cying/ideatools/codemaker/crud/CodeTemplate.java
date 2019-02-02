package com.cying.ideatools.codemaker.crud;

import com.intellij.openapi.util.text.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author hansong.xhs
 * @version $Id: CodeTemplate.java, v 0.1 2017-01-28 9:41 hansong.xhs Exp $$
 */
@Data
@AllArgsConstructor
public class CodeTemplate {

    public static final String DEFAULT_ENCODING = "UTF-8";

    public CodeTemplate() {}

    /**
     * template name
     */
    private String name;

    /**
     * the generated class name, support velocity
     */
    private String classNameVm;

    /**
     * code template in velocity
     */
    private String codeTemplate;

    /**
     * the encoding of the generated file
     */
    private String fileEncoding;

    public boolean isValid() {
        return StringUtil.isNotEmpty(getClassNameVm()) && StringUtil.isNotEmpty(getName())
                && StringUtil.isNotEmpty(getCodeTemplate()) && StringUtil.isNotEmpty(getFileEncoding());
    }

    public static final CodeTemplate EMPTY_TEMPLATE = new CodeTemplate("", "", "", DEFAULT_ENCODING);

}
