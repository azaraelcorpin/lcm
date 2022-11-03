package com.ems.lcm.config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@PropertySource({ "classpath:application.properties" })
@EnableAutoConfiguration
@EnableJpaRepositories(
    basePackages = "com.ems.lcm.DataSource_LCM", 
    entityManagerFactoryRef = "LcmEntityManager", 
    transactionManagerRef = "LcmTransactionManager"
)
public class DataSource_LcmConfig {
    @Autowired
    private Environment env;

    public DataSource_LcmConfig() {
        super();
    }

    @Bean(name = "LcmEntityManager")
    public LocalContainerEntityManagerFactoryBean LcmEntityManager() {
        final LocalContainerEntityManagerFactoryBean EntityManager = new LocalContainerEntityManagerFactoryBean();
        EntityManager.setDataSource(LcmDataSource());
        EntityManager.setPackagesToScan("com.ems.lcm.DataSource_LCM.model");

        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        EntityManager.setJpaVendorAdapter(vendorAdapter);
        final HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("lcm.jpa.hibernate.ddl-auto"));
        properties.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
        properties.put("hibernate.show_sql", env.getProperty("spring.jpa.show-sql"));
        properties.put("hibernate.format_sql", env.getProperty("spring.jpa.properties.hibernate.format_sql"));
        EntityManager.setJpaPropertyMap(properties);

        return EntityManager;
    }

    @Bean
    public DataSource LcmDataSource() {
 
        DriverManagerDataSource dataSource
          = new DriverManagerDataSource();
        dataSource.setDriverClassName(
          env.getProperty("lcm.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("lcm.datasource.url"));
        dataSource.setUsername(env.getProperty("lcm.datasource.username"));
        dataSource.setPassword(env.getProperty("lcm.datasource.password"));
 
        return dataSource;
    }

    @Bean(name = "LcmTransactionManager")
    public PlatformTransactionManager LcmTransactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(LcmEntityManager().getObject());
        return transactionManager;
    }
}
