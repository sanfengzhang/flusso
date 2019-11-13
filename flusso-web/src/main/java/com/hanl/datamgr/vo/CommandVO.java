package com.hanl.datamgr.vo;

import com.hanl.datamgr.entity.CommandEntity;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author: Hanl
 * @date :2019/9/29
 * @desc:
 */
@Data
@ToString
public class CommandVO extends BaseVO<CommandEntity> {

    private String id;

    private String commandName;

    private String commandClazz;//当前Command所属的原始java类名称

    private String commandType;//按Command功能分类型，解析类型、富化类型

    private Date createTime;//数据处理流程创建时间

    private Date updateTime;//数据流程更新时间

    private String morphName;

    @Override
    public CommandEntity to() {
        CommandEntity entity = new CommandEntity();
        entity.setCommandType(commandType);
        entity.setCommandClazz(commandClazz);
        entity.setCommandName(commandName);
        entity.setCreateTime(createTime);
        entity.setUpdateTime(updateTime);
        entity.setCommandMorphName(morphName);
        return entity;
    }

    @Override
    public void from(CommandEntity commandEntity) {
        this.setId(commandEntity.getId());
        this.setCommandName(commandEntity.getCommandName());
        this.setCommandClazz(commandEntity.getCommandClazz());
        this.setCommandType(commandEntity.getCommandType());
        this.setMorphName(commandEntity.getCommandMorphName());
        this.setCreateTime(commandEntity.getCreateTime());
        this.setUpdateTime(commandEntity.getUpdateTime());
    }
}
