package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class InvalidAlertMapper {

  public List<InvalidAlert> fromAlertCompositeCollections(
      List<AlertCompositeCollection> alertCompositeCollections) {
    return alertCompositeCollections
        .stream()
        .map(AlertCompositeCollection::getInvalidAlerts)
        .flatMap(Collection::stream)
        .toList();
  }
}
