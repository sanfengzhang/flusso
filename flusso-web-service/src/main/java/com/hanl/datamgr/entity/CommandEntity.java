package com.hanl.datamgr.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * @author: Hanl
 * @date :2019/9/29
 * @desc:
 */
@Data
@Entity
@Table(name = "command")
public class CommandEntity implements Serializable {

    @Id
    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    @Column(name = "cmd_name")
    private String commandName;

    @Column(name = "cmd_morph_name")
    private String commandMorphName;

    @Column(name = "cmd_clazz")
    private String commandClazz;

    @Column(name = "cmd_type")
    private String commandType;

    @Column(name = "cmd_provider")
    private String commandProvider;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "commandEntity", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JsonIgnoreProperties(value = {"commandEntity"})
    private Set<CommandParamEntity> cmdParams = new HashSet<>();


    @OneToMany(mappedBy = "command", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JsonIgnoreProperties(value = {"command"})
    private List<CommandInstanceEntity> commandInstanceEntityList = new ArrayList<>();

    @Column(name = "create_time")
    @CreationTimestamp
    private Date createTime;//数据处理流程创建时间

    @Column(name = "update_time")
    private Date updateTime;//数据流程更新时间

    @Override
    public String toString() {
        List<String> ids = new ArrayList<>();
        commandInstanceEntityList.forEach(data -> {
            ids.add(data.getId());
        });
        return "CommandEntity{" +
                "id='" + id + '\'' +
                ", commandName='" + commandName + '\'' +
                ", commandMorphName='" + commandMorphName + '\'' +
                ", commandClazz='" + commandClazz + '\'' +
                ", commandType='" + commandType + '\'' +
                ", commandProvider='" + commandProvider + '\'' +
                ", commandInstanceEntityList=" + ids +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
