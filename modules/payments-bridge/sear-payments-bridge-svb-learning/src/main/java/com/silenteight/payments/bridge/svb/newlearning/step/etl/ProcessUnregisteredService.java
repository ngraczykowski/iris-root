package com.silenteight.payments.bridge.svb.newlearning.step.etl;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.port.RegisterAlertUseCase;
import com.silenteight.payments.bridge.etl.parser.port.MessageParserUseCase;
import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.svb.newlearning.domain.AlertComposite;
import com.silenteight.payments.bridge.svb.newlearning.domain.AlertDetails;
import com.silenteight.payments.bridge.svb.newlearning.domain.EtlHit;
import com.silenteight.payments.bridge.svb.newlearning.domain.LearningRegisteredAlert;
import com.silenteight.payments.bridge.svb.newlearning.step.etl.feature.port.CreateFeatureUseCase;
import com.silenteight.payments.bridge.svb.oldetl.service.AlertParserService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import static com.silenteight.payments.bridge.etl.parser.domain.MessageFormat.ALL;
import static com.silenteight.payments.bridge.etl.parser.domain.MessageFormat.SWIFT;
import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
class ProcessUnregisteredService {

  private final RegisterAlertUseCase registerAlertUseCase;
  private final MessageParserUseCase messageParserUseCase;
  private final CreateFeatureUseCase createFeatureUseCase;

  LearningRegisteredAlert process(AlertComposite alertComposite, long jobId) {
    var etlHits = createEtlHits(alertComposite);
    var hitsFeatures = etlHits
        .stream()
        .map(createFeatureUseCase::createFeatureInputs)
        .map(Map::entrySet)
        .flatMap(Set::stream)
        .collect(toMap(Entry::getKey, Entry::getValue));

    var registeredAlerts = registerAlertUseCase.batchRegistration(
        List.of(alertComposite.toRegisterAlertRequest(jobId)));
    var registeredAlert = registeredAlerts.get(0);
    return alertComposite.toLearningRegisteredAlert(registeredAlert);
  }


  private List<EtlHit> createEtlHits(AlertComposite alertComposite) {
    var alert = alertComposite.getAlertDetails();
    var messageData = createMessageData(alert);
    return alertComposite
        .getHits()
        .stream()
        .map(hit -> EtlHit
            .builder()
            .hitComposite(hit)
            .alertedPartyData(AlertParserService.extractAlertedPartyData(
                messageData, hit.getFkcoVMatchedTag(),
                alert.getFkcoVFormat(), alert.getFkcoVApplication()))
            .build()
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
