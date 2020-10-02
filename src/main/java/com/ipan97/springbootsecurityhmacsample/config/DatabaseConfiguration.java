package com.ipan97.springbootsecurityhmacsample.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories("com.ipan97.springbootsecurityhmacsample.repository.jpa")
@EnableElasticsearchRepositories("com.ipan97.springbootsecurityhmacsample.repository.elasticsearch")
@EnableTransactionManagement
public class DatabaseConfiguration {
}
