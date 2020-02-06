package com.hanl.datamgr.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: Hanl
 * @date :2019/10/25
 * @desc:
 */

@Entity
@Table(name = "tb_cmd_param")
@Data
@EqualsAndHashCode(exclude = {"commandEntity"})
public class CommandParamEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    @Column(name = "param_name")
    private String paramName;

    @Column(name = "param_type")
    private String paramType;

    @Column(name = "param_format")
    private String paramFormat;//,columnDefinition = "varchar(32) COMMENT '数据格式化,yyyy-MM-dd'"

    @Column(name = "cmd_param_alias")
    private String cmdParamAlias;

    @Column(name = "create_time")
    @CreationTimestamp
    private Date createTime;//任务创建时间

    @Column(name = "update_time")
    @LastModifiedDate
    private Date updateTime;//任务更新时间

    @ManyToOne
    @JoinColumn(name = "cmd_id")
    @JsonIgnoreProperties({"cmdParams", "commandInstanceEntityList"})
    private CommandEntity commandEntity;


}
