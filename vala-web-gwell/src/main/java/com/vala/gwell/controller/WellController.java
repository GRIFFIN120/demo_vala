package com.vala.gwell.controller;

import com.vala.framework.file.controller.FileBaseController;
import com.vala.gwell.entity.SliceEntity;
import com.vala.gwell.entity.WellEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/well")
public class WellController extends FileBaseController<WellEntity> {




}
