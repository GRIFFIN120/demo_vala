package com.vala.gwell.entity;

import com.vala.framework.file.entity.FileEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;

@Data
@Entity
@ToString(callSuper = true)
public class TrainEntity extends FileEntity {
}
