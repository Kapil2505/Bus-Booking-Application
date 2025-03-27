package com.RedBus.GenericConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class GenericRepositoryCreator {

    private final DefaultListableBeanFactory beanFactory;
    private final ApplicationContext applicationContext;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public GenericRepositoryCreator(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.beanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
    }


    public <T> T getGenericRepository(String repoName, Class<T> repositoryInterface) {
        if (!beanFactory.containsBean(repoName)) {
            createRepositoryBean(repoName, repositoryInterface);
        }
        return applicationContext.getBean(repoName, repositoryInterface);
    }

    private <T> void createRepositoryBean(String beanName, Class<T> repositoryInterface) {
        if (!beanFactory.containsBean(beanName)) {
            if (entityManager == null) {
                throw new IllegalStateException("EntityManager is not initialized");
            }
            JpaRepositoryFactory factory = new JpaRepositoryFactory(entityManager);
            T repository = factory.getRepository(repositoryInterface);
            beanFactory.registerSingleton(beanName, repository);
        }
    }
}
