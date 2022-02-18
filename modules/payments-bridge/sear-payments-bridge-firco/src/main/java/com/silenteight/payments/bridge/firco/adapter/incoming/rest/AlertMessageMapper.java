package com.silenteight.payments.bridge.firco.adapter.incoming.rest;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.alertmessage.model.FircoAlertMessage;
import com.silenteight.payments.bridge.firco.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.firco.dto.input.CaseManagerAuthenticationDto;
import com.silenteight.payments.bridge.firco.dto.input.RequestDto;

import org.springframework.util.AlternativeJdkIdGenerator;
import org.springframework.util.IdGenerator;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
class AlertMessageMapper {

  private static final IdGenerator DEFAULT_ID_GENERATOR = new AlternativeJdkIdGenerator();

  private final String decisionUrl;
  private final String dataCenter;

  @Setter
  private IdGenerator idGenerator = DEFAULT_ID_GENERATOR;

  @Setter
  private Integer defaultPriority = 5;

  @Setter
  private Clock clock = Clock.systemUTC();

  Stream<FircoAlertMessage> map(RequestDto dtos) {
    var receiveTime = OffsetDateTime.now(clock);
    return dtos
        .getAlerts()
        .stream()
        .map(dto -> mapToAlertMessage(receiveTime, dto, dtos.getAuthentication()));
  }

  private FircoAlertMessage mapToAlertMessage(
      OffsetDateTime receiveTime, AlertMessageDto dto, CaseManagerAuthenticationDto credentials) {
    return new FircoAlertMessage(
        idGenerator.generateId(), receiveTime, dto, dataCenter, decisionUrl,
        credentials.getUserLogin(), credentials.getUserPassword());
  }

  private Integer priorityToInt(String priority) {
    if (StringUtils.hasLength(priority)) {
      try {
        return NumberUtils.parseNumber(priority.trim(), Integer.class);
      } catch (NumberFormatException exception) {
        log.warn(
            "Couldn't convert priority string [{}] into integer. Setting default value {})",
            priority, defaultPriority);
      }
    }
    return defaultPriority;
  }

}
