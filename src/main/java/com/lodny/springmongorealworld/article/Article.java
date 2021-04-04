package com.lodny.springmongorealworld.article;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.slugify.Slugify;
import com.lodny.springmongorealworld.user.User;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Document("article")
public class Article {
  @Id
  private String id;

  private String title;
  private String description;
  private String body;
  private List<String> tagList;

  @CreatedDate
  private Date createdAt;
  @LastModifiedDate
  private Date updatedAt;

  private Boolean favorited;
  private int favoritesCount;

  private String slug;
  
  // @JsonProperty("author")
  private User author;

  public Article(ArticleInsertDto dto, User user) {
    this.title = dto.getTitle();
    this.description = dto.getDescription();
    this.body = dto.getBody();
    this.tagList = dto.getTagList();

    // this.createdAt = new Date();
    // this.updatedAt = new Date();
    this.favorited = false;
    this.favoritesCount = 0;

    this.slug = getSlugURL(this.title);
    this.author = user;
  }

  public void update(ArticleInsertDto dto) {

    String value = dto.getTitle();
    if (null != value && !value.isBlank())
      this.title = value;
      
    value = dto.getDescription();
    if (null != value && !value.isBlank())
      this.description = value;

    value = dto.getBody();
    if (null != value && !value.isBlank())
      this.body = value;

    List<String> list = dto.getTagList();
    if (null != list)
      this.tagList = list;

    // Optional.ofNullable(dto.getUsername()).filter(o -> !o.get().isBlank()).ifPresent(op -> this.username = op.get());
    // Optional.ofNullable(dto.getEmail()).filter(o -> !o.get().isBlank()).ifPresent(op -> this.email = op.get());
    // Optional.ofNullable(dto.getBio()).filter(o -> !o.get().isBlank()).ifPresent(op -> this.bio = op.get());
    // Optional.ofNullable(dto.getImage()).filter(o -> !o.get().isBlank()).ifPresent(op -> this.image = op.get());
    // Optional.ofNullable(dto.getBio()).filter(o -> !o.get().isBlank()).ifPresent(
    //   op -> this.password = (new BCryptPasswordEncoder()).encode(op.get())
    // );
  }

  public Map<String, Object> toJSON(User user) {
    
    Map<String, Object> map = new HashMap<>();    
    map.put("title", this.title);
    map.put("slug", this.slug);    
    map.put("body", this.body);
    map.put("description", this.description);
    map.put("createdAt", this.createdAt);
    map.put("updatedAt", this.updatedAt);
    map.put("tagList", this.tagList);
    
    map.put("favorited", false);
    map.put("favoritesCount", 0);

    map.put("author", author.toProfileJSON(user));

    return map;
  }

  public String getSlugURL(String str) {

    String slug = new Slugify().slugify(str);
    String postfix = Long.toHexString(Double.doubleToLongBits(Math.random())).substring(10);

    return slug + "-" + postfix;
  }

  // public Article(User user, String title, String description, String body, List<String> tagList) {
  //   this.title = title;
  //   this.description = description;
  //   this.body = body;
  //   this.tagList = tagList;
  //   this.createdAt = new Date();
  //   this.updatedAt = new Date();
  //   this.favorited = false;
  //   this.favoritesCount = 0;

  //   this.slug = new Slugify().slugify(title);
  //   this.author = user;
  // }
  
}
