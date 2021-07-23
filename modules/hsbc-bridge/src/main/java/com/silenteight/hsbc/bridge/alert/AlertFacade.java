package com.silenteight.hsbc.bridge.alert;

import lombok.Builder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.alert.AlertPayloadConverter.InputCommand;
import com.silenteight.hsbc.bridge.alert.dto.AlertDataComposite;

import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;

@Builder
@Slf4j
public class AlertFacade implements Consumer<AlertDataComposite> {

  private final AlertPayloadConverter alertPayloadConverter;
  private final AlertRepository repository;

  @Transactional
  public void createRawAlerts(String bulkId, @NonNull InputStream inputStream) throws IOException {
    alertPayloadConverter.convertAndConsumeAlertData(new InputCommand(bulkId, inputStream), this);
  }

  public List<AlertInfo> getAlertByName(@NonNull String name) {
    var alerts = repository.findByName(name).stream().collect(toList());
    return mapToAlertInfo(alerts);
  }

  @Override
  @Transactional
  public void accept(AlertDataComposite alertData) {
    var alertEntity = new AlertEntity(alertData.getBulkId());
    var payloadEntity = new AlertDataPayloadEntity(alertData.getPayload());
    alertEntity.setPayload(payloadEntity);

    repository.save(alertEntity);
  }

  private List<AlertInfo> mapToAlertInfo(List<AlertEntity> alertEntities) {
    return alertEntities.stream().map(a -> new AlertInfo(a.getId())).collect(toList());
  }
}
