package com.lodny.springmongorealworld.article;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import com.lodny.springmongorealworld.commons.UTIL;
import com.lodny.springmongorealworld.exception.InvalidAuthenticationException;
import com.lodny.springmongorealworld.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class ArticleController {

  @Autowired
  private ArticleService service;

  // add article
  @PostMapping("/articles")
  public ResponseEntity<?> addArticle(@Valid @RequestBody ArticleInsertDto dto, BindingResult result, ServletRequest request) {

    // Optional<User> user = Optional.ofNullable((User)request.getAttribute("user"));
    if (null == request.getAttribute("user"))
      throw new InvalidAuthenticationException();

    User user = (User)request.getAttribute("user");
    Article article = new Article(dto, user);
    service.insert(article);

    return new ResponseEntity<>(UTIL.jsonRoot("article", article.toJSON(null)), HttpStatus.CREATED);
  }  
  
  // get all articles
  @GetMapping("/articles")
  public ResponseEntity<?> getAll() {

    Object list = service.getAll().stream().map(article -> article.toJSON(null));
    return new ResponseEntity<>(UTIL.jsonRoot("articles", list), HttpStatus.OK);
  }

  // get an article
  @GetMapping("/articles/{slug}")
  public ResponseEntity<?> getArticle(@PathVariable String slug, ServletRequest request) {

    User user = (User)request.getAttribute("user");

    return new ResponseEntity<>(UTIL.jsonRoot("article", service.findBySlug(slug).toJSON(user)), HttpStatus.OK);
  }

  // update article
  @PutMapping("/articles/{slug}")
  public ResponseEntity<?> updateArticle(@Valid @RequestBody ArticleInsertDto dto, @PathVariable String slug, BindingResult result, ServletRequest request) {

    // Optional<User> user = Optional.ofNullable((User)request.getAttribute("user"));
    if (null == request.getAttribute("user"))
      throw new InvalidAuthenticationException();

    // User user = (User)request.getAttribute("user");
    Article article = service.findBySlug(slug);
    article.update(dto);

    return new ResponseEntity<>(UTIL.jsonRoot("article", service.update(article).toJSON(null)), HttpStatus.OK);
  }
  
  // delete article
  @DeleteMapping("/articles/{slug}")
  public ResponseEntity<?> deleteArticle(@PathVariable String slug, ServletRequest request) {
    if (null == request.getAttribute("user"))
      throw new InvalidAuthenticationException();

    Article article = service.findBySlug(slug);
    service.remove(article);

    return new ResponseEntity<>("{}", HttpStatus.OK);  
  }

}
