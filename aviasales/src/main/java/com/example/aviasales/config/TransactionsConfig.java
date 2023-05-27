package com.example.aviasales.config;

import bitronix.tm.BitronixTransactionManager;
import bitronix.tm.TransactionManagerServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.transaction.SystemException;

@Configuration
public class TransactionsConfig {
    @Bean
    public bitronix.tm.Configuration transactionManagerServices() {
        bitronix.tm.Configuration configuration = TransactionManagerServices.getConfiguration();
        configuration.setServerId("1");
        return configuration;
    }
    @Bean(name = "bitronixTransactionManager")
    public BitronixTransactionManager transactionManager(bitronix.tm.Configuration _c){
        BitronixTransactionManager trans = TransactionManagerServices.getTransactionManager();
        try {
            trans.setTransactionTimeout(60);
        } catch (SystemException e) {
            throw new RuntimeException(e);
        }
        return trans;

    }
}
