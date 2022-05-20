package com.silenteight.adjudication.engine.features.category;

import com.silenteight.adjudication.engine.testing.RepositoryTestConfiguration;
import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(classes = {
    RepositoryTestConfiguration.class
})
@Sql
class CategoryRepositoryIT extends BaseDataJpaTest {

  private static final String CATEGORIES_CUSTOMER_TYPE = "categories/customerType";
  private static final String CATEGORIES_HIT_TYPE = "categories/hitType";

  @Autowired
  private CategoryRepository repository;

  @Test
  void findsCategoriesByNames() {
    var found =
        repository.findAllByNameIn(List.of(CATEGORIES_CUSTOMER_TYPE, CATEGORIES_HIT_TYPE));

    assertThat(found)
        .hasSize(2)
        .anyMatch(category -> category.getName().equals(CATEGORIES_CUSTOMER_TYPE))
        .anyMatch(category -> category.getName().equals(CATEGORIES_HIT_TYPE));
  }

  @Test
  void doesNotFindOtherCategory() {
    var found = repository.findAllByNameIn(List.of("bogus name"));

    assertThat(found).isEmpty();
  }
}
