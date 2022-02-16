package com.silenteight.payments.bridge.warehouse.index.model;

import lombok.Builder;
import lombok.Value;

import com.silenteight.payments.bridge.warehouse.index.model.learning.IndexMatch;
import com.silenteight.payments.bridge.warehouse.index.model.payload.WarehouseAlert;

import java.util.List;
import java.util.UUID;

@Value
@Builder
public class IndexAlertRegisteredRequest {

  String name;

  String discriminator;

  UUID alertId;

  String systemId;

  String status;

  List<IndexMatch> matches;

  public WarehouseAlert toWarehouseAlert() {
    return WarehouseAlert.builder()
        .alertMessageId(getAlertId().toString())
        .fircoSystemId(getSystemId())
        .status(getStatus())
        .build();
  }
}
