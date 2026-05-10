package com.portfolio.cms.controller;

import com.portfolio.cms.service.CategoryService;
import com.portfolio.cms.service.SlideService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PublicController {

    private final SlideService slideService;
    private final CategoryService categoryService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("slides", slideService.findAllActive());
        model.addAttribute("categories", categoryService.findAllSorted());
        return "public/home";
    }
}
