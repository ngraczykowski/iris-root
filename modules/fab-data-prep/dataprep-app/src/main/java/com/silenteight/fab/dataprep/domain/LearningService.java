package com.silenteight.fab.dataprep.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.fab.dataprep.domain.model.RegisteredAlert;
import com.silenteight.fab.dataprep.domain.model.WarehouseEvent;
import com.silenteight.fab.dataprep.domain.model.WarehouseEvent.Alert;
import com.silenteight.fab.dataprep.domain.model.WarehouseEvent.Match;
import com.silenteight.fab.dataprep.domain.outgoing.LearningEventPublisher;

import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
class LearningService {

  private final LearningEventPublisher learningEventPublisher;

  public void feedWarehouse(RegisteredAlert registeredAlert) {
    WarehouseEvent warehouseEvent = createEvent(registeredAlert);

    learningEventPublisher.publish(warehouseEvent);
  }

  private static WarehouseEvent createEvent(RegisteredAlert registeredAlert) {
    return WarehouseEvent.builder()
        .requestId(randomUUID().toString())
        .alerts(singletonList(Alert.builder()
            .alertName(registeredAlert.getAlertName())    //TODO add missing fields
            .matches(createMatches(registeredAlert.getMatches()))
            .build()))
        .build();
  }

  private static List<Match> createMatches(List<RegisteredAlert.Match> matches) {
    return matches.stream()
        .map(match -> Match.builder()
            .matchName(match.getMatchName())    //TODO add missing fields
            .build())
        .collect(toList());
  }
}
