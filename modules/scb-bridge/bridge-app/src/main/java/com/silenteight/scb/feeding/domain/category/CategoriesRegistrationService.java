package com.silenteight.scb.feeding.domain.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.feeding.domain.port.outgoing.UniversalDatasourceApiClient;
import com.silenteight.universaldatasource.api.library.category.v2.BatchCreateCategoriesIn;
import com.silenteight.universaldatasource.api.library.category.v2.CategoryShared;

import java.util.List;
import javax.annotation.PostConstruct;

@RequiredArgsConstructor
public class CategoriesRegistrationService {

  private final UniversalDatasourceApiClient universalDatasourceApiClient;
  private final List<CategoryShared> categories;

  @PostConstruct
  void registerCategoriesInUds() {
    universalDatasourceApiClient.registerCategories(batchCreateCategoriesIn());
  }

  private BatchCreateCategoriesIn batchCreateCategoriesIn() {
    return BatchCreateCategoriesIn.builder()
        .categories(categories)
        .build();
  }
}
