package com.le.jr.am.profit.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zefer.pd4ml.PD4Constants;
import org.zefer.pd4ml.PD4ML;

import java.awt.*;
import java.io.*;

/**
 * Created by dujianjun on 2017/3/1.
 */
public class PdfUtil {
    Logger logger = LoggerFactory.getLogger(PdfUtil.class);



    // HTML代码来自于HTML文件
    public static void generatePDF_2(File outputPDFFile, String inputHTMLFileName) throws Exception {
        FileOutputStream fos = new FileOutputStream(outputPDFFile);

        File file = new File(inputHTMLFileName);
        FileInputStream fileIn = new FileInputStream(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len=0;
        while((len=fileIn.read(buf))!=-1){
            baos.write(buf,0,len);
        }
        byte[] data = baos.toByteArray();
        String strFile= new String(data,"UTF-8");
        fileIn.close();
        baos.close();

        StringReader strReader = new StringReader(strFile);
        PD4ML pd4ml = new PD4ML();
        pd4ml.setPageInsets(new Insets(5, 20, 20, 20));
        pd4ml.setHtmlWidth(1000);
        pd4ml.setPageSize(pd4ml.changePageOrientation(PD4Constants.A4));
        pd4ml.useTTF("C:/Users/dujianjun/Desktop/fonts", true);
        pd4ml.setDefaultTTFs("KaiTi", "KaiTi", "KaiTi");
        pd4ml.enableTableBreaks(false);
        pd4ml.enableImgSplit(false);
        pd4ml.enableDebugInfo();
        pd4ml.render(strReader, fos);
    }

    // HTML代码来自于读取好的String
    public static void generatePdfFromStr(File outputPDFFile, String strFile,String fontPath) throws Exception {
        FileOutputStream fos = new FileOutputStream(outputPDFFile);
        StringReader strReader = new StringReader(strFile);
        PD4ML pd4ml = new PD4ML();
        pd4ml.setPageInsets(new Insets(5, 20, 20, 20));
        pd4ml.setHtmlWidth(1000);
        pd4ml.setPageSize(pd4ml.changePageOrientation(PD4Constants.A4));
        pd4ml.useTTF(fontPath, true);
        pd4ml.setDefaultTTFs("KaiTi", "KaiTi", "KaiTi");
        pd4ml.enableTableBreaks(false);
        pd4ml.enableImgSplit(false);
        pd4ml.enableDebugInfo();
        pd4ml.render(strReader, fos);
    }




    public static void main(String[] args) throws Exception{
        generatePDF_2(new File("D:/a.pdf"), "D:/1702230043112096133031_investing.html");
    }
}
