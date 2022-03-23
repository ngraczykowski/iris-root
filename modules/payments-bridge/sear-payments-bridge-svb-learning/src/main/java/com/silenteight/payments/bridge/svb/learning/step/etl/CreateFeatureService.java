package com.silenteight.payments.bridge.svb.learning.step.etl;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;
import com.silenteight.payments.bridge.datasource.FeatureInputSpecification;
import com.silenteight.payments.bridge.datasource.agent.CreateFeatureInputsProcess;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputStructured;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputUnstructured;
import com.silenteight.payments.bridge.svb.learning.domain.EtlHit;
import com.silenteight.payments.bridge.svb.learning.domain.HitComposite;

import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
class CreateFeatureService {

  private final CreateFeatureInputsProcess createFeatureInputsProcess;

  void createFeatureInputs(
      List<EtlHit> etlHits, RegisterAlertResponse registeredAlert,
      FeatureInputSpecification featureInputSpecification) {

    var featureInputsStructured = etlHits.stream()
        .map(a -> createStructuredInput(a, registeredAlert))
        .collect(toList());

    createFeatureInputsProcess.createStructuredFeatureInputs(
        featureInputsStructured, featureInputSpecification);
  }

  void createUnstructuredFeatureInputs(
      final List<HitComposite> hitComposites,
      final RegisterAlertResponse registeredAlert,
      final FeatureInputSpecification featureInputSpecification
  ) {

    var featureInputsUnstructured = hitComposites.stream()
        .map(a -> createUnstructuredInput(a, registeredAlert))
        .collect(toList());

    createFeatureInputsProcess.createUnstructuredFeatureInputs(
        featureInputsUnstructured, featureInputSpecification);
  }

  private static FeatureInputStructured createStructuredInput(
      EtlHit hit, RegisterAlertResponse registeredAlert) {
    return FeatureInputStructured.builder()
        .alertName(registeredAlert.getAlertName())
        .matchName(registeredAlert.getMatchName(hit.getMatchId()))
        .nameAgentData(hit.getNameAgentData())
        .geoAgentData(hit.getGeoAgentData())
        .identificationMismatchAgentData(hit.getIdentificationMismatchAgentData())
        .historicalAgentData(hit.getHistoricalAgentData())
        .build();
  }

  private static FeatureInputUnstructured createUnstructuredInput(
      HitComposite hit, RegisterAlertResponse registeredAlert) {
    return FeatureInputUnstructured.builder()
        .alertName(registeredAlert.getAlertName())
        .matchName(registeredAlert.getMatchName(hit.getMatchId()))
        .nameMatchedTextAgentData(hit.getNameMatchedTextAgent())
        .contextualAgentData(hit.getContextualAgentData())
        .build();
  }
}
