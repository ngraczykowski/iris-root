package com.silenteight.fab.dataprep.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.domain.AlertEntity.State;
import com.silenteight.fab.dataprep.domain.model.AlertItem;
import com.silenteight.fab.dataprep.domain.model.AlertState;
import com.silenteight.fab.dataprep.domain.model.CreateAlertItem;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Optional;
import javax.annotation.PostConstruct;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AlertService {

  private final AlertRepository alertRepository;
  private final Clock clock;
  private final LearningProperties learningProperties;

  @PostConstruct
  @Scheduled(cron = "@monthly")
  public void init() {
    createPartitions();
  }

  public Optional<AlertItem> getAlertItem(String discriminator) {
    OffsetDateTime createdAfter = getCreatedAfter();
    return alertRepository.findByDiscriminatorAndCreatedAtAfter(discriminator, createdAfter)
        .map(entity -> AlertItem.builder()
            .alertName(entity.getAlertName())
            .messageName(entity.getMessageName())
            .discriminator(entity.getDiscriminator())
            .state(AlertState.valueOf(entity.getState().name()))
            .matchNames(new ArrayList<>(entity.getMatchNames()))
            .build());
  }

  public void save(CreateAlertItem createAlertItem) {
    AlertEntity alertEntity = AlertEntity.builder()
        .discriminator(createAlertItem.getDiscriminator())
        .alertName(createAlertItem.getAlertName())
        .messageName(createAlertItem.getMessageName())
        .state(State.REGISTERED)
        .matchNames(createAlertItem.getMatchNames())
        .build();

    log.debug("Saving: {}", alertEntity);
    alertRepository.save(alertEntity);
  }

  public void setAlertState(String discriminator, AlertState alertState) {
    OffsetDateTime createdAfter = getCreatedAfter();
    alertRepository.updateState(discriminator, State.valueOf(alertState.name()), createdAfter);
  }

  public void deleteAll() {
    alertRepository.deleteAll();
  }

  private OffsetDateTime getCreatedAfter() {
    return OffsetDateTime.now(clock)
        .minus(learningProperties.getDataRetentionDuration());
  }

  public void createPartitions() {
    OffsetDateTime currentMonth = OffsetDateTime.now(clock);
    alertRepository.createPartition(currentMonth);
    alertRepository.createPartition(currentMonth.plusMonths(1));
  }
}
