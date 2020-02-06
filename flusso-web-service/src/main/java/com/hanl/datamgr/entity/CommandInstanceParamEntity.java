package com.hanl.datamgr.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author: Hanl
 * @date :2019/10/12
 * @desc:
 */
@Entity
@Table(name = "tb_cmd_instance_param")
@Data
public class CommandInstanceParamEntity implements Serializable {

    @Id
    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    @Column(name = "param_value")
    private String paramValue;

    @ManyToOne
    @JoinColumn(name = "cmd_param_id")
    private CommandParamEntity commandParamEntity;

    @ManyToOne
    @JoinColumn(name = "cmd_instance_id")
    private CommandInstanceEntity commandInstanceEntity;


}
