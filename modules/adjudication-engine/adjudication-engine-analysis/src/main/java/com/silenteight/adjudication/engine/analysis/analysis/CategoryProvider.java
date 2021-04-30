package com.silenteight.adjudication.engine.analysis.analysis;

import com.silenteight.adjudication.engine.solve.category.dto.CategoryDto;

import java.util.List;

public interface CategoryProvider {

  List<CategoryDto> getCategories(List<String> categoryNames);
}
