package com.lodny.springmongorealworld.comment;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.lodny.springmongorealworld.article.Article;
import com.lodny.springmongorealworld.user.User;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Document("comment")
public class Comment {

  @Id
  private String id;

  private String body;

  //  @DBRef(lazy = true)
  // @DBRef
  private Article article;
  private User author;

  private Date createdAt;
  private Date updatedAt;

  public Comment(String body, Article article, User author) {

    this.body = body;
    this.article = article;
    this.author = author;
  }

  public Object toJSON() {

    Map<String, Object> map = new HashMap<>();
    map.put("id", this.id);
    map.put("body", this.body);
    map.put("createdAt", this.createdAt);
    map.put("updatedAt", this.updatedAt);

    // map.put("article", this.article.toJSON());
    map.put("author", this.author.toProfileJSON(null));

    return map;
  }
}
