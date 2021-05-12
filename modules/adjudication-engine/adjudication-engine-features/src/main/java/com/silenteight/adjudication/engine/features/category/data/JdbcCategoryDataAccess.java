package com.silenteight.adjudication.engine.features.category.data;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.features.category.CategoryDataAccess;

import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Repository
class JdbcCategoryDataAccess implements CategoryDataAccess {

  private final InsertCategoryBatchSqlUpdate insertCategoryBatchSqlUpdate;

  @Override
  public int addCategories(Collection<String> categoryNames) {
    categoryNames.forEach(insertCategoryBatchSqlUpdate::execute);

    var rowsAffected = insertCategoryBatchSqlUpdate.flush();

    return IntStream.of(rowsAffected).sum();
  }
}
