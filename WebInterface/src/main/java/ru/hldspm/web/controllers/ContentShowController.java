package ru.hldspm.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;
import ru.hldspm.web.entities.Content;
import ru.hldspm.web.repository.ContentRepository;

@Controller
public class ContentShowController {
    private final int PAGE_SIZE = 50;

    @Autowired
    private ContentRepository contentRepository;

    @GetMapping(value={"/content/{pageNumber}", "/content/{pageNumber}/"})
    public String home(@PathVariable("pageNumber") int pageNumber, Model model) {
        Page<Content> contentPage = contentRepository.findAll(PageRequest.of(pageNumber, PAGE_SIZE));
        model.addAttribute("contentList", contentPage.getContent());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", contentPage.getTotalPages());
        return "content";
    }

    @GetMapping(value={"/content", "/content/"})
    public RedirectView contentRedirect(){
        return new RedirectView("/content/0");
    }
}
