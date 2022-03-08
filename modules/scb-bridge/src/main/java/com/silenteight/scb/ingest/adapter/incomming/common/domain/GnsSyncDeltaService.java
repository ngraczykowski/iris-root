package com.silenteight.scb.ingest.adapter.incomming.common.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.common.batch.ecm.ExternalId;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.persistence.EntityManager;

import static com.silenteight.scb.ingest.adapter.incomming.common.domain.GnsSyncConstants.PRIMARY_TRANSACTION_MANAGER;
import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
@Slf4j
public class GnsSyncDeltaService {

  public static final String SPLIT_ID_CHARACTER = "|";
  private static final String SPLIT_ID_CHARACTER_REGEX = "\\|";
  private static final String EMPTY_STRING = "";
  private final EntityManager entityManager;
  private final GnsSyncDeltaRepository deltaRepository;

  @Transactional(PRIMARY_TRANSACTION_MANAGER)
  public void updateDelta(Map<String, Integer> deltas, String deltaJobName) {
    if (deltas.isEmpty())
      return;

    deltaRepository.deleteAllByAlertExternalIdInAndDeltaJobName(deltas.keySet(), deltaJobName);
    deltas.forEach(
        (externalAlertId, decisionsCount) -> entityManager.persist(
            createGnsSynDelta(externalAlertId, decisionsCount, deltaJobName)));
  }

  @Nonnull
  private static GnsSyncDelta createGnsSynDelta(
      String externalAlertId, Integer decisionsCount, String deltaJobName) {
    if (externalAlertId.contains(SPLIT_ID_CHARACTER))
      return new GnsSyncDelta(
          externalAlertId, decisionsCount, tryExtractWatchlistId(externalAlertId), deltaJobName);
    else
      return new GnsSyncDelta(externalAlertId, decisionsCount, EMPTY_STRING, deltaJobName);
  }

  private static String tryExtractWatchlistId(String externalAlertId) {
    final String[] splitId = externalAlertId.split(SPLIT_ID_CHARACTER_REGEX);
    if (splitId.length == 2) {
      return splitId[1];
    } else {
      return EMPTY_STRING;
    }
  }

  @Transactional(transactionManager = PRIMARY_TRANSACTION_MANAGER, readOnly = true)
  public Map<String, Integer> findAllByAlertExternalId(List<String> ids, String deltaJobName) {
    return deltaRepository
        .findAllByAlertExternalIdInAndDeltaJobName(ids, deltaJobName)
        .stream()
        .collect(toMap(
            GnsSyncDeltaProjection::getAlertExternalId,
            GnsSyncDeltaProjection::getDecisionsCount));
  }

  @Transactional(transactionManager = PRIMARY_TRANSACTION_MANAGER, readOnly = true)
  public Map<ExternalId, Integer> findAllByExternalId(
      List<ExternalId> externalIds, String deltaJobName) {
    final List<String> systemIds =
        externalIds
            .stream()
            .map(x -> x.getSystemId() + "|" + x.getWatchlistId())
            .collect(Collectors.toList());
    final List<String> watchlistIds =
        externalIds.stream().map(ExternalId::getWatchlistId).collect(Collectors.toList());

    return deltaRepository
        .findAllByExternalIdInAndDeltaJobName(systemIds, watchlistIds, deltaJobName)
        .stream()
        .collect(Collectors.toMap(
            x -> new ExternalId(x.getAlertExternalId(), x.getWatchlistId()),
            GnsSyncDeltaProjection::getDecisionsCount));
  }
}
