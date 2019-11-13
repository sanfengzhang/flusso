package org.kitesdk.morphline.api;

/**
 * @author: Hanl
 * @date :2019/9/27
 * @desc:
 */
public interface SkipCommandSelector {

    public boolean skip(Record record, String commandInstanceId);
}
