package com.silenteight.hsbc.datasource.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.category.dto.CategoryDto;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ListCategoriesUseCase {

  private final CategoryModelHolder categoryModelHolder;

  public List<CategoryDto> getCategories() {

    return categoryModelHolder.getCategories().stream()
        .map(e -> CategoryDto.builder()
            .name(e.getName())
            .displayName(e.getDisplayName())
            .multiValue(e.isMultiValue())
            .allowedValues(e.getAllowedValues())
            .categoryType(e.getType())
            .build())
        .collect(Collectors.toList());
  }
}
