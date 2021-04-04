package com.lodny.springmongorealworld.comment;

import java.util.List;

import com.lodny.springmongorealworld.article.Article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

  @Autowired
  private CommentRepository repository;

  public Comment insert(Comment comment) {
    return repository.insert(comment);
  }

  public List<Comment> findByArticle(Article article) {
    return repository.findByArticle(article);
  }

  public void deleteById(String id) {
    repository.deleteById(id);
  }
  
}
