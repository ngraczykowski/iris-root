package com.silenteight.hsbc.bridge.report;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.alert.WarehouseApi;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
class Warehouse implements WarehouseApi {

  private final WarehouseMessageSender sender;

  @Override
  public void send(Collection<Alert> alerts) {
    var data = DataIndexRequestCreator.create(alerts);

    log.info(
        "Sending data to warehouse with id: {}, for analysis: {} and alert count: {}",
        data.getRequestId(), data.getAnalysisName(), data.getAlertsCount());

    sender.send(data);
  }
}
