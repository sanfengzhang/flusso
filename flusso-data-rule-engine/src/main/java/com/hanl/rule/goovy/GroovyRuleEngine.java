package com.hanl.rule.goovy;

/**
 * @author: Hanl
 * @date :2019/11/7
 * @desc:
 */
public interface GroovyRuleEngine<T> {

    public void initEngine();

    public T getGroovyRule(String id);

    public void destroy();
}
