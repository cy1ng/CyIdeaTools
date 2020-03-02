package com.cying.ideatools.utils;

import java.util.regex.Matcher;

import static com.cying.ideatools.gaps.GapsCompProviderVisitor.ParamWithCodePattern;

/**
 * @author chengying13378 chengying13378@hundsun.com
 * @description bupps
 * @date 2019-05-17 15:40
 */
public class BuppsUtil {


    /**
     *  通过请求类型解析交易码
     * @param methodName
     * @param reqTypeSimpleName
     * @return
     */
    public static String getTransCode(String methodName,String reqTypeSimpleName){

        String transCodeOrMethod = methodName;
        Matcher matcher = ParamWithCodePattern.matcher(reqTypeSimpleName);
        if (matcher.find()) {
            transCodeOrMethod = matcher.group(1);
        }
        return transCodeOrMethod;
    }
}
