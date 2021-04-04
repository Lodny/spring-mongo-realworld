package com.lodny.springmongorealworld.article;

import java.util.List;

import com.lodny.springmongorealworld.exception.InvalidArticleSlugException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {

  @Autowired
  private ArticleRepository repository;

  public Article insert(Article article) {
    return repository.insert(article);
  }

  public List<Article> getAll() {
    return repository.findAll();
  }

  public Article findBySlug(String slug) {
    return repository.findBySlug(slug).orElseThrow(() -> new InvalidArticleSlugException());
  }

  public Article update(Article article) {
    return repository.save(article);
  }

  public void remove(Article article) {
    repository.delete(article);
  }
}
