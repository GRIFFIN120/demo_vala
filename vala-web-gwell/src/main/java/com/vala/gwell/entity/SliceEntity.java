package com.vala.gwell.entity;

import com.vala.framework.file.entity.ImageEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;

@Data
@Entity
@ToString(callSuper = true)
public class SliceEntity extends ImageEntity {
    public String sn;
    public Double depth0;
    public Double depth1;

    public Integer type;
    public Integer attr;
    public Integer well;
}
