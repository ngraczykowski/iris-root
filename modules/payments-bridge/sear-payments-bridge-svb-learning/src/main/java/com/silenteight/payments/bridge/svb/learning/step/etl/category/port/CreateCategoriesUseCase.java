package com.silenteight.payments.bridge.svb.learning.step.etl.category.port;

import com.silenteight.datasource.categories.api.v2.CreateCategoryValuesRequest;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;
import com.silenteight.payments.bridge.svb.learning.domain.EtlHit;
import com.silenteight.payments.bridge.svb.learning.domain.HitComposite;
import com.silenteight.payments.bridge.svb.learning.step.etl.feature.service.FeatureInputSpecification;

import java.util.List;

public interface CreateCategoriesUseCase {

  List<CreateCategoryValuesRequest> createCategoryValues(
      final List<EtlHit> etlHits, final RegisterAlertResponse registerAlert,
      final FeatureInputSpecification featureInputSpecification);

  List<CreateCategoryValuesRequest> createCategoryValues(
      List<EtlHit> etlHits, RegisterAlertResponse registerAlert);

  List<CreateCategoryValuesRequest> createUnstructuredCategoryValues(
      List<HitComposite> hitComposites, RegisterAlertResponse registerAlert);

  List<CreateCategoryValuesRequest> createUnstructuredCategoryValues(
      final List<HitComposite> hitComposites, final RegisterAlertResponse registerAlert,
      final FeatureInputSpecification featureInputSpecification);
}
