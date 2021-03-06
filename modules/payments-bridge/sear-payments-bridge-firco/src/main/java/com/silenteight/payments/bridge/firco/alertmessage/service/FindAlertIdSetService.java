package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.firco.alertmessage.model.AlertIdSet;
import com.silenteight.payments.bridge.firco.alertmessage.port.FindAlertIdSetAccessPort;
import com.silenteight.payments.bridge.firco.alertmessage.port.FindAlertIdSetUseCase;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class FindAlertIdSetService implements FindAlertIdSetUseCase  {

  private final FindAlertIdSetAccessPort findAlertIdSetAccessPort;

  @Override
  @Timed(histogram = true, percentiles = { 0.5, 0.95, 0.99})
  public List<AlertIdSet> find(List<String> systemIds) {
    return findAlertIdSetAccessPort.find(systemIds);
  }
}
