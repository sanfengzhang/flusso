package org.kitesdk.morphline.han;

import org.kitesdk.morphline.api.Record;
import org.kitesdk.morphline.api.SkipCommandSelector;

/**
 * @author: Hanl
 * @date :2019/9/27
 * @desc:
 */
public class KeySkipCommandSelector implements SkipCommandSelector {

    @Override
    public boolean skip(Record record, String commandInstanceId) {
        if (null == record) {
            return true;
        }
        if (commandInstanceId.equals("313db3153f2347c782a720b9abd41b45")) {
            if (record.getFirstValue("name").toString().equals("zhangsan")) {
                return true;
            }
        }
        return false;
    }
}
