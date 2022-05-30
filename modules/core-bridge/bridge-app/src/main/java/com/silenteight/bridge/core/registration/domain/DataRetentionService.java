package com.silenteight.bridge.core.registration.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.command.DataRetentionStrategyCommand;
import com.silenteight.bridge.core.registration.domain.command.StartDataRetentionCommand;
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository;
import com.silenteight.bridge.core.registration.domain.strategy.DataRetentionStrategyFactory;

import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
class DataRetentionService {

  private final AlertRepository alertRepository;
  private final DataRetentionStrategyFactory strategyFactory;

  void start(StartDataRetentionCommand command) {
    log.info(
        "Starting data retention process. Duration: [{}], mode: [{}]", command.duration(),
        command.mode());

    var expirationDate = Instant.now().minus(command.duration());
    var alerts = alertRepository.findAlertsApplicableForDataRetention(expirationDate);
    log.info(
        "Found [{}] alerts qualifying for data retention with expiration date [{}]", alerts.size(),
        expirationDate);

    var strategy = strategyFactory.getStrategy(command.mode());
    var strategyCommand = DataRetentionStrategyCommand.builder()
        .mode(command.mode())
        .expirationDate(expirationDate)
        .alerts(alerts)
        .chunkSize(command.chunkSize())
        .build();
    strategy.run(strategyCommand);
  }
}
