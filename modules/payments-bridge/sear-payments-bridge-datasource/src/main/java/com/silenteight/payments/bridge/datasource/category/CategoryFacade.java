package com.silenteight.payments.bridge.datasource.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesResponse;
import com.silenteight.datasource.categories.api.v1.ListCategoriesResponse;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryFacade {

  private final AvailableCategoriesProvider availableCategoriesProvider;

  private final GetMatchCategoryValuesUseCase getMatchCategoryValuesUseCase;

  public ListCategoriesResponse listCategories() {
    return availableCategoriesProvider.getAvailableCategories();
  }

  public BatchGetMatchCategoryValuesResponse batchGetMatchCategoryValues(
      List<String> matchValuesList) {

    return getMatchCategoryValuesUseCase.batchGetMatchCategoryValues(matchValuesList);
  }
}
