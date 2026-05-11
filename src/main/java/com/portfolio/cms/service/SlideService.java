package com.portfolio.cms.service;

import com.portfolio.cms.dto.SlideForm;
import com.portfolio.cms.entity.Category;
import com.portfolio.cms.entity.Slide;
import com.portfolio.cms.entity.SlideImage;
import com.portfolio.cms.repository.SlideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SlideService {

    private final SlideRepository slideRepository;
    private final CategoryService categoryService;
    private final FileStorageService fileStorageService;

    public List<Slide> findAllNotDeleted() {
        return slideRepository.findAllNotDeleted();
    }

    /**
     * 公開LP で参照される is_active = true のスライド一覧。
     * 内容は admin が CRUD した時のみ変わるため、Caffeine で 5 分キャッシュする
     * （TTL は application.yml で定義）。CRUD 操作側で {@link CacheEvict} を呼ぶ
     * ので、書込み直後の閲覧でも古いキャッシュは出ない。
     */
    @Cacheable("publicSlides")
    public List<Slide> findAllActive() {
        return slideRepository.findAllActive();
    }

    public Slide findByIdForAdmin(Long id) {
        return slideRepository.findByIdNotDeleted(id)
                .orElseThrow(() -> new IllegalArgumentException("スライドが見つかりません: id=" + id));
    }

    @Transactional
    @CacheEvict(value = "publicSlides", allEntries = true)
    public Slide create(SlideForm form) {
        Category category = categoryService.findById(form.getCategoryId());
        Slide slide = Slide.builder()
                .category(category)
                .title(form.getTitle())
                .description(form.getDescription())
                .sortOrder(form.getSortOrder())
                .isActive(Boolean.TRUE.equals(form.getIsActive()))
                .build();

        attachImages(slide, form.getImages());
        return slideRepository.save(slide);
    }

    @Transactional
    @CacheEvict(value = "publicSlides", allEntries = true)
    public Slide update(Long id, SlideForm form) {
        Slide slide = findByIdForAdmin(id);
        slide.setCategory(categoryService.findById(form.getCategoryId()));
        slide.setTitle(form.getTitle());
        slide.setDescription(form.getDescription());
        slide.setSortOrder(form.getSortOrder());
        slide.setIsActive(Boolean.TRUE.equals(form.getIsActive()));

        attachImages(slide, form.getImages());
        return slide;
    }

    @Transactional
    @CacheEvict(value = "publicSlides", allEntries = true)
    public void softDelete(Long id) {
        Slide slide = findByIdForAdmin(id);
        slide.setDeletedAt(LocalDateTime.now());
        slide.setIsActive(false);
    }

    private void attachImages(Slide slide, List<MultipartFile> uploads) {
        if (uploads == null) return;
        int nextOrder = slide.getImages().size();
        for (MultipartFile f : uploads) {
            if (f == null || f.isEmpty()) continue;
            String stored = fileStorageService.store(f);
            SlideImage image = SlideImage.builder()
                    .slide(slide)
                    .fileName(stored)
                    .sortOrder(nextOrder++)
                    .build();
            slide.getImages().add(image);
        }
    }
}
