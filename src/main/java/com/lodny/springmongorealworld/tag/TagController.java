package com.lodny.springmongorealworld.tag;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.lodny.springmongorealworld.article.Article;
import com.lodny.springmongorealworld.article.ArticleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class TagController {

  @Autowired
  private ArticleService service;

  // Request URL: http://localhost:8080/api/tags
  @GetMapping("/api/tags")
  public ResponseEntity<?> getAll() {

    List<Article> tagListList = service.getAllTagList();

    System.out.println("> TagController : getAll() : size : " + tagListList.size());

    Map<String, Integer> mapTags = new HashMap<>();
    tagListList.stream().forEach(article
      -> article.getTagList().forEach(tag
        -> mapTags.put(tag, Optional.ofNullable(mapTags.get(tag)).orElse(1))));

    System.out.println(mapTags.toString());

    // limit(3)
    // List<String> sortedTagList = mapTags.entrySet().stream().sorted((a, b) -> a.getValue().compareTo(b.getValue())).collect(Collectors.toList()).;
    List<String> sortedTagList = mapTags.entrySet().stream().sorted(Comparator.comparing(Entry::getValue)).limit(3).collect(Collectors.toCollection(e -> e.getKey());
    System.out.println("> sortedTagList" + sortedTagList);


    return new ResponseEntity<>(UTIL.jsonRoot("tags", sortedTagList), HttpStatus.OK);
    // return new ResponseEntity<>("'tags': ['reactjs', 'angularjs']", HttpStatus.OK);
    // return new ResponseEntity<>("{ \"tags\": [\"reactjs\", \"angularjs\"] }", HttpStatus.OK);
  }

}
