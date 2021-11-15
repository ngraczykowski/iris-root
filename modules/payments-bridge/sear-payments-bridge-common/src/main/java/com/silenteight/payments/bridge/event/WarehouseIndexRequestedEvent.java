package com.silenteight.payments.bridge.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import com.silenteight.data.api.v2.ProductionDataIndexRequest;

@RequiredArgsConstructor
@Getter
@ToString
public class WarehouseIndexRequestedEvent extends DomainEvent {

  public static final String CHANNEL = "warehouseIndexRequestedEventChannel";

  private final ProductionDataIndexRequest request;
  private final IndexRequestOrigin indexRequestOrigin;

  public enum IndexRequestOrigin {
    UNSET,
    CMAPI,
    LEARNING
  }

}
