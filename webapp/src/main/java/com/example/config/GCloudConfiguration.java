package com.example.config;

import com.google.cloud.spring.autoconfigure.config.GcpConfigBootstrapConfiguration;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({PropertyPlaceholderAutoConfiguration.class, GcpConfigBootstrapConfiguration.class})
public class GCloudConfiguration {
}
