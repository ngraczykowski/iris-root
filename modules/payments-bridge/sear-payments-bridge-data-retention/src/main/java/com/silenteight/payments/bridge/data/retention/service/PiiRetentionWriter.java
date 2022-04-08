package com.silenteight.payments.bridge.data.retention.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.dataretention.api.v1.PersonalInformationExpired;
import com.silenteight.payments.bridge.data.retention.adapter.AlertDataRetentionAccessPort;
import com.silenteight.payments.bridge.data.retention.port.SendPersonalInformationExpiredPort;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

import static com.silenteight.payments.bridge.data.retention.model.DataType.PERSONAL_INFORMATION;

@Slf4j
@Component
@RequiredArgsConstructor
class PiiRetentionWriter implements ItemWriter<String> {

  private final AlertDataRetentionAccessPort alertDataRetentionAccessPort;
  private final SendPersonalInformationExpiredPort sendPersonalInformationExpiredPort;

  @SuppressWarnings("unchecked")
  @Override
  public void write(List<? extends String> alertNames) throws Exception {
    alertDataRetentionAccessPort.update(
        (List<String>) alertNames, OffsetDateTime.now(), PERSONAL_INFORMATION);


    log.info("The personal identifiable information data of {} alerts is being "
        + "requested to be deleted in accordance with the retention policy", alertNames.size());

    sendPersonalInformationExpiredPort.send(
        PersonalInformationExpired.newBuilder().addAllAlerts((List<String>) alertNames).build());
  }
}
