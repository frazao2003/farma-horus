package com.horus.formataArquivoHorus.config;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class HibernateConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MultiTenantConnectionProvider multiTenantConnectionProvider;

    @Autowired
    private CurrentTenantIdentifierResolver currentTenantIdentifierResolver;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.horus.formataArquivoHorus.model"); // Pacote das entidades
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        // Definindo as propriedades diretamente
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect"); // Dialeto para PostgreSQL
        properties.put("hibernate.multiTenancy", "SCHEMA"); // Configuração do multi-tenant
        properties.put("hibernate.multiTenantConnectionProvider", multiTenantConnectionProvider);
        properties.put("hibernate.multiTenantIdentifierResolver", currentTenantIdentifierResolver);
        properties.put("hibernate.hbm2ddl.auto", "update"); // Geração automática do schema

        em.setJpaProperties(properties);
        return em;
    }
}
