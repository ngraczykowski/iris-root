package com.silenteight.payments.bridge.svb.newlearning.step.etl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;
import com.silenteight.payments.bridge.agents.model.AlertedPartyKey;
import com.silenteight.payments.bridge.etl.parser.port.MessageParserUseCase;
import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.svb.newlearning.domain.AlertComposite;
import com.silenteight.payments.bridge.svb.newlearning.domain.AlertDetails;
import com.silenteight.payments.bridge.svb.newlearning.domain.EtlHit;
import com.silenteight.payments.bridge.svb.newlearning.domain.HitComposite;
import com.silenteight.payments.bridge.svb.newlearning.step.etl.category.port.CreateCategoriesUseCase;
import com.silenteight.payments.bridge.svb.newlearning.step.etl.feature.port.CreateFeatureUseCase;
import com.silenteight.payments.bridge.svb.oldetl.model.CreateAlertedPartyEntitiesRequest;
import com.silenteight.payments.bridge.svb.oldetl.port.CreateAlertedPartyEntitiesUseCase;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.silenteight.payments.bridge.etl.parser.domain.MessageFormat.ALL;
import static com.silenteight.payments.bridge.etl.parser.domain.MessageFormat.SWIFT;
import static com.silenteight.payments.bridge.svb.oldetl.service.AlertParserService.extractAlertedPartyData;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
class IngestDatasourceService {

  private final CreateFeatureUseCase createFeatureUseCase;
  private final CreateCategoriesUseCase createCategoriesUseCase;
  private final MessageParserUseCase messageParserUseCase;
  private final CreateAlertedPartyEntitiesUseCase createAlertedPartyEntitiesUseCase;

  void ingest(AlertComposite alertComposite, RegisterAlertResponse registeredAlert) {
    processForUnstructuredTags(alertComposite, registeredAlert);
    processForStructuredTags(alertComposite, registeredAlert);
  }

  private void processForUnstructuredTags(
      AlertComposite alertComposite, RegisterAlertResponse registeredAlert) {
    createFeatureUseCase.createUnstructuredFeatureInputs(alertComposite.getHits(), registeredAlert);
  }

  private void processForStructuredTags(
      AlertComposite alertComposite, RegisterAlertResponse registeredAlert) {
    try {
      var etlHits = createEtlHits(alertComposite);
      createFeatureUseCase.createFeatureInputs(etlHits, registeredAlert);
      createCategoriesUseCase.createCategoryValues(etlHits, registeredAlert);
    } catch (Exception e) {
      log.warn("Failed to process alert = {} for structured tags", alertComposite.getSystemId());
    }
  }

  private List<EtlHit> createEtlHits(AlertComposite alertComposite) {
    return alertComposite
        .getHits()
        .stream()
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
    var allMatchingFields = createAllMatchFieldsValue(messageData, hit.getFkcoVMatchedTag());
    return hit.toEtlHit(alertedParty, alertedPartyEntities, allMatchingFields);
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

  private static List<String> createAllMatchFieldsValue(
      MessageData messageData, String matchedTag) {
    return messageData.findAllValues(matchedTag).collect(toList());
  }
}
