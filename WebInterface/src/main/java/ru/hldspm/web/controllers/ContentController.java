package ru.hldspm.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import ru.hldspm.web.entities.Content;
import ru.hldspm.web.entities.ContentType;
import ru.hldspm.web.entities.ContentVersion;
import ru.hldspm.web.entities.Game;
import ru.hldspm.web.entities.Platform;
import ru.hldspm.web.entities.User;
import ru.hldspm.web.repository.ContentRepository;
import ru.hldspm.web.repository.ContentTypeRepository;
import ru.hldspm.web.repository.ContentVersionRepository;
import ru.hldspm.web.repository.GameRepository;
import ru.hldspm.web.repository.PlatformRepository;
import ru.hldspm.web.repository.UserRepository;
import ru.hldspm.web.service.CacheUpdateService;
import ru.hldspm.web.service.FilesProcessingService;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;

@Controller
public class ContentController {

    @Autowired
    private ContentRepository contentRepository;
    @Autowired
    private ContentTypeRepository contentTypeRepository;

    @Autowired
    private PlatformRepository platformRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContentVersionRepository contentVersionRepository;


    @GetMapping(value={"/content/{pageNumber}", "/content/{pageNumber}/"})
    public String home(@PathVariable("pageNumber") int pageNumber, Model model) {
        int PAGE_SIZE = 50;
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

    @GetMapping("/add-content")
    public String showAddContentForm(Model model) {
        model.addAttribute("content", new Content());
        model.addAttribute("contentTypes", contentTypeRepository.findAll());
        model.addAttribute("platforms", platformRepository.findAll());
        model.addAttribute("games", gameRepository.findAll());
        model.addAttribute("users", userRepository.findAll());
        return "add-content";
    }

    @PostMapping("/add-content")
    public String addContent(@RequestParam("version") String version, @RequestPart("file") MultipartFile contentArchive, @ModelAttribute Content content) {
        Content savedContent;
        try {
            content.setUploadedAt(LocalDateTime.now());
            ContentType contentType = contentTypeRepository.findById(content.getContentType().getId()).orElse(null);
            Platform platform = platformRepository.findById(content.getPlatform().getId()).orElse(null);
            Game game = gameRepository.findById(content.getGame().getId()).orElse(null);
            User uploader = userRepository.findById(content.getUploader().getId()).orElse(null);
            content.setContentType(contentType);
            content.setPlatform(platform);
            content.setGame(game);
            content.setUploader(uploader);
            savedContent = contentRepository.save(content);
        } catch (Exception e){
            savedContent = contentRepository.findByName(content.getName());
        }
        if (savedContent.getVersions() == null) {
            savedContent.setVersions(new HashSet<>());
        }
        // Saves content's version to the versions table if necessary
        if (!Objects.equals(savedContent.getContentType().getName(), "map")) {
            ContentVersion contentVersion = new ContentVersion();
            contentVersion.setContent(savedContent);
            contentVersion.setVersion(version);
            contentVersion.setUploadedAt(LocalDateTime.now());
            contentVersionRepository.save(contentVersion);
            FilesProcessingService.saveUploadedContent(contentArchive, savedContent.getGame(), savedContent.getPlatform(), savedContent.getContentType(), savedContent.getName(), version);
            //TODO Add JSONs update
        }
        CacheUpdateService.updateJsonCache(savedContent.getGame(), savedContent.getPlatform(), savedContent.getContentType(), savedContent.getName(), version);
        return "redirect:/add-content";
    }


}
