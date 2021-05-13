package com.silenteight.adjudication.engine.comments.domain;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryTemplateModel {

  @Singular
  private List<String> values;
}
