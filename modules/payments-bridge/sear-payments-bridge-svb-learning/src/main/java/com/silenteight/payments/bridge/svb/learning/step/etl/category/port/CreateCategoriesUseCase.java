package com.silenteight.payments.bridge.svb.learning.step.etl.category.port;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;
import com.silenteight.payments.bridge.datasource.FeatureInputSpecification;
import com.silenteight.payments.bridge.svb.learning.domain.EtlHit;
import com.silenteight.payments.bridge.svb.learning.domain.HitComposite;

import java.util.List;

public interface CreateCategoriesUseCase {

  void createCategoryValues(
      final List<EtlHit> etlHits, final RegisterAlertResponse registerAlert,
      final FeatureInputSpecification featureInputSpecification);

  void createUnstructuredCategoryValues(
      final List<HitComposite> hitComposites, final RegisterAlertResponse registerAlert,
      final FeatureInputSpecification featureInputSpecification);
}
