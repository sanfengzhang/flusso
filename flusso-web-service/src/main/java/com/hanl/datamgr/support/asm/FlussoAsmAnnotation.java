package com.hanl.datamgr.support.asm;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2020/1/16
 * @desc:
 */
public class FlussoAsmAnnotation {

    private String annotationClassName;

    private Map<String, Object> attrMap = new HashMap<>();


    public FlussoAsmAnnotation(String annotationClassName) {
        this.annotationClassName = annotationClassName;
    }

    public void addAttr(String key, Object value) {
        this.attrMap.put(key, value);
    }

    public Object getAttr(String key) {

        return attrMap.get(key);
    }

    public String getAnnotationClassName() {
        return annotationClassName;
    }

    public void setAnnotationClassName(String annotationClassName) {
        this.annotationClassName = annotationClassName;
    }

    public Map<String, Object> getAttrMap() {
        return attrMap;
    }

    public void setAttrMap(Map<String, Object> attrMap) {
        this.attrMap = attrMap;
    }

    @Override
    public String toString() {
        return "FlussoAsmAnnotation{" +
                "annotationClassName='" + annotationClassName + '\'' +
                ", attrMap=" + attrMap +
                '}';
    }
}
