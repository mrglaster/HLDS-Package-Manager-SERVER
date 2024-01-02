package ru.hldspm.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.hldspm.web.entities.Content;
import ru.hldspm.web.repository.ContentRepository;

import java.util.List;

@Controller
public class AuthController {
    @Autowired
    private ContentRepository contentRepository;
    @GetMapping("/")
    public String home(Model model) {
        List<Content> contentList = contentRepository.findAll();
        model.addAttribute("contentList", contentList);
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
