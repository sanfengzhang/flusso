package com.hanl.datamgr.repository.support;


import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.support.RepositoryProxyPostProcessor;

import java.lang.annotation.Annotation;

/**
 * @author: Hanl
 * @date :2019/10/14
 * @desc:
 */
public class EntityToVoPostProcessor implements RepositoryProxyPostProcessor {

    @Override
    public void postProcess(ProxyFactory proxyFactory, RepositoryInformation repositoryInformation) {


    }

    static class EntityToVoAdvice implements MethodInterceptor {

        RepositoryInformation repositoryInformation;

        EntityToVoAdvice(RepositoryInformation repositoryInformation) {
            this.repositoryInformation = repositoryInformation;
        }

        @Override
        public Object invoke(MethodInvocation methodInvocation) throws Throwable {


            return null;
        }
    }
}



