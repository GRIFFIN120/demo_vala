package com.vala.gwell.entity;

import com.vala.base.entity.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;

@Data
@Entity
@ToString(callSuper = true)
public class SliceTypeEntity extends BaseEntity {
}
