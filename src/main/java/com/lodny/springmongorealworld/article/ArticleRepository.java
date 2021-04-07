package com.lodny.springmongorealworld.article;

import java.util.List;
import java.util.Optional;

import com.lodny.springmongorealworld.user.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends MongoRepository<Article, String> {

  public Page<Article> findAll(Pageable pageable);

  public Optional<Article> findBySlug(String slug);

  // @Query(value = "{author: ?0}", sort="{'createdAt': -1}")
  public Page<Article> findByAuthor(User author, Pageable pageable);
  public Page<Article> findByAuthor(String author, Pageable pageable);

  // @Query(value = "{ _id : { $in : ?0 } }")
  // public Page<Article> findById(List<String> articles, Pageable paging);
  public Page<Article> findByIdIn(List<String> articles, Pageable paging);

  public Page<Article> findByAuthorIn(List<String> authors, Pageable paging);

  @Query(value="{}", fields="{ title : 1, _id : 0 }")
  // public List<Map<String, String>> findTitleByTitle(String title);
  public List<String> findTitleByTitle(String title);

  @Query(value="{}", fields="{ tagList : 1, _id : 0 }")
  public List<Article> findTagListByTagList(String string);
  // public List<List<String>> findTagListByTagList(String string);
  // public List<String> findTagListByTagList(String string);

  @Query(value="{ tagList : ?0 }")
  public Page<Article> findByTagList(String tag, Pageable paging);

}
