package com.example.docapi.service;

import com.example.docapi.controller.dto.AttributeDto;
import com.example.documentsparser.DocService;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
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

    private String wrap(String string) {
        return OPEN_TAG + string + CLOSE_TAG;
    }

    private String unwrap(String string) {
        return string.substring(OPEN_TAG.length(), string.length() - CLOSE_TAG.length());
    }

    public List<AttributeDto> findAttributes(File file) {
        return findAttributes(docService.findByRegex(file, TAG_PATTERN));
    }

    public List<AttributeDto> findAttributes(Set<String> tags) {
        List<AttributeDto> attributeDtos = new ArrayList<>();
        for (String tag : tags) {
            attributeDtos.add(attributeDtoFromTag(tag.substring(OPEN_TAG.length(), tag.length() - CLOSE_TAG.length())));
        }
        attributeDtos = mergeAttributes(attributeDtos);
        return attributeDtos;
    }

    private AttributeDto attributeDtoFromTag(String tag) {
        AttributeDto attributeDto = new AttributeDto();
        if (tag.contains(".")) {
            List<AttributeDto> childes = new ArrayList<>();
            childes.add(attributeDtoFromTag(tag.substring(tag.indexOf('.') + 1).trim()));
            attributeDto
                    .setName(tag.substring(0, tag.indexOf('.')).trim())
                    .setChildes(childes);
        } else {
            attributeDto
                    .setName(tag)
                    .setChildes(new ArrayList<>());
        }
        return attributeDto;
    }

    private static List<AttributeDto> mergeAttributes(List<AttributeDto> attributeDtos) {
        if (attributeDtos == null || attributeDtos.isEmpty()) return new ArrayList<>();
        List<AttributeDto> attributesList = new ArrayList<>(attributeDtos);
        AttributeDto attribute = attributesList.remove(0);
        List<AttributeDto> childes = attribute.getChildes();
        for (AttributeDto attributeDto : attributesList) {
            if (attributeDto.getName().equals(attribute.getName())) {
                childes.addAll(attributeDto.getChildes());
            }
        }
        attribute.setChildes(mergeAttributes(attribute.getChildes()));
        List<AttributeDto> result = mergeAttributes(attributesList.stream()
                .filter(attributeDto -> !attributeDto.getName().equals(attribute.getName()))
                .collect(Collectors.toList()));
        result.add(attribute);
        return result;
    }

    public Map<String, String> attributeDtoToMap(List<AttributeDto> attributeDtos) {
        return attributeDtos.stream()
                .map(ParsingLogic::attributeDtoToMap)
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue));
    }

    public Map<String, String> attributeDtoToMap(AttributeDto attributeDto) {
        Map<String, String> map = new HashMap<>();
        if (attributeDto.getChildes() == null || attributeDto.getChildes().isEmpty()) {
            map.put(OPEN_TAG + attributeDto.getName() + CLOSE_TAG,
                    attributeDto.getValue() == null
                            ? ""
                            : attributeDto.getValue());
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
            map.put(OPEN_TAG + prefix + "." + attributeDto.getName() + CLOSE_TAG,
                    attributeDto.getValue() == null
                            ? ""
                            : attributeDto.getValue());
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

    public void replaceContent(File input, File output, Map<String, String> replaces) {
        docService.replaceContent(input, output, replaces);
    }
}
