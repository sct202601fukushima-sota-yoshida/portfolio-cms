package com.portfolio.cms.controller;

import com.portfolio.cms.dto.SlideForm;
import com.portfolio.cms.entity.Slide;
import com.portfolio.cms.service.CategoryService;
import com.portfolio.cms.service.SlideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/slides")
@RequiredArgsConstructor
public class AdminSlideController {

    private final SlideService slideService;
    private final CategoryService categoryService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("slides", slideService.findAllNotDeleted());
        return "admin/slides/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("slideForm", new SlideForm());
        model.addAttribute("categories", categoryService.findAllSorted());
        model.addAttribute("mode", "create");
        return "admin/slides/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("slideForm") SlideForm form,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.findAllSorted());
            model.addAttribute("mode", "create");
            return "admin/slides/form";
        }
        Slide saved = slideService.create(form);
        ra.addFlashAttribute("message", "スライドを登録しました: " + saved.getTitle());
        return "redirect:/admin/slides";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Slide slide = slideService.findByIdForAdmin(id);
        SlideForm form = new SlideForm();
        form.setId(slide.getId());
        form.setCategoryId(slide.getCategory().getId());
        form.setTitle(slide.getTitle());
        form.setDescription(slide.getDescription());
        form.setSortOrder(slide.getSortOrder());
        form.setIsActive(slide.getIsActive());

        model.addAttribute("slideForm", form);
        model.addAttribute("slide", slide);
        model.addAttribute("categories", categoryService.findAllSorted());
        model.addAttribute("mode", "edit");
        return "admin/slides/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("slideForm") SlideForm form,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("slide", slideService.findByIdForAdmin(id));
            model.addAttribute("categories", categoryService.findAllSorted());
            model.addAttribute("mode", "edit");
            return "admin/slides/form";
        }
        slideService.update(id, form);
        ra.addFlashAttribute("message", "スライドを更新しました");
        return "redirect:/admin/slides";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        slideService.softDelete(id);
        ra.addFlashAttribute("message", "スライドを削除しました");
        return "redirect:/admin/slides";
    }
}
