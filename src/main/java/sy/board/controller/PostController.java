package sy.board.controller;

import sy.board.domain.Post;
import sy.board.service.PostService;
import sy.board.support.PageParam;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public String list(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(required = false) String q,
                       Model model) {
        var pageParam = new PageParam(page, size, q);
        var posts = postService.list(pageParam);
        var total = postService.count(pageParam);
        model.addAttribute("posts", posts);
        model.addAttribute("page", pageParam);
        model.addAttribute("total", total);
        return "posts/list";
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("post", new Post());
        return "posts/form";
    }

    @PostMapping
    public String write(@Valid @ModelAttribute Post post, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "posts/form";
        Long id = postService.write(post);
        return "redirect:/posts/" + id;
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("post", postService.get(id, true));
        return "posts/view";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("post", postService.get(id, false));
        return "posts/form";
    }

    @PutMapping("/{id}")
    public String edit(@PathVariable Long id, @Valid @ModelAttribute Post post, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "posts/form";
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
