package com.example.docapi.service;

import com.example.docapi.controller.dto.AttributeDto;
import com.example.documentsparser.DocService;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@UtilityClass
public class ParsingLogic {

    private final DocService docService = new DocService();

    private static final String ALLOWED_SIGNS = "A-Za-zА-Яа-я_ё\\-\\.";
    private static final String OPEN_TAG = "<!";
    private static final String CLOSE_TAG = ">";
    private static final Pattern TAG_PATTERN = Pattern.compile(String.format("%s[%s][%s\\s]+%s", OPEN_TAG, ALLOWED_SIGNS, ALLOWED_SIGNS, CLOSE_TAG));

    public Set<AttributeDto> findAttributes(File file) {
        return findAttributes(docService.findByRegex(file, TAG_PATTERN));
    }

    public Set<AttributeDto> findAttributes(Set<String> tags){
        Set<AttributeDto> attributeDtos = new HashSet<>();
        for (String tag : tags) {
            attributeDtos.add(attributeDtoFromTag(tag.substring(OPEN_TAG.length(), tag.length() - CLOSE_TAG.length())));
        }
        attributeDtos = mergeAttributes(attributeDtos);
        return attributeDtos;
    }

    private AttributeDto attributeDtoFromTag(String tag) {
        AttributeDto attribute = new AttributeDto();
        if(tag.contains(".")) {
            Set<AttributeDto> childes = new HashSet<>();
            childes.add(attributeDtoFromTag(tag.substring(tag.indexOf('.') + 1).trim()));
            attribute
                    .setName(tag.substring(0, tag.indexOf('.')).trim())
                    .setChildes(childes);
        } else {
            attribute
                    .setName(tag)
                    .setChildes(new HashSet<>());
        }
        return attribute;
    }

    private static Set<AttributeDto> mergeAttributes(Set<AttributeDto> attributeDtos) {
        if (attributeDtos == null || attributeDtos.isEmpty()) return new HashSet<>();
        List<AttributeDto> attributesList = new ArrayList<>(attributeDtos);
        AttributeDto attribute = attributesList.remove(0);
        Set<AttributeDto> childes = attribute.getChildes();
        for (AttributeDto attributeDto : attributesList){
            if (attributeDto.getName().equals(attribute.getName())){
                childes.addAll(attributeDto.getChildes());
            }
        }
        attribute.setChildes(mergeAttributes(attribute.getChildes()));
        Set<AttributeDto> result = mergeAttributes(attributesList.stream()
                .filter(attributeDto -> !attributeDto.getName().equals(attribute.getName()))
                .collect(Collectors.toSet()));
        result.add(attribute);
        return result;
    }

    public Map<String, String> attributeDtoToMap(List<AttributeDto> attributeDtos){
        return attributeDtos.stream()
                .map(ParsingLogic::attributeDtoToMap)
                .flatMap (map -> map.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue));
}

    public Map<String, String> attributeDtoToMap(AttributeDto attributeDto) {
        Map<String, String> map = new HashMap<>();
        if (attributeDto.getChildes() == null || attributeDto.getChildes().isEmpty()) {
            map.put(OPEN_TAG + attributeDto.getName() + CLOSE_TAG, attributeDto.getValue());
        } else {
            String nextPrefix = attributeDto.getName();
            Map<String, String> childesMap = attributeDto.getChildes().stream().map(child -> attributeDtoToMap(child, nextPrefix))
                    .flatMap(childMaps -> childMaps.entrySet().stream())
                    .collect(Collectors.toMap(Map.Entry::getKey,
                            Map.Entry::getValue));
            map.putAll(childesMap);
        }
        return map;
    }

    public Map<String, String> attributeDtoToMap(AttributeDto attributeDto, String prefix) {
        Map<String, String> map = new HashMap<>();
        if (attributeDto.getChildes() == null || attributeDto.getChildes().isEmpty()) {
            map.put(OPEN_TAG + prefix + "." + attributeDto.getName() + CLOSE_TAG, attributeDto.getValue());
        } else {
            String nextPrefix = prefix + "." + attributeDto.getName();
            Map<String, String> childesMap = attributeDto.getChildes().stream().map(child -> attributeDtoToMap(child, nextPrefix))
                    .flatMap(childMaps -> childMaps.entrySet().stream())
                    .collect(Collectors.toMap(Map.Entry::getKey,
                            Map.Entry::getValue));
            map.putAll(childesMap);
        }
        return map;
    }

    public void replaceContent(File input, File output, Map<String, String> replaces){
        docService.replaceContent(input, output, replaces);
    }


//    public String parseStringByTags(String input, Map<String, String> tags_values) {
//        String result = input;
//
//        for (String key : tags_values.keySet()) {
//            result = result.replaceAll("<!" + key + ">",
//                    tags_values.get(key).isEmpty() ? "" : tags_values.get(key));
//        }
//
//        return result;
//    }
//
//    public String parseStringByTemplates(String input, Map<String, DpsTemplate> templates) {
//        String result = input;
//
//        for (String key : templates.keySet()) {
//            for (DpsAttribute dpsAttribute : templates.get(key).getAttributesForDocParser()) {
//                if (!(dpsAttribute.getValue() == null || dpsAttribute.getValue().isEmpty())) {
//                    result = result.replaceAll(
//                            String.format("<%s.%s>", key, dpsAttribute.getCode()),
//                            dpsAttribute.getValue());
//                }
//            }
//        }
//        return result;
//    }
//
//    private Map<String, DpsTemplate> extractTemplates(String content, Map<String, DpsTemplate> templates) {
//
//        Matcher matcher = TEMPLATE_PATTERN.matcher(content);
//
//        while (matcher.find()) {
//            String temp = content.substring(matcher.start() + 1, matcher.end() - 1);
//            String tKey = temp.replaceAll("<[.*[.]]", "");
//            String aCode = temp.replaceAll("[[.].*]>", "");
//            DpsTemplate dpsTemplate;
//            DpsAttribute dpsAttribute = new DpsAttribute().setCode(aCode);
//            if (templates.containsKey(tKey)) {
//                if (templates.get(tKey) != null) {
//                    dpsTemplate = templates.get(tKey);
//                } else dpsTemplate = new DpsTemplate();
//                if (!dpsTemplate.getAttributesForDocParser().contains(dpsAttribute)) {
//                    dpsTemplate.addAttribute(dpsAttribute);
//                }
//            } else dpsTemplate = new DpsTemplate().addAttribute(dpsAttribute);
//            templates.put(tKey, dpsTemplate);
//        }
//        return templates;
//    }
}
