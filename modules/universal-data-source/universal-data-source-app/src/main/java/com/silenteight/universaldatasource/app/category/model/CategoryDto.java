package com.silenteight.universaldatasource.app.category.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CategoryDto {
  private String name;
  private String displayName;
  private String categoryType;
  private List<String> allowedValues;
  private boolean multiValue;
}
