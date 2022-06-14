package com.silenteight.adjudication.engine.comments.comment.dto;

import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
public class CommentTemplateDto {

  @NotNull
  private String templateName;

  @NotNull
  private String template;

}
