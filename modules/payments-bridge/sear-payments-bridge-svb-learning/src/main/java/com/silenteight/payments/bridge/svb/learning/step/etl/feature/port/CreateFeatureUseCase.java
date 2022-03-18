package com.silenteight.payments.bridge.svb.learning.step.etl.feature.port;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;
import com.silenteight.payments.bridge.datasource.FeatureInputSpecification;
import com.silenteight.payments.bridge.svb.learning.domain.EtlHit;
import com.silenteight.payments.bridge.svb.learning.domain.HitComposite;

import java.util.List;

public interface CreateFeatureUseCase {

  List<AgentInput> createFeatureInputs(
      List<EtlHit> etlHits, RegisterAlertResponse registerAlert);

  List<AgentInput> createFeatureInputs(
      final List<EtlHit> etlHits, final RegisterAlertResponse registerAlert,
      final FeatureInputSpecification featureInputSpecification);

  List<AgentInput> createUnstructuredFeatureInputs(
      final List<HitComposite> hitComposites, final RegisterAlertResponse registerAlert
  );

  List<AgentInput> createUnstructuredFeatureInputs(
      final List<HitComposite> hitComposites, final RegisterAlertResponse registerAlert,
      final FeatureInputSpecification featureInputSpecification
  );
}
