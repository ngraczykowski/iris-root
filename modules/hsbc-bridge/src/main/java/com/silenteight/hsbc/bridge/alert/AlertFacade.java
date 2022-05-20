package com.silenteight.hsbc.bridge.alert;

import lombok.Builder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.alert.AlertPayloadConverter.InputCommand;
import com.silenteight.hsbc.bridge.alert.dto.AlertDataComposite;
import com.silenteight.hsbc.bridge.util.StreamUtils;

import one.util.streamex.StreamEx;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.EntityManager;

@Builder
@Slf4j
public class AlertFacade implements Consumer<AlertDataComposite> {

  private final AlertPayloadConverter alertPayloadConverter;
  private final AlertRepository repository;
  private final EntityManager entityManager;
  private final AlertReProcessor alertReProcessor;

  @Transactional
  public void createRawAlerts(String bulkId, @NonNull InputStream inputStream) throws IOException {
    alertPayloadConverter.convertAndConsumeAlertData(new InputCommand(bulkId, inputStream), this);
  }

  public List<AlertInfo> getAlertByName(@NonNull String name) {
    var alerts = repository.findByName(name).stream().collect(Collectors.toList());
    return mapToAlertInfo(alerts);
  }

  @Transactional
  public Collection<AlertEntity> getRegisteredAlertsFromDb(Stream<String> idsWithDiscriminators) {
    var registeredAlerts = new ArrayList<AlertEntity>();
    var counter = new AtomicInteger(0);

    StreamEx.of(idsWithDiscriminators)
        .groupRuns((prev, next) -> counter.incrementAndGet() % 1000 != 0)
        .forEach(chunk -> registeredAlerts.addAll(
            repository.findByNameIsNotNullAndStatusAndExternalIdInAndDiscriminatorIn(chunk)
                .collect(Collectors.toList())));

    return registeredAlerts.stream()
        .filter(StreamUtils.distinctBy(alert -> alert.getExternalId() + "_" + alert.getDiscriminator()))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  @Override
  @Transactional
  public void accept(AlertDataComposite alertData) {
    var alertEntity = new AlertEntity(alertData.getBulkId());
    var payloadEntity = new AlertDataPayloadEntity(alertData.getPayload());
    alertEntity.setPayload(payloadEntity);

    repository.save(alertEntity);
    entityManager.flush();
    entityManager.clear();
  }

  private List<AlertInfo> mapToAlertInfo(List<AlertEntity> alertEntities) {
    return alertEntities.stream().map(a -> new AlertInfo(a.getId())).collect(Collectors.toList());
  }

  public void reProcessAlerts(String bulkId, List<String> alerts) {
    alertReProcessor.reProcessAlerts(bulkId, alerts);
  }
}
