package com.lodny.springmongorealworld.article;

import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import com.lodny.springmongorealworld.commons.UTIL;
import com.lodny.springmongorealworld.exception.InvalidAuthenticationException;
import com.lodny.springmongorealworld.user.User;
import com.lodny.springmongorealworld.user.UserService;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/articles")
@CrossOrigin(origins = "http://localhost:3000")
public class ArticleController {

  @Autowired
  private ArticleService service;

  @Autowired
  private UserService userService;

  // add article
  @PostMapping("")
  public ResponseEntity<?> addArticle(@Valid @RequestBody ArticleInsertDto dto, BindingResult result, ServletRequest request) {

    // Optional<User> user = Optional.ofNullable((User)request.getAttribute("user"));
    User user = (User)request.getAttribute("user");
    if (user == null) throw new InvalidAuthenticationException();

    Article article = new Article(dto, user);
    article = service.insert(article);

    return new ResponseEntity<>(UTIL.jsonRoot("article", article.toJSON(user)), HttpStatus.CREATED);
  }

  // get all articles
  // Your Feed :  Request URL: http://localhost:8080/api/articles/feed?limit=10&offset=0
  // My Article:  Request URL: http://localhost:8080/api/articles?author=333&limit=5&offset=0

  @GetMapping("")
  public ResponseEntity<?> getAll(@RequestParam String author, ServletRequest request) {

    System.out.println("> ArticleController : getAll() : author : " + author);

    User user = (User)request.getAttribute("user");

    System.out.println("> get all articles : ");
    Object list = service.getAll()
        .stream()
        .map(article -> article.toJSON(user))
        .collect(Collectors.toList());

    return new ResponseEntity<>(UTIL.jsonRoot("articles", list), HttpStatus.OK);
  }

  // get an article
  // token : optional
  @GetMapping("/{slug}")
  public ResponseEntity<?> getArticle(@PathVariable String slug, ServletRequest request) {

    User user = (User)request.getAttribute("user");

    return new ResponseEntity<>(UTIL.jsonRoot("article", service.findBySlug(slug).toJSON(user)), HttpStatus.OK);
  }

  // update article
  @PutMapping("/{slug}")
  public ResponseEntity<?> updateArticle(@Valid @RequestBody ArticleInsertDto dto, @PathVariable String slug, BindingResult result, ServletRequest request) {

    // Optional<User> user = Optional.ofNullable((User)request.getAttribute("user"));
    User user = (User)request.getAttribute("user");
    if (user == null) throw new InvalidAuthenticationException();

    Article article = service.findBySlug(slug);
    article.update(dto);

    return new ResponseEntity<>(UTIL.jsonRoot("article", service.update(article).toJSON(null)), HttpStatus.OK);
  }

  // delete article
  @DeleteMapping("/{slug}")
  public ResponseEntity<?> deleteArticle(@PathVariable String slug, ServletRequest request) {

    if (null == request.getAttribute("user"))
      throw new InvalidAuthenticationException();

    Article article = service.findBySlug(slug);
    service.remove(article);

    return new ResponseEntity<>("{}", HttpStatus.OK);
  }

  @PostMapping("/{slug}/favorite")
  public ResponseEntity<?> favorite(@PathVariable String slug, ServletRequest request) {

    User user = (User)request.getAttribute("user");
    if (null == user) throw new InvalidAuthenticationException();

    Article article = service.findBySlug(slug);

    final Boolean favorite = user.favorite(article);
    article.setFavoritesCount(favorite);

    service.update(article);
    userService.update(user);

    return new ResponseEntity<>(UTIL.jsonRoot("article", article.toJSON(user)), HttpStatus.OK);
  }

}
