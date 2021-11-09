package com.silenteight.payments.bridge.data.retention.adapter;

import com.silenteight.payments.bridge.data.retention.model.AlertDataRetention;
import com.silenteight.payments.bridge.data.retention.model.DataType;

import java.time.OffsetDateTime;
import java.util.List;

public interface AlertDataRetentionAccessPort {

  void insert(Iterable<AlertDataRetention> alertRetention);

  List<String> findAlertNameByAlertTimeBefore(OffsetDateTime time, DataType dataType);

  void update(List<String> alertNames, OffsetDateTime dateTime, DataType dataType);

}
