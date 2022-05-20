package com.silenteight.adjudication.engine.features.category.data;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.features.category.CategoryDataAccess;

import org.springframework.stereotype.Repository;

import java.util.Collection;

@RequiredArgsConstructor
@Repository
class JdbcCategoryDataAccess implements CategoryDataAccess {

  private final InsertCategoryBatchSqlUpdate insertCategoryBatchSqlUpdate;

  @Override
  public int addCategories(Collection<String> categoryNames) {
    return insertCategoryBatchSqlUpdate.execute(categoryNames);
  }
}
