package com.vala.gwell.controller;

import com.vala.base.utils.ExcelUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.rosuda.REngine.Rserve.RConnection;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/t1")
    public String t1() throws Exception {

//        输入
        double[] input = new double[]{64.561,14.786,2.496,46.2428,39.827};
        File f = new File("/Users/liuyinpeng/Documents/项目/（已完成）/未命名文件夹/train.xlsx");




        FileInputStream fis = new FileInputStream(f);
        Workbook book = ExcelUtils.getWorkBook(fis, "xlsx");
        List<Object[]> read = ExcelUtils.read(book, 0);
        String[] titles = "AC,CNL,DEN,GR,RD,TYPE".split(",");


        RConnection re = new RConnection("120.27.8.186");

        for (int i = 0; i < titles.length; i++) {
            String title = titles[i];
            double[] data = this.getData(read, i);
            re.assign(title,data);
        }
        re.voidEval("library(MASS)");
        re.voidEval("data = data.frame(AC,CNL,DEN,GR,RD,TYPE)");
        re.voidEval("params <- lda(TYPE~AC+CNL+DEN+GR+RD, data=data)");

        for (int i = 0; i < titles.length-1; i++) {
            String title = titles[i];
            re.assign(title,new double[]{input[i]});
        }

        re.voidEval("data=data.frame(AC,CNL,DEN,GR,RD)");
        re.voidEval("ret = as.numeric(predict(params, data)$class)");

        double ret = re.eval("ret").asDouble();
        System.out.println(ret);


//        for (Object[] objects : read) {
//            String s = ArrayUtils.toString(objects);
//            System.out.println(s);
//        }

//
//    REXP x = re.eval("R.version.string");
//    System.out.println(x.asString());
//    double[] arr = re.eval("rnorm(20)").asDoubles();
//    for (double a : arr) {
//        System.out.print(a + ",");
//    }

        re.close();
        return "刘寅鹏";
    }


    private double[] getData(List<Object[]> list, int index){
        double[] data = new double[list.size()-1];
        for (int i = 1; i < list.size(); i++) {
            Object[] objects =  list.get(i);
            data[i-1] = (double) objects[index];
        }
        return data;
    }



}
