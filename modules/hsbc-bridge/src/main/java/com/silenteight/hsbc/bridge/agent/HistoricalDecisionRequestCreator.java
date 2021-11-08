package com.silenteight.hsbc.bridge.agent;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.json.external.model.AlertData;
import com.silenteight.hsbc.bridge.json.external.model.CaseHistory;
import com.silenteight.hsbc.datasource.datamodel.WatchlistType;
import com.silenteight.proto.learningstore.historicaldecision.v1.api.*;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class HistoricalDecisionRequestCreator {

  private final AgentTimestampMapper timestampMapper;

  HistoricalDecisionLearningStoreExchangeRequest create(Collection<AlertData> alerts) {
    return HistoricalDecisionLearningStoreExchangeRequest.newBuilder()
        .addAllAlerts(mapAlerts(alerts))
        .build();
  }

  private List<Alert> mapAlerts(Collection<AlertData> alerts) {
    return alerts
        .stream()
        .map(this::toAlert)
        .collect(Collectors.toList());
  }

  private Alert toAlert(AlertData alert) {
    return Alert.newBuilder()
        .setAlertId(alert.getId())
        .setMatchId(alert.getCaseId())
        .setAlertedParty(createAlertedParty(alert))
        .setWatchlist(createWatchlist(alert))
        .addAllDecisions(mapDecisions(alert.getCaseHistory()))
        .build();
  }

  private List<Decision> mapDecisions(List<CaseHistory> history) {
    return history.stream()
        .filter(e -> "currentState".equals(e.getAttribute()))
        .map(this::toDecision)
        .collect(Collectors.toList());
  }

  private Decision toDecision(CaseHistory caseHistory) {
    return Decision.newBuilder()
        .setId(createDecisionId(caseHistory))
        .setValue(caseHistory.getNewValue())
        .setCreatedAt(timestampMapper.toUnixTimestamp(caseHistory.getModifiedDateTime()))
        .build();
  }

  private String createDecisionId(CaseHistory caseHistory) {
    var allValues = caseHistory.getModifiedBy()
        + caseHistory.getModifiedDateTime()
        + caseHistory.getAttribute()
        + caseHistory.getOldValue()
        + caseHistory.getNewValue()
        + caseHistory.getTransition();

    return DigestUtils.sha1Hex(allValues);
  }

  private Watchlist createWatchlist(AlertData alert) {

    if (!alert.getWorldCheckIndividuals().isEmpty()) {
      var wl = alert.getWorldCheckIndividuals().get(0);
      return getWatchlist(wl.getListRecordId(), WatchlistType.WORLDCHECK_INDIVIDUALS);
    }

    if (!alert.getWorldCheckEntities().isEmpty()) {
      var wl = alert.getWorldCheckEntities().get(0);
      return getWatchlist(wl.getListRecordId(), WatchlistType.WORLDCHECK_ENTITIES);
    }

    if (!alert.getPrivateListIndividuals().isEmpty()) {
      var wl = alert.getPrivateListIndividuals().get(0);
      return getWatchlist(wl.getListRecordId(), WatchlistType.PRIVATE_LIST_INDIVIDUALS);
    }

    if (!alert.getPrivateListEntities().isEmpty()) {
      var wl = alert.getPrivateListEntities().get(0);
      return getWatchlist(wl.getListRecordId(), WatchlistType.PRIVATE_LIST_ENTITIES);
    }

    if (!alert.getCtrpScreeningIndividuals().isEmpty()) {
      var wl = alert.getCtrpScreeningIndividuals().get(0);
      return getWatchlist(wl.getCountryCode(), WatchlistType.CTRPPRHB_LIST_INDIVIDUALS);
    }

    if (!alert.getCtrpScreeningEntities().isEmpty()) {
      var wl = alert.getCtrpScreeningEntities().get(0);
      return getWatchlist(wl.getCountryCode(), WatchlistType.CTRPPRHB_LIST_ENTITIES);
    }

    return Watchlist.getDefaultInstance();
  }

  private Watchlist getWatchlist(String listRecordId, WatchlistType type) {
    return Watchlist.newBuilder()
        .setId(listRecordId)
        .setType(type.getLabel())
        .build();
  }

  private AlertedParty createAlertedParty(AlertData alert) {
    var dnsCase = alert.getCaseInformation();
    var builder = AlertedParty.newBuilder();

    findApCountry(alert).ifPresent(builder::setCountry);
    return builder
        .setId(dnsCase.getExternalId())
        .build();
  }

  private Optional<String> findApCountry(AlertData alertData) {
    var individuals = alertData.getCustomerIndividuals();
    var entities = alertData.getCustomerEntities();

    if (!individuals.isEmpty()) {
      return Optional.of(individuals.get(0).getEdqLobCountryCode());
    } else if (!entities.isEmpty()) {
      return Optional.of(entities.get(0).getEdqLobCountryCode());
    }
    return Optional.empty();
  }
}
