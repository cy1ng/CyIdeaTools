package com.cying.ideatools.dtotransformer.javaparser;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.pojo.format.UPPSPojoField;
import com.pojo.format.UPPSPojoStruture;

import java.util.List;
import java.util.Map;

import static com.cying.ideatools.gaps.GapsCompProviderVisitor.DefJavaDoc;

/**
 * @author chengying13378 chengying13378@hundsun.com
 * @description 解析接口
 * @date 2019-06-08 23:31
 */
public class ApiInterfaceVisitor extends VoidVisitorAdapter<Void> {


    private List<UPPSPojoStruture> apiList;

    private Map<String/*bean名称*/, List<UPPSPojoField>> beanMap;

    public ApiInterfaceVisitor(List<UPPSPojoStruture> apiList, Map<String, List<UPPSPojoField>> beanMap) {
        this.apiList = apiList;
        this.beanMap = beanMap;
    }

    @Override
    public void visit(MethodDeclaration md, Void arg) {

        UPPSPojoStruture interfaceStruc = new UPPSPojoStruture();
        String simpleDesc = md.getJavadoc().orElse(DefJavaDoc).getDescription().toText();
        interfaceStruc.setTransName(simpleDesc);
        interfaceStruc.setTransCode("");
        interfaceStruc.setModelName("模块名"); // TODO
        interfaceStruc.setReqPojoFieldList(null);
        interfaceStruc.setRespPojoFieldList(null);
    }
}
