package sy.board.controller;

import sy.board.domain.Post;
import sy.board.service.PostService;
import sy.board.support.PageParam;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text == null) {
                    setValue(null);
                } else {
                    String trimmed = text.trim();
                    setValue(trimmed.isEmpty() ? null : trimmed);
                }
            }
        });
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(required = false) String q,
                       Model model) {

        // 목록 & 총 개수
        var posts = postService.list(page, size, q);
        long total = postService.count(q);

        // 총 페이지
        int totalPages = Math.max(1, (int) ((total + size - 1) / size));
        page = Math.min(Math.max(1, page), totalPages);

        // 페이지 윈도우 계산 (기본: 현재 기준 좌우 5, 총 11개)
        int left = 5, right = 5;
        int start = Math.max(1, page - left);
        int end = Math.min(totalPages, page + right);

        // 실제 노출 개수가 11보다 작으면 남는 몫을 한쪽에 보정(앞/뒤에 페이지가 부족한 케이스)
        int targetCount = left + 1 + right; // 11
        int currentCount = end - start + 1;
        int need = targetCount - currentCount;
        if (need > 0) {
            if (start == 1) {
                end = Math.min(totalPages, end + need);
            } else if (end == totalPages) {
                start = Math.max(1, start - need);
            }
        }

        // Prev/Next (윈도우 바깥 이동)
        boolean showPrev = start > 1;
        boolean showNext = end < totalPages;
        int prevPage = Math.max(1, start - 1);
        int nextPage = Math.min(totalPages, end + 1);

        // 페이지 목록
        var pagesToShow = java.util.stream.IntStream.rangeClosed(start, end)
                .boxed().toList();

        model.addAttribute("posts", posts);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("q", q);
        model.addAttribute("total", total);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pagesToShow", pagesToShow);
        model.addAttribute("showPrev", showPrev);
        model.addAttribute("showNext", showNext);
        model.addAttribute("prevPage", prevPage);
        model.addAttribute("nextPage", nextPage);

        return "posts/list";
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("post", new Post());
        return "posts/form";
    }

    @PostMapping
    public String write(@Valid @ModelAttribute Post post, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "posts/form";
        }
        Long id = postService.write(post);
        return "redirect:/posts/" + id;
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("post", postService.getForView(id));
        return "posts/view";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("post", postService.get(id));
        return "posts/form";
    }
    @PutMapping("/{id}")
    public String edit(@PathVariable Long id,
                       @Valid @ModelAttribute Post post,
                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "posts/form";
        }
        post.setId(id);
        postService.edit(post);
        return "redirect:/posts/" + id;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        postService.delete(id);
        return "redirect:/posts";
    }

    @PostMapping("/bulk-delete")
    public String bulkDelete(@RequestParam(value = "ids", required = false) java.util.List<Long> ids,
                             @RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "10") int size,
                             @RequestParam(required = false) String q,
                             org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        if (ids != null && !ids.isEmpty()) {
            postService.deleteByIds(ids);
        }
        // 목록으로 돌아갈 때 기존 조건 유지
        ra.addAttribute("page", page);
        ra.addAttribute("size", size);
        if (q != null && !q.isBlank()) ra.addAttribute("q", q);
        return "redirect:/posts";
    }
}
