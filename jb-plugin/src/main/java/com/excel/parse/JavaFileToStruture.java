package com.excel.parse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.pojo.format.StringToUPPSPojoStruture;
import com.pojo.format.UPPSPojoStruture;

public class JavaFileToStruture {


    public static void reqFileList(String directoryPath, List<File> reqFileList, List<File> respFileList, List<File> beanFileList) {

        File file = new File(directoryPath);

        boolean isDirectory = file.isDirectory();
        if (isDirectory) {
            //是目录，递归调用
            String[] stringList = file.list();
//			System.out.println();

            for (String str : stringList) {
                reqFileList(directoryPath + File.separator + str, reqFileList, respFileList, beanFileList);
            }
        } else {
            //不是，则判断是否符合条件，符合则塞到fileList中。
            if (file.getName().contains("Req")) {
                reqFileList.add(file);
            } else if (file.getName().contains("Resp")) {
                respFileList.add(file);
            } else if (file.getName().endsWith(".java")) {
                beanFileList.add(file);
            }
        }


    }

    /**
     * 合并
     *
     * @param reqList
     * @param respList
     * @param beanList
     */
    public static void merge(List<UPPSPojoStruture> reqList, List<UPPSPojoStruture> respList, List<UPPSPojoStruture> beanList) {

        for (UPPSPojoStruture respPojoFields : respList) {

            String transCode = respPojoFields.getTransCode();
            UPPSPojoStruture reqPojo = getFromList(reqList, transCode);
            reqPojo.setRespPojoFieldList(respPojoFields.getRespPojoFieldList());
        }

        for (UPPSPojoStruture beanPojoFields : beanList) {
            reqList.add(beanPojoFields);
        }

//		UPPSPojoStruture reqPojo = getFromList(reqList,"BT0009");
//		System.out.println(reqPojo);


    }

    /**
     * @param list
     * @param transCode
     * @return
     */
    public static UPPSPojoStruture getFromList(List<UPPSPojoStruture> list, String transCode) {

        UPPSPojoStruture uppsPojo = null;
        for (UPPSPojoStruture pojo : list) {

            if (transCode.equals(pojo.getTransCode())) {
                uppsPojo = pojo;
            }
        }
        return uppsPojo;
    }

    /**
     * @param pojoPath
     * @return
     * @throws Exception
     */
    public static List<UPPSPojoStruture> createPojoList(String pojoPath) throws Exception {

        List<File> reqFileList = new ArrayList<File>();
        List<File> respFileList = new ArrayList<File>();
        List<File> beanFileList = new ArrayList<File>();

        JavaFileToStruture.reqFileList(pojoPath, reqFileList, respFileList, beanFileList);
        List<UPPSPojoStruture> reqList = new ArrayList<UPPSPojoStruture>();
        List<UPPSPojoStruture> respList = new ArrayList<UPPSPojoStruture>();
        List<UPPSPojoStruture> beanList = new ArrayList<UPPSPojoStruture>();

        for (File file : reqFileList) {
            UPPSPojoStruture pojoStruture = StringToUPPSPojoStruture.createUPPSPojoStrutureByFile(file);
            reqList.add(pojoStruture);
        }

        for (File file : respFileList) {
            UPPSPojoStruture pojoStruture = StringToUPPSPojoStruture.createUPPSPojoStrutureByFile(file);
            respList.add(pojoStruture);
        }

        for (File file : beanFileList) {
            UPPSPojoStruture pojoStruture = StringToUPPSPojoStruture.createUPPSPojoStrutureByFile(file);
            beanList.add(pojoStruture);
        }

        JavaFileToStruture.merge(reqList, respList, beanList);

        return reqList;
    }

}
