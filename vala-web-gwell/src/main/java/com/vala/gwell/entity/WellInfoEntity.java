package com.vala.gwell.entity;

import com.vala.framework.file.entity.ImageEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;

@Data
@Entity
@ToString(callSuper = true)
public class WellInfoEntity extends ImageEntity {
}
