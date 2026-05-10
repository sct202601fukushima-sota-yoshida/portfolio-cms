package com.portfolio.cms.service;

import com.portfolio.cms.entity.Category;
import com.portfolio.cms.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock CategoryRepository categoryRepository;

    @InjectMocks CategoryService categoryService;

    @Test
    void findAllSorted_returnsCategoriesInSortOrder() {
        Category c1 = Category.builder().id(1L).name("自己紹介").sortOrder(0).build();
        Category c2 = Category.builder().id(2L).name("スキル").sortOrder(1).build();
        given(categoryRepository.findAllByOrderBySortOrderAsc()).willReturn(List.of(c1, c2));

        assertThat(categoryService.findAllSorted()).containsExactly(c1, c2);
    }

    @Test
    void findById_returnsCategory_whenFound() {
        Category c = Category.builder().id(3L).name("実績").build();
        given(categoryRepository.findById(3L)).willReturn(Optional.of(c));

        assertThat(categoryService.findById(3L)).isSameAs(c);
    }

    @Test
    void findById_throws_whenNotFound() {
        given(categoryRepository.findById(404L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.findById(404L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("404");
    }
}
