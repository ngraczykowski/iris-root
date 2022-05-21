package com.silenteight.hsbc.datasource.category.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class CategoryMatchesDto {

  String category;
  List<String> matches;
}
