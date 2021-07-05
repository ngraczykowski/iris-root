package com.silenteight.hsbc.bridge.report;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.alert.WarehouseApi;

import java.util.Collection;

@RequiredArgsConstructor
class Warehouse implements WarehouseApi {

  private final WarehouseMessageSender sender;

  @Override
  public void send(Collection<Alert> alerts) {
    var data = DataIndexRequestCreator.create(alerts);
    sender.send(data);
  }
}
