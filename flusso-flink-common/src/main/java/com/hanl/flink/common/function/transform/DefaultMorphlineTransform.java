package com.hanl.flink.common.function.transform;

import java.util.*;

/**
 * @author: Hanl
 * @date :2019/8/30
 * @desc:
 */
public class DefaultMorphlineTransform extends MorphlineTransform<Map<String, Object>> {

    public DefaultMorphlineTransform(String transformContextName,String mainFlowName, List<Map<String, Object>> morphFlowConfigs) {

        super(transformContextName, mainFlowName, morphFlowConfigs);
    }


    @Override
    public Map<String, Object> output(Map<String, Collection<Object>> value) {
        Iterator<Map.Entry<String, Collection<Object>>> it = value.entrySet().iterator();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        while (it.hasNext()) {
            Map.Entry<String, Collection<Object>> en = it.next();

            if (en.getKey().equals("message")) {
                continue;
            }

            resultMap.put(en.getKey(), en.getValue().iterator().next());
        }
        return resultMap;
    }
}
