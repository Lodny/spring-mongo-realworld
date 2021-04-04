package com.lodny.springmongorealworld.comment;

import java.util.List;

import com.lodny.springmongorealworld.article.Article;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {

  List<Comment> findByArticle(Article article); 

}
