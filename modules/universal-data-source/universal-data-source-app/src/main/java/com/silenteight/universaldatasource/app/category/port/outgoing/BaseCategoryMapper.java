package com.silenteight.universaldatasource.app.category.port.outgoing;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.Category;
import com.silenteight.universaldatasource.app.category.model.CategoryDto;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
class BaseCategoryMapper implements CategoryMapper {

  public List<CategoryDto> mapCategories(List<Category> categories) {

    return categories.stream()
        .map(category -> CategoryDto.builder()
            .name(category.getName())
            .displayName(category.getDisplayName())
            .categoryType(category.getType().toString())
            .allowedValues(category.getAllowedValuesList())
            .multiValue(category.getMultiValue())
            .build()
        ).collect(Collectors.toList());
  }
}
