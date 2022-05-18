package com.silenteight.payments.bridge.svb.learning.step.reetl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.port.FindRegisteredAlertUseCase;
import com.silenteight.payments.bridge.datasource.FeatureInputSpecification;
import com.silenteight.payments.bridge.svb.learning.step.etl.IngestDatasourceService;

import java.util.List;
import javax.annotation.Nonnull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ReEtlAlertProcessorFactory {

  @Nonnull
  static ReEtlAlertProcessor create(
      final FindRegisteredAlertUseCase findRegisteredAlertUseCase,
      final List<String> determineFeatureInputList,
      final IngestDatasourceService ingestDatasourceService
  ) {

    final FeatureInputSpecification featureInputSpecification =
        FeatureInputSpecificationStrategy.INSTANCE.chooseSpecification(determineFeatureInputList);

    return new ReEtlAlertProcessor(
        findRegisteredAlertUseCase,
        featureInputSpecification,
        ingestDatasourceService
    );
  }
}
