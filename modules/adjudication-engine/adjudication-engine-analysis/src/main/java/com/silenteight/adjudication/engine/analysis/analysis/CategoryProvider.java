package com.silenteight.adjudication.engine.analysis.analysis;

import com.silenteight.adjudication.engine.features.category.dto.CategoryDto;

import java.util.List;

public interface CategoryProvider {

  List<CategoryDto> getCategories(List<String> categoryNames);
}
