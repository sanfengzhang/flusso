package com.hanl.datamgr.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * @author: Hanl
 * @date :2019/9/30
 * @desc:
 */
@Data
@Entity
@Table(name = "command_instance")
public class CommandInstanceEntity implements Serializable {
    @Id
    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    @Column(name = "cmd_instance_name")
    @JsonProperty("name")
    private String commandInstanceName;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "commandInstanceEntity", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JsonIgnoreProperties(value = {"commandInstanceEntity"})
    private Set<CommandInstanceParamEntity> cmdInstanceParams = new HashSet<>();


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "commandInstanceEntity", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JsonIgnoreProperties(value = {"commandInstanceEntity"})
    private Set<CommandInstanceFlowRelation> cmdInstanceFlowRelSet = new HashSet<>();//该command实例节点包含的子流程关系

    @Column(name = "cmd_input")
    private String commandInputParams;//cmd的的输入参数，Record中的参数

    @Column(name = "cmd_output")//cmd的输出参数
    private String commandOutputParams;

    //是否跳过当前算子的操作的选择器，之所以不配在command上面是想更灵活的配置,因为同一个command只针对其实例去配置选择条件
    //挂在command下会对该算子下所有的实例生效不太符合实际应用场景，下面selectSubFlowClazz同理
    @Column(name = "skip_cmd_selector")
    private String skipCmdSelectorClazz;

    @Column(name = "skip_cmd_condition")//选择器的逻辑表达式
    private String skipCmdCondition;


    @ManyToOne
    @JoinColumn(name = "cmd_id")
    @JsonIgnoreProperties(value = {"commandInstanceEntityList"})
    private CommandEntity command;//属于哪个Command的实例对象

    @Column(name = "version")//同一个业务下面的同一个算子，可能在不同的时候使用版本不一样
    private int version;

    @Column(name = "ico")
    private String ico;

    @Column(name = "create_time")
    @CreationTimestamp
    private Date createTime;

}
