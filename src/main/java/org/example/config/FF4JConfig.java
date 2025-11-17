package org.example.config;
import org.ff4j.FF4j;
import org.ff4j.property.PropertyInt;
import org.ff4j.property.store.InMemoryPropertyStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FF4JConfig {
    @Bean
    public FF4j ff4j() {

        FF4j ff4j = new FF4j();

        ff4j.setPropertiesStore(new InMemoryPropertyStore());

        ff4j.createFeature("showAddToCart", true);
        ff4j.createFeature("newCatalogLayout", true);

        if (!ff4j.getPropertiesStore().existProperty("showAddToCart.rollout")) {
            ff4j.createProperty(new PropertyInt("showAddToCart.rollout", 100));
        }

        if (!ff4j.getPropertiesStore().existProperty("newCatalogLayout.rollout")) {
            ff4j.createProperty(new PropertyInt("newCatalogLayout.rollout", 100));
        }

        return ff4j;
    }
}
