package com.hanl.datamgr.core.listener;

/**
 * @author: Hanl
 * @date :2019/10/24
 * @desc:
 */
public enum EventStatus {

    CommandChange("CommandChange"), CacheChange("CacheChange");

    private final String eventType;

    private EventStatus(String eventType) {

        this.eventType = eventType;
    }


    public String getEventType() {
        return eventType;
    }

}
