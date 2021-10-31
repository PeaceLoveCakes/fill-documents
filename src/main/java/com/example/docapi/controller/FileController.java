package com.example.docapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FileController {

//    private final DocService docService;
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @PostMapping(
//            path = "/replaceContent",
//            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<?> replaceContent(@RequestPart MultipartFile file,
//                                            @RequestParam String replacesJson) throws IOException {
//        File tmp = docService.convert(file);
//        Map<String, String> replaces = objectMapper.readValue(replacesJson, Map.class);
//        File result = docService.replaceContent(tmp, replaces);
//        tmp.delete();
//
//        byte[] data = Files.readAllBytes(result.toPath());
//        ByteArrayResource resource = new ByteArrayResource(data);
//        result.delete();
//        String contentType = "application/octet-stream";
//        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(contentType))
//                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
//                .body(resource);
//    }
//
//    @PostMapping(path = "/findByRegex",
//            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public Set<String> findByRegex(@RequestPart MultipartFile file,
//                                   @RequestBody String pattern) {
//        File tmp = docService.convert(file);
//        Set<String> result = docService.findByRegex(tmp, pattern);
//        tmp.delete();
//        return result;
//    }
//
//    @PostMapping(path = "/findAllByRegex",
//            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public Map<String, Set<String>> findAllByRegex(@RequestPart MultipartFile file,
//                                                   @RequestBody Set<String> patterns) {
//        File tmp = docService.convert(file);
//        Map<String, Set<String>> result = docService.findByRegex(tmp, patterns);
//        tmp.delete();
//        return result;
//    }
}