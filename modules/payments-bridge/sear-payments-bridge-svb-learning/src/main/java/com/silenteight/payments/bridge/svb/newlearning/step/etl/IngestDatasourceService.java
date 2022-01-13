package com.silenteight.payments.bridge.svb.newlearning.step.etl;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;
import com.silenteight.payments.bridge.etl.parser.port.MessageParserUseCase;
import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.svb.newlearning.domain.AlertComposite;
import com.silenteight.payments.bridge.svb.newlearning.domain.AlertDetails;
import com.silenteight.payments.bridge.svb.newlearning.domain.EtlHit;
import com.silenteight.payments.bridge.svb.newlearning.step.etl.category.port.CreateCategoriesUseCase;
import com.silenteight.payments.bridge.svb.newlearning.step.etl.feature.port.CreateFeatureUseCase;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.silenteight.payments.bridge.etl.parser.domain.MessageFormat.ALL;
import static com.silenteight.payments.bridge.etl.parser.domain.MessageFormat.SWIFT;
import static com.silenteight.payments.bridge.svb.oldetl.service.AlertParserService.extractAlertedPartyData;

@Service
@RequiredArgsConstructor
class IngestDatasourceService {

  private final CreateFeatureUseCase createFeatureUseCase;
  private final CreateCategoriesUseCase createCategoriesUseCase;
  private final MessageParserUseCase messageParserUseCase;

  void ingest(AlertComposite alertComposite, RegisterAlertResponse registeredAlert) {
    var etlHits = createEtlHits(alertComposite);
    createFeatureUseCase.createFeatureInputs(etlHits, registeredAlert);
    createCategoriesUseCase.createCategoryValues(etlHits, registeredAlert);
  }

  private List<EtlHit> createEtlHits(AlertComposite alertComposite) {
    var alert = alertComposite.getAlertDetails();
    var messageData = createMessageData(alert);
    return alertComposite
        .getHits()
        .stream()
        .map(hit -> hit.toEtlHit(extractAlertedPartyData(
            messageData, hit.getFkcoVMatchedTag(),
            alert.getFkcoVFormat(), alert.getFkcoVApplication()))
        )
        .collect(
            Collectors.toList());
  }

  private MessageData createMessageData(AlertDetails alert) {
    return messageParserUseCase.parse(
        alert.getFkcoVContent().startsWith("{") ? SWIFT : ALL,
        alert.getFkcoVContent());
  }
}
