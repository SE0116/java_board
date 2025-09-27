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

        // 사이즈/페이지 안전 범위 보정
        int pageSize = Math.max(1, Math.min(size, 100));
        int pageNum  = Math.max(1, page);

        var pageParam = new PageParam(pageNum, pageSize, q);
        var posts = postService.list(pageParam);
        long total = postService.count(pageParam);

        // 페이지네이션 계산
        long totalPages = (total + pageSize - 1) / pageSize;
        if (totalPages == 0) totalPages = 1; // 데이터 없을 때도 1 페이지로 취급
        if (pageNum > totalPages) {
            pageNum = (int) totalPages;
            pageParam = new PageParam(pageNum, pageSize, q);
            posts = postService.list(pageParam); // 보정된 페이지로 재조회(선택)
        }

        boolean hasPrev = pageNum > 1;
        boolean hasNext = pageNum < totalPages;

        model.addAttribute("posts", posts);
        model.addAttribute("page", pageNum);
        model.addAttribute("size", pageSize);
        model.addAttribute("q", q);
        model.addAttribute("total", total);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("hasNext", hasNext);

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
}
