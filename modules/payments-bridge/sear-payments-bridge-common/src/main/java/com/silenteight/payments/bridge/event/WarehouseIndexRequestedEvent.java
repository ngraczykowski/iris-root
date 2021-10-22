package com.silenteight.payments.bridge.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import com.silenteight.data.api.v1.ProductionDataIndexRequest;

@RequiredArgsConstructor
@Getter
@ToString
public class WarehouseIndexRequestedEvent {

  private final ProductionDataIndexRequest request;
  private final IndexRequestOrigin indexRequestOrigin;

  public enum IndexRequestOrigin {
    UNSET,
    CMAPI,
    LEARNING
  }

}
