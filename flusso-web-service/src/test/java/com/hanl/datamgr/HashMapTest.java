package com.hanl.datamgr;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

import java.util.Date;

/**
 * @author: Hanl
 * @date :2019/12/31
 * @desc:
 */
public class HashMapTest {

    @Test
    public void test1() throws Exception {

        /// e.hash & (newCap - 1)
        System.out.println(98 & 15);
        System.out.println(98 & 31);
        System.out.println(96 & 7);

        String timeStr = "2019-10-12 01:09:08.919999";
        Date date = DateUtils.parseDate(timeStr, new String[]{"yyyy-MM-dd HH:mm:ss.SSSSSS"});
        System.out.println(date.getTime());

    }
}
