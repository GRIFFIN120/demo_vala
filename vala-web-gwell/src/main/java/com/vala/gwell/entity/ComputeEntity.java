package com.vala.gwell.entity;

import com.vala.base.entity.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;

@Data
@Entity
@ToString(callSuper = true)
public class ComputeEntity extends BaseEntity {
    public Double AC;
    public Double CNL;
    public Double DEN;
    public Double GR;
    public Double RD;

    public Integer train;

    public String attr;

}
