package com.lodny.springmongorealworld.comment;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import com.lodny.springmongorealworld.article.Article;
import com.lodny.springmongorealworld.article.ArticleService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/articles")
@CrossOrigin(origins = "http://localhost:3000")
public class CommentController {

  @Autowired
  private CommentService commentService;

  @Autowired
  private ArticleService articleService;
  
  // add comment
  @PostMapping("/{slug}/comments")
  public ResponseEntity<?> addComment(@Valid @RequestBody CommentInsertDto dto, BindingResult result, @PathVariable String slug, ServletRequest request) {

    User user = (User) request.getAttribute("user");
    if (null == user)
      throw new InvalidAuthenticationException();

    Article article = articleService.findBySlug(slug);
    Comment comment = new Comment(dto.getBody(), article, user);    

    return new ResponseEntity<>(UTIL.jsonRoot("comment", commentService.insert(comment).toJSON()), HttpStatus.CREATED);
  }

  // get all comments of slug article
  @GetMapping("/{slug}/comments")
  public ResponseEntity<?> findAll(@PathVariable String slug) {

    // User user = (User) request.getAttribute("user");
    // if (null == user)
    //   throw new InvalidAuthenticationException();

    Article article = articleService.findBySlug(slug);
    Object list = commentService.findByArticle(article).stream().map(comment -> comment.toJSON());

    return new ResponseEntity<>(UTIL.jsonRoot("comments", list), HttpStatus.OK);
  }

  // delete a comment
  @DeleteMapping("/{slug}/comments/{id}")
  public ResponseEntity<?> deleteComment(@PathVariable String slug, @PathVariable String id, ServletRequest request) {

    System.out.println("> deleteComment : slug : " + slug + " : id : " + id);

    User user = (User) request.getAttribute("user");
    if (null == user)
      throw new InvalidAuthenticationException();

    articleService.findBySlug(slug);    
    commentService.deleteById(id);

    return new ResponseEntity<>("success : delete", HttpStatus.OK);
  }
}
