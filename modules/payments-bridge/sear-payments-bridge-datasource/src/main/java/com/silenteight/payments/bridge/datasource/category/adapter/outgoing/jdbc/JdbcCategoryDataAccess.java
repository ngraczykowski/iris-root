package com.silenteight.payments.bridge.datasource.category.adapter.outgoing.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v1.CategoryValue;
import com.silenteight.payments.bridge.datasource.category.model.MatchCategoryRequest;
import com.silenteight.payments.bridge.datasource.category.model.MatchCategoryValue;
import com.silenteight.payments.bridge.datasource.category.port.outgoing.CategoryDataAccess;

import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import javax.transaction.Transactional;


@Repository
@RequiredArgsConstructor
class JdbcCategoryDataAccess implements CategoryDataAccess {

  private final SelectCategoryQuery selectCategoryQuery;
  private final InsertCategoryValueQuery insertCategoryValueQuery;

  @Override
  @Transactional
  public Collection<CategoryValue> batchGetMatchCategoryValues(
      List<MatchCategoryRequest> matchValuesList) {
    return selectCategoryQuery.execute(matchValuesList);
  }

  @Override
  @Transactional
  public void saveAll(
      List<MatchCategoryValue> categoryValues) {
    insertCategoryValueQuery.execute(categoryValues);
  }
}
