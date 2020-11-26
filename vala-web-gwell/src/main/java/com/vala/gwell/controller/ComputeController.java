package com.vala.gwell.controller;

import com.vala.base.controller.BaseController;
import com.vala.base.utils.ExcelUtils;
import com.vala.commons.bean.ResponseResult;
import com.vala.framework.file.controller.FileBaseController;
import com.vala.gwell.entity.ComputeEntity;
import com.vala.gwell.entity.TrainEntity;
import com.vala.gwell.entity.WellEntity;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;

@RestController
@RequestMapping("/compute")
public class ComputeController extends BaseController<ComputeEntity> {

    @RequestMapping("/compute/{id}")
    public ResponseResult compute(@PathVariable Integer id) throws Exception {
        ComputeEntity computeEntity = this.baseService.get(id);
        TrainEntity trainEntity = this.baseService.get(TrainEntity.class, computeEntity.train);
        byte[] bytes = this.fastDfsService.read(trainEntity.url);
        InputStream fis = new ByteArrayInputStream(bytes);

        Workbook book = ExcelUtils.getWorkBook(fis, "xlsx");
        List<Object[]> read = ExcelUtils.read(book, 0);

        Integer ret = this.doit(read, computeEntity);
        System.out.println(ret);
        if(ret!=null){
            computeEntity.setAttr(ret.toString());
            this.baseService.saveOrUpdate(computeEntity);

            return new ResponseResult(200,"识别成功",ret);
        }else {
            return new ResponseResult(500,"识别不成功");
        }




    }
    private double[] getData(List<Object[]> list, String title){
        Object[] titles = list.get(0);
        int index = -1;
        for (int i = 0; i < titles.length; i++) {
            String temp = (String) titles[i];
            if(title.equalsIgnoreCase(temp)){
                index = i;
                break;
            }
        }
        double[] data = new double[list.size()-1];
        for (int i = 1; i < list.size(); i++) {
            Object[] objects =  list.get(i);
            data[i-1] = (double) objects[index];
        }
        return data;
    }

    private Integer doit(List<Object[]> read, ComputeEntity bean){
        String[] titles = "AC,CNL,DEN,GR,RD,TYPE".split(",");

        RConnection re = null;
        try {
            re = new RConnection("120.27.8.186");
            for (int i = 0; i < titles.length; i++) {
                String title = titles[i];
                double[] data = this.getData(read, title);
                re.assign(title,data);
            }
            re.voidEval("library(MASS)");
            re.voidEval("data = data.frame(AC,CNL,DEN,GR,RD,TYPE)");
            re.voidEval("params <- lda(TYPE~AC+CNL+DEN+GR+RD, data=data)");

            for (int i = 0; i < titles.length-1; i++) {
                String title = titles[i];
                Field  field = this.domain.getField(title);
                field.setAccessible(true);
                Double value = (Double) field.get(bean);
                if(value==null){return null;}
                re.assign(title,new double[]{value});
            }

            re.voidEval("data=data.frame(AC,CNL,DEN,GR,RD)");
            re.voidEval("ret = as.numeric(predict(params, data)$class)");


            Integer ret = re.eval("ret").asInteger();
            return ret;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            re.close();
        }

        return null;
    }

}
