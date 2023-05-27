package com.example.aviasales.config;

import java.util.Properties;

import javax.sql.DataSource;
import javax.transaction.TransactionManager;

import bitronix.tm.BitronixTransactionManager;
import bitronix.tm.TransactionManagerServices;
import bitronix.tm.resource.jdbc.PoolingDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

@Configuration
public class BitronixConfig {

    @Value("${spring.datasource.username}")
    private String userName;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.url}")
    private String url;

    @Bean(name = "bitronixTransactionManager")
    public BitronixTransactionManager bitronixTransactionManager() throws Throwable {
        BitronixTransactionManager bitronixTransactionManager = TransactionManagerServices.getTransactionManager();
        bitronixTransactionManager.setTransactionTimeout(60);
        return bitronixTransactionManager;
    }

    @Bean
    public PlatformTransactionManager transactionManager(TransactionManager bitronixTransactionManager) {
        return new JtaTransactionManager(bitronixTransactionManager);
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        PoolingDataSource bitronixDataSourceBean = new PoolingDataSource();
        bitronixDataSourceBean.setMaxPoolSize(5);
        bitronixDataSourceBean.setUniqueName("primaryPostgreSqlDataSourceResource");
        bitronixDataSourceBean.setClassName("org.postgresql.xa.PGXADataSource");
        bitronixDataSourceBean.setAllowLocalTransactions(true);
        Properties properties = new Properties();
        properties.put("user", userName);
        properties.put("password", password);
        properties.put("url", url);
        bitronixDataSourceBean.setDriverProperties(properties);
        return bitronixDataSourceBean;
    }
}
