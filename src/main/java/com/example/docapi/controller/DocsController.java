package com.example.docapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/documents")
public class DocsController {

//    private final DocumentsService docService;
//
//    @GetMapping("/list")
//    public String documentsList(Model model) {
//        List<String> docs = docService.templatesList();
//        model.addAttribute("templates", docs);
//        return "documents/list";
//    }
//
//    @GetMapping("/form")
//    public String documentFormPage(@RequestParam String template, Model model) {
//
//        DpsDocTemplate docTemplate = ;
//
//        model.addAttribute("resultName", resultName(template));
//        model.addAttribute("document", docTemplate);
//
//        return "documents/form";
//    }
//
//    @PostMapping("/form")
//    public void documentForm(@ModelAttribute DpsDocTemplate document,
//                               @RequestParam String resultName,
//                               Model model){
//        docService.parseDoc(document, resultName);
//    }
//
//    @GetMapping("/myDocs")
//    public String myDocuments(Model model) {
//        List<String> docs = docService.outputList();
//        model.addAttribute("docNames", docs);
//        return "documents/outputList";
//    }
//
//    @GetMapping("/myDocs/download")
//    public void downloadFile(@RequestParam String docName,
//        HttpServletResponse response) {
//        try {
//            InputStream is = new FileInputStream(docService.getOutputFile(docName));
//            IOUtils.copy(is, response.getOutputStream());
//            response.setContentType("application/xlsx");
//            response.flushBuffer();
//        } catch (IOException ex) {
//            throw new RuntimeException("IOError writing file to output stream");
//        }
//    }
//
//
//
//    public String resultName(String templateName){
//        return String.format("%s от %s",
//                templateName.replaceAll("\\.[\\w]+", ""),
//                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
//    }

}
