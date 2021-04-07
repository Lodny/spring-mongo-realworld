package com.lodny.springmongorealworld.article;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.github.slugify.Slugify;
import com.lodny.springmongorealworld.user.User;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;

// @Getter
@NoArgsConstructor
@Document("article")
public class Article {
  @Id
  private String id;

  private String title;
  private String description;
  private String body;
  private List<String> tagList;

  private Date createdAt;
  private Date updatedAt;

  private Boolean favorited;
  private int favoritesCount;

  private String slug;

  // @JsonProperty("author")
  // @DBRef
  @DBRef(lazy = true)
  private User author;

  public Article(ArticleInsertDto dto, User user) {
    this.title = dto.getTitle();
    this.description = dto.getDescription();
    this.body = dto.getBody();
    this.tagList = dto.getTagList();

    this.createdAt = new Date();
    this.updatedAt = new Date();

    this.favorited = false;
    this.favoritesCount = 0;

    this.slug = getSlugURL(this.title);
    this.author = user;
  }

  public void update(ArticleInsertDto dto) {

    Optional.ofNullable(dto.getTitle()).ifPresent(str -> this.title = str.isBlank() ? this.title : str);
    Optional.ofNullable(dto.getDescription()).ifPresent(str -> this.description = str.isBlank() ? this.description : str);
    Optional.ofNullable(dto.getBody()).ifPresent(str -> this.body = str.isBlank() ? this.body : str);
    Optional.ofNullable(dto.getTagList()).ifPresent(list -> this.tagList = list);

    // String value = dto.getTitle();
    // if (null != value && !value.isBlank())
    //   this.title = value;

    // value = dto.getDescription();
    // if (null != value && !value.isBlank())
    //   this.description = value;

    // value = dto.getBody();
    // if (null != value && !value.isBlank())
    //   this.body = value;

    // List<String> list = dto.getTagList();
    // if (null != list)
    //   this.tagList = list;

    this.updatedAt = new Date();
  }

  public Map<String, Object> toJSON(User favoritedUser) {

    Map<String, Object> map = new HashMap<>();
    map.put("title", this.title);
    map.put("slug", this.slug);
    map.put("description", this.description);
    map.put("body", this.body);
    map.put("createdAt", this.createdAt);
    map.put("updatedAt", this.updatedAt);
    map.put("tagList", this.tagList);

    Optional.ofNullable(favoritedUser).ifPresentOrElse(
      user -> map.put("favorited", user.isFavorite(this.id)),
      () -> map.put("favorited", false));

    map.put("favoritesCount", this.favoritesCount);
    map.put("author", author.toProfileJSON(favoritedUser));

    return map;
  }

  public String getSlugURL(String str) {

    String slug = new Slugify().slugify(str);
    String postfix = Long.toHexString(Double.doubleToLongBits(Math.random())).substring(10);

    return slug + "-" + postfix;
  }

  public void setFavoritesCount(Boolean add) {
    this.favoritesCount += add ? +1 : -1;
  }

  public String getId() {
    return id;
  }

  public List<String> getTagList() {
    return tagList;
  }

}
