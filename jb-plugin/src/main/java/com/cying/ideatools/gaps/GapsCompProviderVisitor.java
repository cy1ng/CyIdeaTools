package com.cying.ideatools.gaps;

import com.cying.ideatools.codemaker.crud.utils.VelocityUtil;
import com.cying.ideatools.dtotransformer.action.CreateMicroComponentAction;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.javadoc.Javadoc;
import com.github.javaparser.javadoc.description.JavadocDescription;
import com.github.javaparser.javadoc.description.JavadocSnippet;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.Messages;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chengying13378 chengying13378@hundsun.com
 * @description gaps组件自动生成
 * @date 2019-05-10 16:56
 */
public class GapsCompProviderVisitor extends VoidVisitorAdapter<Void> {

    public static JavadocComment DefJavadocComment = new JavadocComment("待补充");

    public static Javadoc DefJavaDoc = new Javadoc(new JavadocDescription(Arrays.asList(new JavadocSnippet("待补充"))));

    public static Pattern ParamWithCodePattern = Pattern.compile("Request<Req(.*)>");

    private static String outputBasePath = System.getProperty("user.home") + File.separator + "GapsGen";

    private String moduleName;

    private String packageName;

    private Map<String, String> simple2AbsNameMapping = new HashMap<>();

    public GapsCompProviderVisitor(String moduleName) {
        this.moduleName = moduleName;
    }

    @Override
    public void visit(final ImportDeclaration n, final Void arg) {

        if (CollectionUtils.isEmpty(n.getChildNodes())) {
            return;
        }
        for (Node node : n.getChildNodes()) {
            if (node instanceof Name) {
                Name name = (Name) node;
                simple2AbsNameMapping.put(name.getIdentifier(), name.getQualifier().get().asString() + "." + name.getIdentifier());
            }
        }
        super.visit(n, arg);
    }

    @Override
    public void visit(PackageDeclaration n, Void arg) {
        this.packageName = n.getNameAsString();
        String tempStr = packageName.replace("com.hundsun.bupps.", "")
                .replace("com.hundsun.iibs.", "");
        int dotIndex = tempStr.indexOf(".");
        this.moduleName = tempStr.substring(0, dotIndex);
        super.visit(n, arg);
    }

    @Override
    public void visit(MethodDeclaration md, Void arg) {

        System.out.println("方法:" + md.getNameAsString());
        // 1. 生成组件
        // 组装参数map
        Map<String, Object> vmCompParam = new HashMap<>();
        // 文件描述/组件描述(这里与方法描述相同)
        String compFileDes = StringEscapeUtils.escapeXml11(md.getJavadoc().orElse(DefJavaDoc).getDescription().toText());
        vmCompParam.put("fileDes", compFileDes);
        // 方法描述
        String desc = StringEscapeUtils.escapeXml11(md.getJavadoc().orElse(DefJavaDoc).toText());
        vmCompParam.put("description", desc);
        // 组件ID
        String compId = UUID.randomUUID().toString().replaceAll("-", "");
        vmCompParam.put("componentUnqId", compId);
        List<CreateMicroComponentAction.ReqInfo> reqInfoList = new ArrayList<>();
        NodeList<Parameter> parameters = md.getParameters();
        if (parameters.size() > 0) {
            for (Parameter parameter : parameters) {
                String absParamType = simple2AbsNameMapping.getOrDefault(parameter.getType().asClassOrInterfaceType().getNameAsString(), "unknown");
                reqInfoList.add(new CreateMicroComponentAction.ReqInfo(parameter.getNameAsString(), absParamType));
            }
        }
        vmCompParam.put("reqParamList", reqInfoList);

        // 响应类名
        String absRespType = simple2AbsNameMapping.getOrDefault(md.getType().asClassOrInterfaceType().getNameAsString(), "unknown");
        vmCompParam.put("respClassName", absRespType);
        // 组件方法名
        vmCompParam.put("compMethodName", md.getNameAsString());
        // 组件类名
        String simpleName = ((ClassOrInterfaceDeclaration) md.getParentNode().get()).getNameAsString();
        vmCompParam.put("compClassName", packageName + "." + simpleName);
        try (InputStream in = getClass().getResourceAsStream("/template/microcomp/GapsFlowComp.vm")) {
            String compTemplateContent = IOUtils.toString(in, "utf-8");
            String compContent = VelocityUtil.evaluate(compTemplateContent, vmCompParam);
            // 组件写入文件
            File basePath = new File(outputBasePath);
            basePath.mkdirs();
            FileWriter fw = new FileWriter(new File(basePath, moduleName + "_" + md.getNameAsString() + ".gapsfc"), false);
            fw.write(compContent);
            fw.flush();
        } catch (Throwable e) {
            e.printStackTrace();
            ApplicationManager.getApplication().invokeLater(() -> Messages.showMessageDialog(e.getMessage(), "Generate Failed", null));
            return;
        }

        // 2. 生成流程
        Map<String, Object> vmFlowParam = new HashMap<>();
        String transCodeOrMethod = md.getNameAsString();
        if (parameters.size() == 1) {
            String typeName = parameters.get(0).getType().asString();
            Matcher matcher = ParamWithCodePattern.matcher(typeName);
            boolean hasCode = matcher.find();
            if (hasCode) {
                transCodeOrMethod = matcher.group(1);
            }
        }
        String flowId = "flow_" + moduleName + "_" + transCodeOrMethod;
        vmFlowParam.put("flowId", flowId);
        vmFlowParam.put("flowName", compFileDes);
        vmFlowParam.put("groupId", moduleName + "-impl");
        vmFlowParam.put("groupDes", "实现");
        vmFlowParam.put("microCompDes", compFileDes);
        vmFlowParam.put("compUnqId", compId);
        vmFlowParam.put("compDescription", compFileDes);
        // 单一微服务组件的请求参数
        vmFlowParam.put("reqParamList", reqInfoList);

        // 流程写入文件
        try (InputStream in = getClass().getResourceAsStream("/template/microcomp/GapsFlow.vm")) {
            String flowTemplateContent = IOUtils.toString(in, "utf-8");
            String flowContent = VelocityUtil.evaluate(flowTemplateContent, vmFlowParam);
            // 将流程写入文件
            File basePath = new File(outputBasePath);
            basePath.mkdirs();
            FileWriter fw = new FileWriter(new File(basePath, flowId + ".gapsflow"), false);
            fw.write(flowContent);
            fw.flush();
            // 记录映射关系文件
            FileWriter mpFileWriter = new FileWriter(new File(basePath, "mp.txt"), true);
            mpFileWriter.write(transCodeOrMethod + "    " + md.getNameAsString() + "    " + compId + "\n");
            mpFileWriter.flush();
        } catch (Throwable e) {
            e.printStackTrace();
            ApplicationManager.getApplication().invokeLater(() -> Messages.showMessageDialog(e.getMessage(), "Generate Failed", null));
            return;
        }

        super.visit(md, arg);
    }


    public static void main(String[] args){
        System.out.println(StringEscapeUtils.escapeXml11("<"));
        System.out.println("-----------------");
        System.out.println(StringEscapeUtils.escapeXml10("获取核心流水列表\n  " +
                "\n" +
                "@param request\n" +
                "@return\n"));

    }
}
