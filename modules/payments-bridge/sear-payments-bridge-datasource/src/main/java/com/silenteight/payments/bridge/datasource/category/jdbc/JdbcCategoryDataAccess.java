package com.silenteight.payments.bridge.datasource.category.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesResponse;
import com.silenteight.payments.bridge.datasource.category.CategoryDataAccess;

import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
class JdbcCategoryDataAccess implements CategoryDataAccess {

  private final SelectCategoryQuery selectCategoryQuery;

  @Override
  public BatchGetMatchCategoryValuesResponse batchGetMatchCategoryValues(
      List<String> matchValuesList) {
    return selectCategoryQuery.execute(matchValuesList);
  }
}
