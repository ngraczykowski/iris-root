package com.silenteight.hsbc.datasource.category;

import org.springframework.data.repository.Repository;

import java.util.Collection;

interface CategoryRepository extends Repository<CategoryEntity, Long> {

  Collection<CategoryEntity> findAll();

  CategoryEntity findByName(String name);

  void save(CategoryEntity entity);
}
