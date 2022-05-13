package com.silenteight.universaldatasource.app.category.adapter.outgoing.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.datasource.categories.api.v2.CreateCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v2.CreatedCategoryValue;
import com.silenteight.universaldatasource.app.category.model.MatchCategoryRequest;
import com.silenteight.universaldatasource.app.category.port.outgoing.CategoryValueDataAccess;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Repository
class JdbcCategoryValueDataAccess implements CategoryValueDataAccess {

  private final SelectCategoryValueQuery selectCategoryValueQuery;
  private final InsertCategoryValueQuery insertCategoryValueQuery;
  private final DeleteCategoryValueQuery deleteCategoryValueQuery;

  @Override
  @Transactional
  public Collection<CategoryValue> batchGetMatchCategoryValues(
      List<MatchCategoryRequest> matchValuesList) {
    return selectCategoryValueQuery.execute(matchValuesList);
  }

  @Override
  @Transactional
  public List<CreatedCategoryValue> saveAll(List<CreateCategoryValuesRequest> categoryValues) {
    return insertCategoryValueQuery.execute(categoryValues);
  }

  @Override
  @Transactional
  public int delete(List<String> alerts) {
    return deleteCategoryValueQuery.execute(alerts);
  }
}
