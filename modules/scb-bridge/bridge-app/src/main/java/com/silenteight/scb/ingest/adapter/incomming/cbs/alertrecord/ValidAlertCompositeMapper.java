package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class ValidAlertCompositeMapper {

  public List<ValidAlertComposite> fromAlertCompositeCollections(
      List<AlertCompositeCollection> alertCompositeCollections) {
    return alertCompositeCollections
        .stream()
        .map(AlertCompositeCollection::getValidAlerts)
        .flatMap(Collection::stream)
        .toList();
  }
}
