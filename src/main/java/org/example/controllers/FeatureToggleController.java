package org.example.controllers;

import org.ff4j.FF4j;
import org.ff4j.core.Feature;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/admin/features")
public class FeatureToggleController {

    private final FF4j ff4j;

    public FeatureToggleController(FF4j ff4j) {
        this.ff4j = ff4j;
    }

    @GetMapping
    public String featureList(Model model) {
        Map<String, Feature> features = ff4j.getFeatures();
        model.addAttribute("features", features.values());

        int addToCartRollout = ff4j.getProperty("showAddToCart.rollout").asInt();
        int layoutRollout = ff4j.getProperty("newCatalogLayout.rollout").asInt();

        model.addAttribute("showAddToCartRollout", addToCartRollout);
        model.addAttribute("newCatalogLayoutRollout", layoutRollout);

        return "admin/books/features";
    }

    @PostMapping("/toggle")
    public String toggleFeature(@RequestParam String featureName,
                                @RequestParam(required = false) String enabled) {

        System.out.println("poop POST: featureName=" + featureName + ", enabled=" + enabled);
        if (enabled != null) {
            ff4j.enable(featureName);
        } else {
            ff4j.disable(featureName);
        }

        return "redirect:/admin/features";
    }

    @PostMapping("/property")
    public String updateProperty(@RequestParam String propertyName,
                                 @RequestParam int value) {

        ff4j.getPropertiesStore().updateProperty(propertyName, String.valueOf(value));
        return "redirect:/admin/features";
    }

}
