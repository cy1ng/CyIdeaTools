package com.cying.ideatools.dtotransformer.javaparser;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.pojo.format.UPPSPojoField;
import net.sf.oval.constraint.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

import static com.cying.ideatools.gaps.GapsCompProviderVisitor.DefJavaDoc;

/**
 * @author chengying13378 chengying13378@hundsun.com
 * @description TODO
 * @date 2019-06-09 00:54
 */
public class ApiPojoFieldVisitor extends VoidVisitorAdapter<Void> {

    private Map<String/*bean名称*/, List<UPPSPojoField>> beanMap;

    public ApiPojoFieldVisitor(Map<String, List<UPPSPojoField>> beanMap) {
        this.beanMap = beanMap;
    }

    @Override
    public void visit(FieldDeclaration field, Void arg) {

        UPPSPojoField uppsPojoField = new UPPSPojoField();
        uppsPojoField.setDetailDesc("");
        uppsPojoField.setIsNotNull(Boolean.FALSE);
        String simpleDesc = field.getJavadoc().orElse(DefJavaDoc).getDescription().toText();
        // 需要解析的常用注解类
        // 设置最大长度
        field.getAnnotationByName(MaxLength.class.getSimpleName()).ifPresent(annotationExpr -> {
            uppsPojoField.setLength(getPairValueByName(annotationExpr, "value"));
            inferName(annotationExpr,uppsPojoField);
        });
        field.getAnnotationByName(Length.class.getSimpleName()).ifPresent(annotationExpr -> {
            uppsPojoField.setLength(getPairValueByName(annotationExpr, "max"));
            inferName(annotationExpr,uppsPojoField);
        });
        // 设置枚举类
        field.getAnnotationByName(CheckWith.class.getSimpleName()).ifPresent(annotationExpr -> {
            String className = getPairValueByName(annotationExpr, "value");
            if (className.endsWith(".Check.class")) {
                className = className.substring(0, className.length() - 12);
            } else if (className.endsWith(".class")) {
                className = className.substring(0, className.length() - 6);
            }
            uppsPojoField.setCheckClassName(className);
            inferName(annotationExpr,uppsPojoField);
        });
        // 是否非空
        field.getAnnotationByName(NotBlank.class.getSimpleName()).ifPresent(annotationExpr -> {
            uppsPojoField.setIsNotNull(Boolean.TRUE);
            inferName(annotationExpr,uppsPojoField);
        });
        field.getAnnotationByName(NotNull.class.getSimpleName()).ifPresent(annotationExpr -> {
            uppsPojoField.setIsNotNull(Boolean.TRUE);
            inferName(annotationExpr,uppsPojoField);
        });
        field.getAnnotationByName("NotNullBlank").ifPresent(annotationExpr -> {
            uppsPojoField.setIsNotNull(Boolean.TRUE);
            inferName(annotationExpr,uppsPojoField);
        });
        // 设置精度
        field.getAnnotationByName(Digits.class.getSimpleName()).ifPresent(annotationExpr -> {
            uppsPojoField.setLength(getPairValueByName(annotationExpr, "maxInteger"));
            uppsPojoField.setPrecision(getPairValueByName(annotationExpr, "maxFraction"));
            inferName(annotationExpr,uppsPojoField);
        });
        // 设置int类型的字段长度
        field.getAnnotationByName(Min.class.getSimpleName()).ifPresent(annotationExpr -> {
            String value = getPairValueByName(annotationExpr, "value");
            uppsPojoField.setDetailDesc(uppsPojoField.getDetailDesc() + "最小值为" + value + ";");
            inferName(annotationExpr,uppsPojoField);
        });
        field.getAnnotationByName(Max.class.getSimpleName()).ifPresent(annotationExpr -> {
            String value = getPairValueByName(annotationExpr, "value");
            uppsPojoField.setDetailDesc(uppsPojoField.getDetailDesc() + "最大值为" + value + ";");
            inferName(annotationExpr,uppsPojoField);
        });

        uppsPojoField.setName(field.getVariables() == null ? "" : field.getVariables().get(0).getNameAsString());
        String type = field.getVariables() == null ? "" : field.getVariables().get(0).getTypeAsString();
        // 如果不是原始类型或String,需要进一步解析
        if(!SIMPLE_TYPE.contains(type)){
            // TODO
        }
        uppsPojoField.setType(type);
        uppsPojoField.setDesc(simpleDesc);

        uppsPojoField.setModelName("");
        uppsPojoField.setUpperNode("");

        final String[] parentClassName = {""};
        field.getParentNode().ifPresent(node -> {
            parentClassName[0] = ((ClassOrInterfaceDeclaration) node).getNameAsString();
        });
        String className = parentClassName[0];
        uppsPojoField.setReq(className.startsWith("Req"));

        List<UPPSPojoField> fieldList = beanMap.getOrDefault(className, new ArrayList<>());
        fieldList.add(uppsPojoField);
        beanMap.put(className, fieldList);
        super.visit(field, arg);
    }

    public static String getPairValueByName(AnnotationExpr annotationExpr, String attrName) {

        if (annotationExpr == null) {
            return "";
        }
        NodeList<MemberValuePair> pairNodeList = ((NormalAnnotationExpr) annotationExpr).getPairs();
        for (MemberValuePair mv : pairNodeList) {
            if (mv.getNameAsString().equals(attrName)) {
                return mv.getValue().toString();
            }
        }
        return "";
    }

    /**
     * 从注解推断出中文名称
     * @param annotationExpr
     * @param uppsPojoField
     */
    public static void inferName(AnnotationExpr annotationExpr, UPPSPojoField uppsPojoField) {

        if(uppsPojoField == null || uppsPojoField.getName() != null){
            return ;
        }
        uppsPojoField.setName(getPairValueByName(annotationExpr, "message"));
    }

    public static final Set<String> SIMPLE_TYPE = new HashSet<>();

    static {
        SIMPLE_TYPE.add("String");
        SIMPLE_TYPE.add("Integer");
        SIMPLE_TYPE.add("Byte");
        SIMPLE_TYPE.add("Long");
        SIMPLE_TYPE.add("Double");
        SIMPLE_TYPE.add("Float");
        SIMPLE_TYPE.add("Character");
        SIMPLE_TYPE.add("Short");
        SIMPLE_TYPE.add("Boolean");
        SIMPLE_TYPE.add("int");
        SIMPLE_TYPE.add("byte");
        SIMPLE_TYPE.add("long");
        SIMPLE_TYPE.add("double");
        SIMPLE_TYPE.add("float");
        SIMPLE_TYPE.add("char");
        SIMPLE_TYPE.add("short");
        SIMPLE_TYPE.add("bool");
    }

    public static void main(String[] args) throws FileNotFoundException {

        FileInputStream in = new FileInputStream("/Users/chengying/work/git/hundsun/bupps/baseservice/bupps-batch/bupps-batch-interfaces/src/main/java/com/hundsun/bupps/batch/interfaces/dto/req/ReqBT0001.java");
        Map<String/*bean名称*/, List<UPPSPojoField>> beanMap = new HashMap<>();
        CompilationUnit compilationUnit = StaticJavaParser.parse(in);
        compilationUnit.accept(new ApiPojoFieldVisitor(beanMap), null);

    }
}
