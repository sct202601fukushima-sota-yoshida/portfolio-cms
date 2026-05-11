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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AdminSlideController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfig.class))
@Import(SecurityConfig.class)
class AdminSlideControllerTest {

    @Autowired MockMvc mockMvc;

    @MockBean SlideService slideService;
    @MockBean CategoryService categoryService;
    @MockBean CustomUserDetailsService customUserDetailsService;

    @Test
    void list_redirectsToLogin_whenAnonymous() throws Exception {
        mockMvc.perform(get("/admin/slides"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/admin/login"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void list_returnsListView_whenAdmin() throws Exception {
        given(slideService.findAllNotDeleted()).willReturn(List.of());

        mockMvc.perform(get("/admin/slides"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/slides/list"))
                .andExpect(model().attributeExists("slides"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createForm_rendersFormWithCategories() throws Exception {
        Category cat = Category.builder().id(1L).name("自己紹介").build();
        given(categoryService.findAllSorted()).willReturn(List.of(cat));

        mockMvc.perform(get("/admin/slides/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/slides/form"))
                .andExpect(model().attribute("mode", "create"))
                .andExpect(model().attributeExists("slideForm", "categories"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void create_rerendersForm_whenValidationFails() throws Exception {
        given(categoryService.findAllSorted()).willReturn(List.of());

        mockMvc.perform(post("/admin/slides")
                        .with(csrf())
                        .param("title", "")
                        .param("sortOrder", "0"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/slides/form"))
                .andExpect(model().attributeHasFieldErrors("slideForm", "title", "categoryId"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void create_redirectsToList_onSuccess() throws Exception {
        Slide saved = Slide.builder().id(1L).title("新スライド").build();
        given(slideService.create(any())).willReturn(saved);

        mockMvc.perform(post("/admin/slides")
                        .with(csrf())
                        .param("categoryId", "1")
                        .param("title", "新スライド")
                        .param("sortOrder", "0")
                        .param("isActive", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/slides"))
                .andExpect(flash().attributeExists("message"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void delete_callsSoftDeleteAndRedirects() throws Exception {
        mockMvc.perform(post("/admin/slides/42/delete").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/slides"));

        verify(slideService).softDelete(eq(42L));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void editForm_rendersFormPrefilledWithExistingSlide() throws Exception {
        Category cat = Category.builder().id(1L).name("自己紹介").sortOrder(0).build();
        Slide existing = Slide.builder()
                .id(42L)
                .category(cat)
                .title("既存スライド")
                .description("既存本文")
                .sortOrder(3)
                .isActive(true)
                .build();
        given(slideService.findByIdForAdmin(42L)).willReturn(existing);
        given(categoryService.findAllSorted()).willReturn(List.of(cat));

        mockMvc.perform(get("/admin/slides/42/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/slides/form"))
                .andExpect(model().attribute("mode", "edit"))
                .andExpect(model().attributeExists("slideForm", "slide", "categories"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void update_rerendersForm_whenValidationFails() throws Exception {
        Category cat = Category.builder().id(1L).name("自己紹介").sortOrder(0).build();
        Slide existing = Slide.builder().id(42L).category(cat).title("title").build();
        given(slideService.findByIdForAdmin(42L)).willReturn(existing);
        given(categoryService.findAllSorted()).willReturn(List.of(cat));

        mockMvc.perform(post("/admin/slides/42")
                        .with(csrf())
                        .param("title", "")          // title is required → validation error
                        .param("sortOrder", "0"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/slides/form"))
                .andExpect(model().attribute("mode", "edit"))
                .andExpect(model().attributeHasFieldErrors("slideForm", "title"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void update_redirectsToList_onSuccess() throws Exception {
        mockMvc.perform(post("/admin/slides/42")
                        .with(csrf())
                        .param("categoryId", "1")
                        .param("title", "更新後のタイトル")
                        .param("description", "更新後の本文")
                        .param("sortOrder", "2")
                        .param("isActive", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/slides"))
                .andExpect(flash().attributeExists("message"));

        verify(slideService).update(eq(42L), any());
    }
}
