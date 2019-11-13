package com.hanl.datamgr.vo;

import com.hanl.datamgr.entity.FiledEntity;
import lombok.Data;
import lombok.ToString;

/**
 * @author: Hanl
 * @date :2019/9/29
 * @desc:
 */
@Data
@ToString
public class FiledTypeVO extends BaseVO<FiledEntity> {

    private String id;

    private String fieldName;

    private String fieldType;

    private String filedValue;

    private int fieldLength;

    private String analyzer;

    private String format;

    private String displayName;

    @Override
    public FiledEntity to() {
        return null;
    }

    @Override
    public void from(FiledEntity entity) {

        this.setFieldName(entity.getFieldName());
        this.setId(entity.getId());
        this.setFieldType(entity.getFieldType());
        this.setFiledValue(entity.getFieldValue());
        this.setDisplayName(entity.getCmdDisplayName());
    }
}
