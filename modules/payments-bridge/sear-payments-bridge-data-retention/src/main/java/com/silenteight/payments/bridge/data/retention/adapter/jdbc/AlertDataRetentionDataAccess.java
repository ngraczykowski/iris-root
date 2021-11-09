package com.silenteight.payments.bridge.data.retention.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.data.retention.adapter.AlertDataRetentionAccessPort;
import com.silenteight.payments.bridge.data.retention.model.AlertDataRetention;
import com.silenteight.payments.bridge.data.retention.model.DataType;

import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
public class AlertDataRetentionDataAccess implements AlertDataRetentionAccessPort {

  private final InsertAlertDataRetention insertAlertDataRetention;
  private final FindAlertDataRetention findAlertDataRetention;
  private final UpdateAlertDataRetention updateAlertDataRetention;

  @Override
  public void insert(Iterable<AlertDataRetention> alertRetention) {
    insertAlertDataRetention.update(alertRetention);
  }

  @Override
  public List<String> findAlertNameByAlertTimeBefore(OffsetDateTime dateTime, DataType dataType) {
    return findAlertDataRetention.findAlertTimeBefore(dateTime, dataType);
  }

  @Override
  public void update(List<String> alertNames, OffsetDateTime dateTime, DataType dataType) {
    updateAlertDataRetention.update(alertNames, dateTime, dataType);
  }
}
