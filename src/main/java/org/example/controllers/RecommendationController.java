package org.example.controllers;

import org.example.entities.Book;
import org.example.entities.User;
import org.example.security.CustomUserDetails;
import org.example.services.RecommendationService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/recommendations")
    public String showRecommendations(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        User user = userDetails.getUser();
        List<Book> recommendations = recommendationService.getRecommendations(user);
        model.addAttribute("recommendations", recommendations);
        return "recommendations";
    }
}
