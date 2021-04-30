package com.silenteight.adjudication.engine.solve.category;

import com.silenteight.adjudication.engine.testing.RepositoryTestConfiguration;
import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

@ContextConfiguration(classes = {
    RepositoryTestConfiguration.class
})
@Sql
class CategoryRepositoryIT extends BaseDataJpaTest {

  @Autowired
  private CategoryRepository repository;

  @Test
  void nothing() {

  }
}
