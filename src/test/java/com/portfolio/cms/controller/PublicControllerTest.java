package com.portfolio.cms.controller;

import com.portfolio.cms.config.SecurityConfig;
import com.portfolio.cms.config.WebMvcConfig;
import com.portfolio.cms.entity.Category;
import com.portfolio.cms.entity.Slide;
import com.portfolio.cms.service.CategoryService;
import com.portfolio.cms.service.CustomUserDetailsService;
import com.portfolio.cms.service.SlideService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PublicController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfig.class))
@Import(SecurityConfig.class)
class PublicControllerTest {

    @Autowired MockMvc mockMvc;

    @MockBean SlideService slideService;
    @MockBean CategoryService categoryService;
    @MockBean CustomUserDetailsService customUserDetailsService;

    @Test
    void home_returnsHomeViewWithSlidesAndCategories() throws Exception {
        Category cat = Category.builder().id(1L).name("自己紹介").sortOrder(0).build();
        Slide slide = Slide.builder().id(10L).title("Hello").category(cat).build();
        given(slideService.findAllActive()).willReturn(List.of(slide));
        given(categoryService.findAllSorted()).willReturn(List.of(cat));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("public/home"))
                .andExpect(model().attributeExists("slides", "categories"));
    }

    @Test
    void home_isAccessibleWithoutAuthentication() throws Exception {
        given(slideService.findAllActive()).willReturn(List.of());
        given(categoryService.findAllSorted()).willReturn(List.of());

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("slides", containsInAnyOrder()));
    }
}
