package com.lodny.springmongorealworld.article;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Data;

@Data
@JsonRootName(value = "article")
public class ArticleInsertDto {

  @NotBlank(message = "can't be blank") // " " => X
  private String title;

  @NotBlank(message = "can't be blank") // " " => X
  private String description;

  @NotBlank(message = "can't be blank") // " " => X
  private String body;

  
  private List<String> tagList;
  
}
