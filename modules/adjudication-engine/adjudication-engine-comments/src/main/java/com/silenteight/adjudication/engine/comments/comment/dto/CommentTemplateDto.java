package com.silenteight.adjudication.engine.comments.comment.dto;

import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Value
public class CommentTemplateDto {

  @NotNull
  private String templateName;

  @Min(1)
  @NotNull
  private Integer revision;

  @NotNull
  private String template;

}
