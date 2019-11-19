package com.hanl.datamgr.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * @author: Hanl
 * @date :2019/10/25
 * @desc:
 */

@Entity
@DiscriminatorValue("cmd_param_config")
@Data
@EqualsAndHashCode(exclude = {"commandEntity"}, callSuper = true)
public class CommandParamEntity extends FieldEntity {

    @ManyToOne
    @JoinColumn(name = "cmd_id")
    @JsonIgnoreProperties({"cmdParams","commandInstanceEntityList"})
    private CommandEntity commandEntity;

}
