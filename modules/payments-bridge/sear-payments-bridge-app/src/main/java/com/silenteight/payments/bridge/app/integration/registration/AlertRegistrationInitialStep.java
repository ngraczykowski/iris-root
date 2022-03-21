package com.silenteight.payments.bridge.app.integration.registration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.ae.alertregistration.domain.Label;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.port.RegisterAlertUseCase;
import com.silenteight.payments.bridge.common.model.AlertData;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessagePayloadUseCase;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessageUseCase;
import com.silenteight.payments.bridge.firco.dto.input.AlertMessageDto;

import io.micrometer.core.annotation.Timed;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;

import static com.silenteight.payments.bridge.common.app.AlertLabelUtils.ALERT_LABEL_SOLVING;
import static com.silenteight.payments.bridge.common.app.AlertLabelUtils.ALERT_LABEL_SOLVING_CMAPI;
import static com.silenteight.payments.bridge.common.protobuf.TimestampConverter.fromOffsetDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
class AlertRegistrationInitialStep {

  private final RegisterAlertUseCase registerAlertUseCase;
  private final AlertMessageUseCase alertMessageUseCase;
  private final AlertMessagePayloadUseCase alertMessagePayloadUseCase;
  private final UniversalDataSourceStep universalDataSourceStep;

  private static RegisterAlertRequest createRequest(AlertData alertData, AlertMessageDto alertDto) {

    var matchIds = getMatchIds(alertDto);
    return RegisterAlertRequest.builder()
        .alertMessageId(alertData.getAlertId())
        .fkcoSystemId(alertDto.getSystemID())
        .alertTime(fromOffsetDateTime(alertDto.getFilteredAt(ZoneOffset.UTC)))
        .priority(alertData.getPriority())
        .matchIds(matchIds)
        .label(getAlertLabelSolvingCmapi())
        .label(Label.of("alertMessageId", alertData.getAlertId().toString()))
        .build();
  }

  @Nonnull
  private static List<String> getMatchIds(AlertMessageDto alertDto) {
    var hits = alertDto.getHits();

    // XXX(ahaczewski): WATCH OUT! AlertParserService#createAlertEtlResponse() assumes the same
    //  iteration order!!! Make sure you keep it in sync, until shit gets cleaned!!!
    return IntStream.range(0, hits.size())
        .<Optional<String>>mapToObj(idx -> {
          var hit = hits.get(idx).getHit();
          return hit.isBlocking() ? Optional.of(hit.getMatchId(idx)) : Optional.empty();
        })
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());
  }

  private static Label getAlertLabelSolvingCmapi() {
    return Label.of(ALERT_LABEL_SOLVING, ALERT_LABEL_SOLVING_CMAPI);
  }

  @Timed
  void start(UUID alertId) {
    var alertData = alertMessageUseCase.findByAlertMessageId(alertId);
    var alertMessageDto = alertMessagePayloadUseCase.findByAlertMessageId(alertId);

    var response = registerAlertUseCase.register(createRequest(alertData, alertMessageDto));
    log.info("Registered alert {} within ae. AlertName: {}", alertId, response.getAlertName());
    var ctx = new Context(alertData, alertMessageDto, response);
    universalDataSourceStep.invoke(ctx);
  }
}
