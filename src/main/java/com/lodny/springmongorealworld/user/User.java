package com.lodny.springmongorealworld.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;

import com.lodny.springmongorealworld.article.Article;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@NoArgsConstructor
@Document("user")
@ToString
public class User {

  @Id
  private String id;

  @Indexed(unique = true)
  private String username;

  @Indexed(unique = true)
  private String email;

  private String password;
  private String bio;
  private String image;

  private Date createdAt;
  // @Field("updated_at")
  private Date updatedAt;

  @DBRef
  List<User> following;
  List<Article> favorites;

  public User(UserRegisterDto dto) {

    this.username = dto.getUsername();
    this.email = dto.getEmail();
    this.password = (new BCryptPasswordEncoder()).encode(dto.getPassword());

    this.following = new ArrayList<>();
    this.favorites = new ArrayList<>();

    this.createdAt = new Date();
    this.updatedAt = new Date();
  }

  public void setImage(String default_image) {
    this.image = default_image;
  }

  public void follow(User followingUser) {
    // int preSize = this.following.size();
    // if (null == this.following)
    //   this.following = new ArrayList<>();

    Boolean isExist = this.following.stream().anyMatch(user -> user.getId().equals(followingUser.getId()));
    if (isExist)
      this.following = this.following.stream().filter(user -> !user.getId().equals(followingUser.getId())).collect(Collectors.toList());
    else
      this.following.add(followingUser);
  }

  public Boolean favorite(Article favoriteArticle) {
    // int preSize = this.following.size();
    // if (null == this.favorites)
    //   this.favorites = new ArrayList<>();

    Boolean already = this.favorites.stream().anyMatch(article -> article.getId().equals(favoriteArticle.getId()));
    if (already)
      this.favorites = this.favorites.stream().filter(article -> !article.getId().equals(favoriteArticle.getId())).collect(Collectors.toList());
    else
      this.favorites.add(favoriteArticle);

    return !already;
  }

  public void update(UserUpdateDto dto) {

    String value = dto.getUsername();
    if (null != value && !value.isBlank())
      this.username = value;

    value = dto.getEmail();
    if (null != value && !value.isBlank())
      this.email = value;

    // value = dto.getBio();
    // if (null != value && !value.isBlank())
    //   this.bio = value;
    Optional.ofNullable(dto.getBio()).ifPresent((@NotBlank var val) -> this.bio = val);
    // Optional.ofNullable(dto.getBio()).ifPresent((@NotBlank var val) -> this.bio = val);

    value = dto.getImage();
    if (null != value && !value.isBlank())
      this.image = value;

    value = dto.getPassword();
    if (null != value && !value.isBlank())
      this.password = (new BCryptPasswordEncoder()).encode(value);

    this.updatedAt = new Date();

    // Optional.ofNullable(dto.getUsername()).filter(o -> !o.get().isBlank()).ifPresent(op -> this.username = op.get());
    // Optional.ofNullable(dto.getEmail()).filter(o -> !o.get().isBlank()).ifPresent(op -> this.email = op.get());
    // Optional.ofNullable(dto.getBio()).filter(o -> !o.get().isBlank()).ifPresent(op -> this.bio = op.get());
    // Optional.ofNullable(dto.getImage()).filter(o -> !o.get().isBlank()).ifPresent(op -> this.image = op.get());
    // Optional.ofNullable(dto.getBio()).filter(o -> !o.get().isBlank()).ifPresent(
    //   op -> this.password = (new BCryptPasswordEncoder()).encode(op.get())
    // );
  }

  public Map<String, Object> toAuthJSON(Optional<String> token) {

    Map<String, Object> map = new HashMap<>();
    map.put("username", this.username);
    map.put("email", this.email);
    token.ifPresent(value -> map.put("token", value));
    map.put("bio", this.bio);
    map.put("image", this.image);
    map.put("createdAt", this.createdAt);
    map.put("updatedAt", this.updatedAt);
    map.put("following", this.following);
    map.put("favorites", this.favorites);

    return map;
  }

  public Map<String, Object> toProfileJSON(User follower) {

    Map<String, Object> map = new HashMap<>();
    map.put("username", this.username);
    map.put("bio", this.bio);
    map.put("image", this.image);

    // Boolean follow = false;
    // if (null != follower && null != follower.getFollowing())
    //   follow = follower.getFollowing().stream().anyMatch(user -> user.getId().equals(getId()));
    // map.put("following", follow);

    map.put("following", false);
    Optional.ofNullable(follower).ifPresent(f -> {
      Optional.ofNullable(f.getFollowing()).ifPresent(list ->
        map.put("following", list.stream().anyMatch(user -> user.getId().equals(getId())))
      );
    });

    return map;
  }

  public boolean checkPassword(String password) {
    return (new BCryptPasswordEncoder()).matches(password, this.password);
  }

  public Boolean isFavorite(String userId) {
    return this.favorites.stream().anyMatch(article -> article.getId().equals(userId));
  }

}