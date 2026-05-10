package com.portfolio.cms.service;

import com.portfolio.cms.entity.Category;
import com.portfolio.cms.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> findAllSorted() {
        return categoryRepository.findAllByOrderBySortOrderAsc();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("カテゴリが見つかりません: id=" + id));
    }
}
