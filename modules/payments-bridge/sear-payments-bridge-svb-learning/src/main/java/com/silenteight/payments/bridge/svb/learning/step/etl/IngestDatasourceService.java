package com.silenteight.payments.bridge.svb.learning.step.etl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;
import com.silenteight.payments.bridge.agents.model.AlertedPartyKey;
import com.silenteight.payments.bridge.datasource.DefaultFeatureInputSpecification;
import com.silenteight.payments.bridge.datasource.FeatureInputSpecification;
import com.silenteight.payments.bridge.etl.parser.port.MessageParserUseCase;
import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.svb.learning.domain.AlertComposite;
import com.silenteight.payments.bridge.svb.learning.domain.AlertDetails;
import com.silenteight.payments.bridge.svb.learning.domain.EtlHit;
import com.silenteight.payments.bridge.svb.learning.domain.HitComposite;
import com.silenteight.payments.bridge.svb.oldetl.model.CreateAlertedPartyEntitiesRequest;
import com.silenteight.payments.bridge.svb.oldetl.port.CreateAlertedPartyEntitiesUseCase;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.silenteight.payments.bridge.common.dto.common.MessageStructure.isMessageStructured;
import static com.silenteight.payments.bridge.etl.parser.domain.MessageFormat.ALL;
import static com.silenteight.payments.bridge.etl.parser.domain.MessageFormat.SWIFT;
import static com.silenteight.payments.bridge.svb.oldetl.service.AlertParserService.extractAlertedPartyData;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class IngestDatasourceService {

  private final CreateFeatureService createFeatureService;
  private final CreateCategoriesValuesService createCategoriesValuesService;
  private final MessageParserUseCase messageParserUseCase;
  private final CreateAlertedPartyEntitiesUseCase createAlertedPartyEntitiesUseCase;

  public void ingest(AlertComposite alertComposite, RegisterAlertResponse registeredAlert) {
    ingest(alertComposite, registeredAlert, DefaultFeatureInputSpecification.INSTANCE);
  }

  public void ingest(
      final AlertComposite alertComposite, final RegisterAlertResponse registeredAlert,
      final FeatureInputSpecification featureInputSpecification
  ) {
    processForUnstructuredTags(alertComposite, registeredAlert, featureInputSpecification);
    processForStructuredTags(alertComposite, registeredAlert, featureInputSpecification);
  }


  private void processForUnstructuredTags(
      AlertComposite alertComposite, RegisterAlertResponse registeredAlert,
      FeatureInputSpecification featureInputSpecification) {
    createFeatureService.createUnstructuredFeatureInputs(
        alertComposite.getHits(),
        registeredAlert,
        featureInputSpecification
    );

    createCategoriesValuesService.createUnstructuredCategoryValues(
        alertComposite.getHits(), registeredAlert,
        featureInputSpecification);
  }

  private void processForStructuredTags(
      AlertComposite alertComposite, RegisterAlertResponse registeredAlert,
      FeatureInputSpecification featureInputSpecification) {
    var etlHits = createEtlHits(alertComposite);

    if (etlHits.isEmpty()) {
      return;
    }

    createFeatureService.createFeatureInputs(etlHits, registeredAlert, featureInputSpecification);
    createCategoriesValuesService.createCategoryValues(
        etlHits, registeredAlert, featureInputSpecification);
  }

  private List<EtlHit> createEtlHits(AlertComposite alertComposite) {
    return alertComposite
        .getHits()
        .stream()
        .filter(hitComposite -> isMessageStructured(hitComposite.getFkcoVMatchedTag()))
        .map(hit -> createEtlHit(hit, alertComposite))
        .collect(toList());
  }

  private EtlHit createEtlHit(HitComposite hit, AlertComposite alertComposite) {
    var alert = alertComposite.getAlertDetails();
    var messageData = createMessageData(alert);
    var alertedParty = extractAlertedPartyData(
        messageData, hit.getFkcoVMatchedTag(),
        alert.getFkcoVFormat(), alert.getFkcoVApplication());
    var alertedPartyEntities = createAlertedPartyEntities(alertedParty, hit.getMatchingTexts());
    return hit.toEtlHit(alertedParty, alertedPartyEntities);
  }

  private MessageData createMessageData(AlertDetails alert) {
    return messageParserUseCase.parse(
        alert.getFkcoVContent().startsWith("{") ? SWIFT : ALL,
        alert.getFkcoVContent());
  }

  private Map<AlertedPartyKey, String> createAlertedPartyEntities(
      AlertedPartyData alertedPartyData, List<String> matchingTexts) {

    return createAlertedPartyEntitiesUseCase.create(CreateAlertedPartyEntitiesRequest
        .builder()
        .alertedPartyData(alertedPartyData)
        .allMatchingText(matchingTexts)
        .build());
  }
}
