package com.lodny.springmongorealworld.article;

import java.util.List;
import java.util.stream.Collectors;

import com.lodny.springmongorealworld.exception.InvalidArticleSlugException;
import com.lodny.springmongorealworld.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {

  @Autowired
  private ArticleRepository repository;

  @Autowired
  MongoOperations operations;

  public Article insert(Article article) {
    return repository.insert(article);
  }

  public List<Article> findAll() {
    return repository.findAll();
  }

  // // using MongoTemplate - 다음에 해보장...
  // public List<Article> getAll(User author, int limit, int offset) {

  //   System.out.println("> ArticleService : getAll() : using MongoTemplate");

  //   Query query = new Query();
  //   if (null != author)
  //     query.addCriteria(Criteria.where("author").is(author));
  //   query.skip(offset);
  //   query.limit(limit);
  //   // query.with(sort)

  //   return operations.find(query, Article.class);
  // }

  public Page<Article> findAll(int limit, int offset) {

    System.out.println("> ArticleService : findAll() : using repository");

    // Pageable paging = PageRequest.of(offset/10, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
    Pageable paging = PageRequest.of(offset/10, limit, Sort.Direction.DESC, "createdAt");
    return repository.findAll(paging);
  }

  public Page<Article> findByAuthor(User author, int limit, int offset) {

    System.out.println("> ArticleService : findByAuthor() : using repository : " + author.getUsername());

    Pageable paging = PageRequest.of(offset/10, limit, Sort.Direction.DESC, "createdAt");
    // return repository.findByAuthor(author, paging);
    return repository.findByAuthor(author.getId(), paging);
  }

  public Page<Article> findByFavorites(List<Article> articles, int limit, int offset) {

    System.out.println("> ArticleService : findByFavorites() : using repository");

    Pageable paging = PageRequest.of(offset/10, limit, Sort.Direction.DESC, "createdAt");

    // List<String> list = articles.stream().map(article -> article.getId()).collect(Collectors.toList());
    List<String> list = articles.stream().map(Article::getId).collect(Collectors.toList());
    return repository.findByIdIn(list, paging);
  }

  // public Streamable<Article> findByAuthorIn(List<User> following, int limit, int offset) {
  public Page<Article> findByAuthorIn(List<User> following, int limit, int offset) {

    System.out.println("> ArticleService : findByAuthorIn() : using repository");

    Pageable paging = PageRequest.of(offset/10, limit, Sort.Direction.DESC, "createdAt");
    List<String> list = following.stream().map(User::getId).collect(Collectors.toList());

    return repository.findByAuthorIn(list, paging);
  }

  public Page<Article> findByTag(String tag, int limit, int offset) {
    Pageable paging = PageRequest.of(offset/10, limit, Sort.Direction.DESC, "createdAt");
    return repository.findByTagList(tag, paging);
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

  // public List<Map<String, String>> getAllTagList() {
  public List<Article> getAllTagList() {
    // return repository.findAllTitle();
    // return repository.findTitleByTitle("");
    return repository.findTagListByTagList("");
  }

}
