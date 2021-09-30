package com.silenteight.payments.bridge.firco.adapter.incoming.rest;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.common.model.AlertMessageModel;

import org.springframework.util.AlternativeJdkIdGenerator;
import org.springframework.util.IdGenerator;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Collection;
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

  Stream<AlertMessageModel> map(Collection<AlertMessageDto> dtos) {
    var receiveTime = OffsetDateTime.now(clock);
    return dtos.stream().map(dto -> mapToAlertMessage(receiveTime, dto));
  }

  private AlertMessageModel mapToAlertMessage(OffsetDateTime receiveTime, AlertMessageDto dto) {
    return AlertMessageModel.builder()
        .id(idGenerator.generateId())
        .messageId(dto.getMessageID())
        .unit(dto.getUnit())
        .dataCenter(dataCenter)
        .decisionUrl(decisionUrl)
        .systemId(dto.getSystemID())
        .receivedAt(receiveTime)
        .priority(priorityToInt(dto.getPriority()))
        .businessUnit(dto.getBusinessUnit()).build();
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

  AlertMessageModel map(AlertMessageDto dto) {
    var receiveTime = OffsetDateTime.now(clock);
    return mapToAlertMessage(receiveTime, dto);
  }
}
