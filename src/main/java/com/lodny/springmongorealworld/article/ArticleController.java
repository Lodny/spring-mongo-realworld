package com.lodny.springmongorealworld.article;

import java.util.stream.Collectors;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import com.lodny.springmongorealworld.commons.UTIL;
import com.lodny.springmongorealworld.exception.InvalidAuthenticationException;
import com.lodny.springmongorealworld.user.User;
import com.lodny.springmongorealworld.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    // My Article:  Request URL: http://localhost:8080/api/articles?author=333&limit=5&offset=0
  // Favorited:   Request URL: http://localhost:8080/api/articles?favorited=111&limit=5&offset=0
  // Tag ??
  @GetMapping("")
  public ResponseEntity<?> findAll(
    @RequestParam(required = false) String author,
    @RequestParam(defaultValue = "10") int limit,
    @RequestParam(defaultValue = "0") int offset,
    @RequestParam(required = false) String favorited,
    ServletRequest request) {

    System.out.println("> ArticleController : findAll() : author : " + author);
    System.out.println("> ArticleController : findAll() : favorited : " + favorited);
    System.out.println("> ArticleController : findAll() : limit : " + limit);
    System.out.println("> ArticleController : findAll() : offset : " + offset);

    User user = (User)request.getAttribute("user");

    Page<Article> list = null;
    if (null != author) {
      User userAuthor = userService.findByUsername(author);
      list = service.findByAuthor(userAuthor, limit, offset);
    }
    else if (null != favorited) {
      User favoritedAuthor = userService.findByUsername(favorited);
      list = service.findByFavorites(favoritedAuthor.getFavorites(), limit, offset);
    }
    else {
      list = service.findAll(limit, offset);
    }

    Object listObj = list
        .stream()
        .map(article -> article.toJSON(user))
        .collect(Collectors.toList());

    return new ResponseEntity<>(UTIL.jsonRoot("articles", listObj), HttpStatus.OK);
  }

  // Your Feed :  Request URL: http://localhost:8080/api/articles/feed?limit=10&offset=0
  @GetMapping("/feed")
  public ResponseEntity<?> getFeedAll(
    @RequestParam(defaultValue = "5") int limit,
    @RequestParam(defaultValue = "0") int offset,
    ServletRequest request) {

    User user = (User)request.getAttribute("user");
    if (user == null) throw new InvalidAuthenticationException();

    System.out.println("> ArticleController : getFeedAll() : limit : " + limit);
    System.out.println("> ArticleController : getFeedAll() : offset : " + offset);

    Object list = service.findByAuthorIn(user.getFollowing(), limit, offset)
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
  public ResponseEntity<?> updateArticle(@Valid @RequestBody ArticleInsertDto dto, BindingResult result, @PathVariable String slug, ServletRequest request) {

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
