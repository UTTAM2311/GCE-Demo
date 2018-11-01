package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.demo.database.demo.repository" ,entityManagerFactoryRef = "demoEntityManagerFactory",
        transactionManagerRef = "demoTransactionManager")
public class JpaConfig {
    @Autowired
    private JpaProperties jpaProperties;

    @Autowired
    private DataSourceProperties dsProperties;

    private JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean demoEntityManagerFactory() {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setPackagesToScan("com.example.demo");
        em.setJpaVendorAdapter(this.jpaVendorAdapter());
        em.setDataSource(dataSource());

        Map<String, Object> jpaPropertiesMap = new HashMap<>(jpaProperties.getProperties());
        em.setJpaPropertyMap(jpaPropertiesMap);

        return em;
    }

    /**
     * Creates the default data source for the application
     */
    @Bean
    public DataSource dataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create(this.getClass().getClassLoader()).driverClassName(dsProperties.getDriverClassName())
                .url(dsProperties.getUrl()).username(dsProperties.getUsername()).password(dsProperties.getPassword());

        if (dsProperties.getType() != null) {
            dataSourceBuilder.type(dsProperties.getType());
        }

        return dataSourceBuilder.build();
    }

    @Bean
    public PlatformTransactionManager demoTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(demoEntityManagerFactory().getObject());
        return transactionManager;
    }
}
