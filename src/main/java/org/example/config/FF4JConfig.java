package org.example.config;
import org.ff4j.FF4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FF4JConfig {
    @Bean
    public FF4j ff4j() {
        return new FF4j()
                .createFeature("showAddToCart", true)
                .createFeature("newCatalogLayout", false)
                .autoCreate(true);
    }
}
