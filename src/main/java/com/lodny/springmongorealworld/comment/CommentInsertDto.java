package com.lodny.springmongorealworld.comment;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Data;

@Data
@JsonRootName(value = "comment")
public class CommentInsertDto {
  
  @NotBlank(message = "can't be blank") // " " => X
  private String body;
}
