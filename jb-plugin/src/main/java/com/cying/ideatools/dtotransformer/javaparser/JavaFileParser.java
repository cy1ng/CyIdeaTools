package com.cying.ideatools.dtotransformer.javaparser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.pojo.format.UPPSPojoStruture;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chengying13378 chengying13378@hundsun.com
 * @description TODO
 * @date 2019-05-08 11:20
 */
public class JavaFileParser {

    public static List<UPPSPojoStruture> createPojoList(String pojoBasePath) throws Exception {

        List<UPPSPojoStruture> uppsPojoStrutureList = new ArrayList<>();
        String pojoPath = "";
        CompilationUnit parse = StaticJavaParser.parse(pojoPath);
        List<FieldDeclaration> fieldDeclarationList = parse.findAll(FieldDeclaration.class);
        return uppsPojoStrutureList;

    }




    public static void main(String[] args){

    }
}