package com.lodny.springmongorealworld.article;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends MongoRepository<Article, String> {
  public Optional<Article> findBySlug(String slug);
}
