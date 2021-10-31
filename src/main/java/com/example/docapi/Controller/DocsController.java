package com.example.docapi.Controller;

import com.example.docapi.Service.DocumentsService;
import com.example.documentsparser.Documents.model.DocTemplateForDocParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
@RequestMapping("/documents")
public class DocsController {

    private final DocumentsService docService;

    @GetMapping("/list")
    public String documentsList(Model model) {
        model.addAttribute("templates", docService.documentsList());
        return "documents/list";
    }

    @GetMapping("/form")
    public String documentFormPage(@RequestParam String templateName, Model model) {

        String resultName = String.format("%s от %s",
                templateName.replaceAll("[.]\\.+", ""),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        DocTemplateForDocParser docTemplate = docService.getTemplate(templateName);

        model.addAttribute("resultName", resultName);
        model.addAttribute("document", docTemplate);

        return "documents/form";
    }

    @PostMapping("/form")
    public String documentForm(@ModelAttribute DocTemplateForDocParser document,
                               @RequestParam String resultName,
                               Model model){
        docService.parseDoc(document, resultName);
        return "redirect:/";
    }
}
