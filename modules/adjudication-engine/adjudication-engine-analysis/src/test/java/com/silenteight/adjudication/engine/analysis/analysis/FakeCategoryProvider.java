package com.silenteight.adjudication.engine.analysis.analysis;

import com.silenteight.adjudication.engine.features.category.dto.CategoryDto;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

class FakeCategoryProvider implements CategoryProvider {

  private final AtomicLong idGenerator = new AtomicLong(1);

  @Override
  public List<CategoryDto> getCategories(List<String> categoryNames) {
    return categoryNames
        .stream()
        .map(name -> new CategoryDto(idGenerator.getAndIncrement(), name))
        .collect(Collectors.toUnmodifiableList());
  }
}
