package com.silenteight.payments.bridge.datasource.category.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesResponse;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class GetMatchCategoryValuesUseCase {

  private final CategoryDataAccess categoryDataAccess;

  BatchGetMatchCategoryValuesResponse batchGetMatchCategoryValues(
      List<String> matchValuesList) {

    return categoryDataAccess.batchGetMatchCategoryValues(matchValuesList);
  }
}
