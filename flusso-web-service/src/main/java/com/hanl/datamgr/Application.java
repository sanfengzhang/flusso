package com.hanl.datamgr;

import com.hanl.datamgr.repository.support.BaseRepositoryFactoryBean;
import com.hanl.datamgr.support.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author: Hanl
 * @date :2019/9/30
 * @desc:
 */
@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class)
public class Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(Application.class, args);
        SpringContextUtil.setApplicationContext(configurableApplicationContext);
    }
}
