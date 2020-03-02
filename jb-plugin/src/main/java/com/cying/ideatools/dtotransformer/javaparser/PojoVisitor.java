package com.cying.ideatools.dtotransformer.javaparser;

import com.cying.ideatools.dtotransformer.action.CreateMicroComponentAction;
import com.cying.ideatools.utils.BuppsUtil;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.pojo.format.UPPSPojoField;
import com.pojo.format.UPPSPojoStruture;
import org.apache.commons.collections.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

import static com.cying.ideatools.gaps.GapsCompProviderVisitor.DefJavaDoc;

/**
 * @author chengying13378 chengying13378@hundsun.com
 * @description 接口Bean生成
 * @date 2019-05-12 08:15
 */
public class PojoVisitor extends VoidVisitorAdapter<Void> {


    private Map<String, String> simple2AbsNameMapping = new HashMap<>();

    private Map<String, UPPSPojoField> beanMap;

    public PojoVisitor(Map<String, UPPSPojoField> beanMap) {
        this.beanMap = beanMap;
    }

    @Override
    public void visit(final ImportDeclaration n, final Void arg) {

        if (CollectionUtils.isEmpty(n.getChildNodes())) {
            return;
        }
        for (Node node : n.getChildNodes()) {
            if (node instanceof Name) {
                Name name = (Name) node;
                // TODO 处理通配的情况
                simple2AbsNameMapping.put(name.getIdentifier(), name.getQualifier().get().asString() + "." + name.getIdentifier());
            }
        }
        super.visit(n, arg);
    }

    @Override
    public void visit(MethodDeclaration md, Void arg) {

        UPPSPojoStruture structure = new UPPSPojoStruture();
        // 1. 接口方法
        String simpleDesc = md.getJavadoc().orElse(DefJavaDoc).getDescription().toText();
        structure.setTransName(simpleDesc);
        // 2. 解析请求报文
        NodeList<Parameter> parameters = md.getParameters();
        if (parameters == null || parameters.size() <= 0) {
            throw new RuntimeException(String.format("方法[%s]没有请求参数", md.getNameAsString()));
        }
        // 推测交易代码
        String transCode = null;
        if (parameters.size() == 1) {
            transCode = BuppsUtil.getTransCode(md.getNameAsString(), parameters.get(0).getTypeAsString());
        } else {
            transCode = md.getNameAsString();
        }


        structure.setTransCode(transCode);
        for (Parameter parameter : parameters) {
            // 请求参数的完全限定名
            String absParamType = simple2AbsNameMapping.getOrDefault(parameter.getType().asClassOrInterfaceType().getNameAsString(), "unknown");

        }
        // 3. 解析应答报文

        super.visit(md, arg);
    }
}
