package com.silenteight.universaldatasource.app.category.adapter.outgoing.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.Category;
import com.silenteight.universaldatasource.app.category.port.outgoing.CategoryDataAccess;

import org.springframework.stereotype.Repository;

import java.util.List;
import javax.transaction.Transactional;

@RequiredArgsConstructor
@Repository
public class JdbcCategoryDataAccess implements CategoryDataAccess {

  private final SelectCategoryQuery selectCategoryQuery;
  private final InsertCategoriesQuery insertCategoriesQuery;

  @Override
  public List<Category> getAllCategories() {
    return selectCategoryQuery.execute();
  }

  @Override
  @Transactional
  public List<Category> saveAll(List<Category> categoriesList) {
    return insertCategoriesQuery.execute(categoriesList);
  }
}
