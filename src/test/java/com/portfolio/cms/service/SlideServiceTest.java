package com.portfolio.cms.service;

import com.portfolio.cms.dto.SlideForm;
import com.portfolio.cms.entity.Category;
import com.portfolio.cms.entity.Slide;
import com.portfolio.cms.repository.SlideRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SlideServiceTest {

    @Mock SlideRepository slideRepository;
    @Mock CategoryService categoryService;
    @Mock FileStorageService fileStorageService;

    @InjectMocks SlideService slideService;

    @Test
    void findAllNotDeleted_delegatesToRepository() {
        Slide slide = Slide.builder().id(1L).title("s").build();
        given(slideRepository.findAllNotDeleted()).willReturn(List.of(slide));

        List<Slide> result = slideService.findAllNotDeleted();

        assertThat(result).containsExactly(slide);
    }

    @Test
    void findByIdForAdmin_returnsSlide_whenFound() {
        Slide slide = Slide.builder().id(10L).title("t").build();
        given(slideRepository.findByIdNotDeleted(10L)).willReturn(Optional.of(slide));

        assertThat(slideService.findByIdForAdmin(10L)).isSameAs(slide);
    }

    @Test
    void findByIdForAdmin_throws_whenNotFound() {
        given(slideRepository.findByIdNotDeleted(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> slideService.findByIdForAdmin(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("99");
    }

    @Test
    void create_buildsSlideFromFormAndSaves() {
        Category cat = Category.builder().id(1L).name("自己紹介").build();
        given(categoryService.findById(1L)).willReturn(cat);
        given(slideRepository.save(any(Slide.class))).willAnswer(inv -> inv.getArgument(0));

        SlideForm form = new SlideForm();
        form.setCategoryId(1L);
        form.setTitle("新しいスライド");
        form.setDescription("説明");
        form.setSortOrder(3);
        form.setIsActive(true);

        Slide saved = slideService.create(form);

        assertThat(saved.getCategory()).isEqualTo(cat);
        assertThat(saved.getTitle()).isEqualTo("新しいスライド");
        assertThat(saved.getSortOrder()).isEqualTo(3);
        assertThat(saved.getIsActive()).isTrue();
        verify(slideRepository).save(saved);
    }

    @Test
    void update_modifiesExistingSlide() {
        Category cat = Category.builder().id(2L).name("スキル").build();
        Slide existing = Slide.builder()
                .id(5L).category(Category.builder().id(1L).build())
                .title("古い").sortOrder(0).isActive(true).build();
        given(slideRepository.findByIdNotDeleted(5L)).willReturn(Optional.of(existing));
        given(categoryService.findById(2L)).willReturn(cat);

        SlideForm form = new SlideForm();
        form.setCategoryId(2L);
        form.setTitle("更新後");
        form.setSortOrder(9);
        form.setIsActive(false);

        Slide updated = slideService.update(5L, form);

        assertThat(updated.getId()).isEqualTo(5L);
        assertThat(updated.getCategory()).isEqualTo(cat);
        assertThat(updated.getTitle()).isEqualTo("更新後");
        assertThat(updated.getSortOrder()).isEqualTo(9);
        assertThat(updated.getIsActive()).isFalse();
    }

    @Test
    void softDelete_setsDeletedAtAndInactive() {
        Slide existing = Slide.builder()
                .id(7L).title("削除対象").isActive(true).deletedAt(null).build();
        given(slideRepository.findByIdNotDeleted(7L)).willReturn(Optional.of(existing));

        slideService.softDelete(7L);

        assertThat(existing.getDeletedAt()).isNotNull();
        assertThat(existing.getIsActive()).isFalse();
    }
}
