package com.hanl.rule.goovy.core;

import com.google.common.collect.Maps;
import com.hanl.rule.goovy.GroovyEngineContext;
import com.hanl.rule.goovy.GroovyRule;
import com.hanl.rule.goovy.GroovyRuleEngine;
import groovy.lang.GroovyClassLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/11/7
 * @desc:
 */
@Slf4j
public class DefaultGroovyRuleEngine implements GroovyRuleEngine<GroovyRule> {

    private GroovyEngineContext context;

    private Map<String, GroovyRule> groovyRules = Maps.newConcurrentMap();

    public DefaultGroovyRuleEngine(GroovyEngineContext context) {

        this.context = context;
    }

    @Override
    public void initEngine() {
        List<String> scanPackage = this.context.scanPackage();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        String path = "classpath*:*.groovy_template";
        if (null != scanPackage || scanPackage.size() != 0) {

        }
        try {
            Resource[] resources = resolver.getResources(path);
            for (Resource resource : resources) {
                String fileName = resource.getFilename();
                InputStream input = resource.getInputStream();
                InputStreamReader reader = new InputStreamReader(input);
                BufferedReader br = new BufferedReader(reader);
                StringBuilder template = new StringBuilder();

                for (String line; (line = br.readLine()) != null; ) {
                    template.append(line).append("\n");
                }

                GroovyClassLoader classLoader = new GroovyClassLoader();
                Class<GroovyRule> aClass = classLoader.parseClass(template.toString());
                groovyRules.put(fileName, aClass.newInstance());
                log.info("load script fileName={}", fileName);
                input.close();
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public GroovyRule getGroovyRule(String id) {
        try {
            GroovyRule groovyRule = groovyRules.get(id);
            if (groovyRule == null) {
                throw new NullPointerException(String.format("script:%s not load", id));
            }
            return groovyRule;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() {

        groovyRules = null;
    }
}
