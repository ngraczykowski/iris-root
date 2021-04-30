package com.silenteight.adjudication.engine.solve.category;

import org.springframework.data.repository.Repository;

import java.util.Collection;

interface CategoryRepository extends Repository<Category, Long> {

  Collection<Category> findAllByNameIn(Collection<String> names);
}
