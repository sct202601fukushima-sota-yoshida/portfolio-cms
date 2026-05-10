package com.portfolio.cms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class SlideForm {

    private Long id;

    @NotNull(message = "カテゴリを選択してください")
    private Long categoryId;

    @NotBlank(message = "タイトルは必須です")
    @Size(max = 100, message = "タイトルは100文字以内で入力してください")
    private String title;

    private String description;

    @NotNull
    @PositiveOrZero(message = "表示順は0以上の数値を入力してください")
    private Integer sortOrder = 0;

    private Boolean isActive = true;

    private List<MultipartFile> images;
}
