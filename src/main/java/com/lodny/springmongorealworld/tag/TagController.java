package com.lodny.springmongorealworld.tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.lodny.springmongorealworld.article.Article;
import com.lodny.springmongorealworld.article.ArticleService;
import com.lodny.springmongorealworld.commons.UTIL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class TagController {

  @Autowired
  private ArticleService service;

  // Request URL: http://localhost:8080/api/tags
  @GetMapping("/api/tags")
  public ResponseEntity<?> getAll() {

    List<Article> articles = service.getAllTagList();

    System.out.println("> TagController : getAll() : size : " + articles.size());

    Map<String, Integer> mapTags = new HashMap<>();
    articles.stream().forEach(
      article -> article.getTagList().forEach(
        tag -> Optional.ofNullable(mapTags.get(tag)).ifPresentOrElse(
          value -> mapTags.put(tag, value + 1), () -> mapTags.put(tag, 1))));
        // -> mapTags.put(tag, Optional.ofNullable(mapTags.get(tag)). .ifPresentOrElse(()->mapTags.get(tag) + 1, 1))));

    System.out.println(mapTags.toString());

    List<String> sortedTagList = mapTags
          .entrySet()
          .stream()
          // .sorted(Comparator.comparing(Entry::getValue))
          .sorted((p1, p2) -> p2.getValue().compareTo(p1.getValue()))
          .limit(3)
          .collect(Collectors.mapping(Entry::getKey, Collectors.toList()));

    System.out.println("> sortedTagList" + sortedTagList);


    return new ResponseEntity<>(UTIL.jsonRoot("tags", sortedTagList), HttpStatus.OK);
  }

}
