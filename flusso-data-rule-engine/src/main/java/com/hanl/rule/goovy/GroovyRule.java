package com.hanl.rule.goovy;

/**
 * @author: Hanl
 * @date :2019/11/7
 * @desc:
 */
public interface GroovyRule<T> {

    public void run(T data, GroovyEngineContext context) throws GroovyException;

}
