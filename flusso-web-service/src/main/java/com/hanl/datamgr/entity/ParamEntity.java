package com.hanl.datamgr.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author: Hanl
 * @date :2019/10/12
 * @desc:
 */
@Data
@Entity
@Table(name = "tb_param")
public class ParamEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    @Column(name = "param_name")
    private String paramName;

    @Column(name = "param_key")
    private String paramKey;

    @Column(name = "param_value")
    private String paramValue;

    @Column(name = "param_value_type")
    private String paramValueType;

    @ManyToOne
    private CommandInstanceEntity commandInstanceEntity;

}
