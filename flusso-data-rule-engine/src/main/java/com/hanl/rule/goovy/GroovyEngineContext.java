package com.hanl.rule.goovy;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/11/7
 * @desc:
 */
@Data
public class GroovyEngineContext {

    private Map<String, Object> contextData;

    public static final String SCAN_GROOVY_PACKAGE = "scan.groovy.package";

    public List<String> scanPackage() {

        return (List<String>) contextData.get(SCAN_GROOVY_PACKAGE);
    }

}
