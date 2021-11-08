package com.silenteight.hsbc.bridge.alert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.alert.AlertSender.SendOption;
import com.silenteight.hsbc.bridge.alert.dto.AlertEntityDto;
import com.silenteight.hsbc.bridge.alert.dto.AlertMatchEntityDto;
import com.silenteight.hsbc.bridge.alert.dto.AlertMetadataDto;

import one.util.streamex.StreamEx;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Slf4j
public class LearningAlertProcessor {

  private final int batchSize;
  private final EntityManager entityManager;
  private final AlertRepository repository;
  private final AlertSender alertSender;

  @Transactional
  public void process(
      @NonNull Collection<Long> registeredIds,
      @NonNull Collection<Long> unregisteredIds) {
    log.info(
        "Processing learning alerts, registered size={}, unregistered size={}, all size={}",
        registeredIds.size(), unregisteredIds.size(),
        registeredIds.size() + unregisteredIds.size());

    updateLearningAlertsAlreadyInDb(registeredIds);
    updateLearningAlertsCompleted(unregisteredIds);

    var alertIds =
        Stream.concat(registeredIds.stream(), unregisteredIds.stream()).collect(Collectors.toList());

    var updatedAlerts = repository.findByIdIn(alertIds);

    sendAlertsChunked(updatedAlerts);
  }

  private void updateLearningAlertsCompleted(Collection<Long> ids) {
    repository.findByIdIn(ids)
        .forEach(a -> {
          a.setStatus(AlertStatus.LEARNING_COMPLETED);
          repository.save(a);
        });
  }

  private void updateLearningAlertsAlreadyInDb(Collection<Long> ids) {
    repository.findByIdIn(ids)
        .forEach(a -> {
          a.setStatus(AlertStatus.LEARNING_REGISTERED_IN_DB);
          repository.save(a);
        });
  }

  private void sendAlertsChunked(Stream<AlertEntity> alertEntities) {
    var counter = new AtomicInteger(0);
    try (alertEntities) {
      StreamEx.of(alertEntities)
          .groupRuns((prev, next) -> counter.incrementAndGet() % batchSize != 0)
          .forEach(chunk -> {
            var alertsDto = chunk.stream()
                .map(this::mapToAlertEntityDto)
                .collect(Collectors.toList());

            entityManager.flush();
            entityManager.clear();

            sendAlerts(alertsDto);
          });
    }
  }

  private AlertEntityDto mapToAlertEntityDto(AlertEntity alertEntity) {
    return AlertEntityDto.builder()
        .externalId(alertEntity.getExternalId())
        .name(alertEntity.getName())
        .discriminator(alertEntity.getDiscriminator())
        .alertTime(alertEntity.getAlertTime())
        .errorMessage(alertEntity.getErrorMessage())
        .bulkId(alertEntity.getBulkId())
        .status(alertEntity.getStatus())
        .matches(mapToAlertMatchEntityDto(alertEntity.getMatches()))
        .payload(alertEntity.getPayload().getPayload())
        .metadata(mapToAlertMetadataDto(alertEntity.getMetadata()))
        .build();
  }

  private List<AlertMatchEntityDto> mapToAlertMatchEntityDto(Collection<AlertMatchEntity> matches) {
    return matches.stream()
        .map(match -> AlertMatchEntityDto.builder()
            .name(match.getName())
            .externalId(match.getExternalId())
            .build())
        .collect(Collectors.toList());
  }

  private List<AlertMetadataDto> mapToAlertMetadataDto(List<AlertMetadata> metadata) {
    return metadata.stream()
        .map(m -> new AlertMetadataDto(m.getKey(), m.getValue()))
        .collect(Collectors.toList());
  }

  private void sendAlerts(List<AlertEntityDto> alerts) {
    alertSender.send(alerts, new SendOption[] { SendOption.AGENTS, SendOption.WAREHOUSE });
  }
}
