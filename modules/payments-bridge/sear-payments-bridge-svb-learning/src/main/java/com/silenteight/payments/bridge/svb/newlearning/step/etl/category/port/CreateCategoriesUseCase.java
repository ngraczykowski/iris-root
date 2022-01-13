package com.silenteight.payments.bridge.svb.newlearning.step.etl.category.port;

import com.silenteight.datasource.categories.api.v2.CreateCategoryValuesRequest;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;
import com.silenteight.payments.bridge.svb.newlearning.domain.EtlHit;

import java.util.List;

public interface CreateCategoriesUseCase {

  List<CreateCategoryValuesRequest> createCategoryValues(
      List<EtlHit> etlHits, RegisterAlertResponse registerAlert);
}
