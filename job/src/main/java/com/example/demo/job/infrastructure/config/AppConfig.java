package com.example.demo.job.infrastructure.config;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
@ComponentScan(basePackages = {
    "com.example.demo.core",
    "com.example.demo.job",
})
public class AppConfig {

    @Bean
    public ExecutionEnvironment getStreamExecutionEnvironment() {
        return ExecutionEnvironment.getExecutionEnvironment();
    }

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
