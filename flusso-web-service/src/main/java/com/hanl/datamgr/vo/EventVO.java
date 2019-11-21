package com.hanl.datamgr.vo;

import com.hanl.datamgr.core.listener.EventStatus;
import com.hanl.datamgr.entity.EventEntity;
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
public class  EventVO extends BaseVO<EventEntity> {

    private String id;

    private String eventName;

    private EventStatus eventType;

    private Object input;

    private Object output;

    private String eventMark;

    private long tookTime;//事件耗时

    private Long eventStartTime;//事件开始时间

    private Date createTime;

    @Override
    public EventEntity to() {
        return null;
    }

    @Override
    public void from(EventEntity userEventEntity) {

    }
}
