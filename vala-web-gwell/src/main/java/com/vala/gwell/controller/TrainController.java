package com.vala.gwell.controller;

import com.vala.commons.bean.ResponseResult;
import com.vala.framework.file.controller.FileBaseController;
import com.vala.gwell.entity.TrainEntity;
import com.vala.gwell.entity.WellEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/train")
public class TrainController extends FileBaseController<TrainEntity> {


}
