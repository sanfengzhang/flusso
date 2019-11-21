package com.hanl.datamgr.support;

import com.fasterxml.jackson.databind.ser.BeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;

/**
 * @author: Hanl
 * @date :2019/10/19
 * @desc:
 */
public class EntityFilterProvider extends FilterProvider {

    @Override
    public BeanPropertyFilter findFilter(Object o) {
        throw new UnsupportedOperationException("Access to deprecated filters not supported");
    }

    @Override
    public PropertyFilter findPropertyFilter(Object filterId, Object valueToFilter) {


        return super.findPropertyFilter(filterId, valueToFilter);
    }
}
