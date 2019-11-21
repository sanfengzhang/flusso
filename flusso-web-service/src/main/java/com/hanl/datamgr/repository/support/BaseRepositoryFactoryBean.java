package com.hanl.datamgr.repository.support;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.core.support.RepositoryProxyPostProcessor;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * @author: Hanl
 * @date :2019/10/14
 * @desc:
 */
public class BaseRepositoryFactoryBean<R extends JpaRepository<T, I>, T, I extends Serializable> extends JpaRepositoryFactoryBean<R, T, I> {

    public BaseRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
        super(repositoryInterface);

    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {

        return new BaseRepositoryFactory<>(entityManager);
    }

    private static class BaseRepositoryFactory<T, I extends Serializable> extends JpaRepositoryFactory {

        private final EntityManager entityManager;

        public BaseRepositoryFactory(EntityManager entityManager) {
            super(entityManager);
            this.entityManager = entityManager;

        }

        @Override
        protected JpaRepositoryImplementation<?, ?> getTargetRepository(RepositoryInformation information, EntityManager entityManager) {

            return new DefaultBaseRepository<>((Class<T>) information.getDomainType(), entityManager);
        }

        @Override
        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {

            return DefaultBaseRepository.class;
        }

        @Override
        public void addRepositoryProxyPostProcessor(RepositoryProxyPostProcessor processor) {
            super.addRepositoryProxyPostProcessor(processor);
        }
    }
}
