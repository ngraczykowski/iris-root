package com.silenteight.hsbc.bridge.adjudication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.alert.AlertServiceApi;
import com.silenteight.hsbc.bridge.alert.dto.AlertDto;
import com.silenteight.hsbc.bridge.alert.dto.BatchCreateAlertMatchesRequestDto;
import com.silenteight.hsbc.bridge.alert.event.UpdateAlertWithNameEvent;
import com.silenteight.hsbc.bridge.domain.AlertMatchIdComposite;
import com.silenteight.hsbc.bridge.match.MatchIdComposite;
import com.silenteight.hsbc.bridge.match.event.UpdateMatchWithNameEvent;

import org.springframework.context.ApplicationEventPublisher;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class AlertService {

  private final AlertServiceApi alertServiceApi;
  private final ApplicationEventPublisher eventPublisher;

  void registerAlertsWithMatches(Map<String, AlertMatchIdComposite> alertMatchIds) {
    var alerts = registerAlerts(alertMatchIds.keySet());
    var matches = registerMatches(alertMatchIds, alerts);

    publishUpdateAlertsWithNameEvent(alerts, alertMatchIds);
    publishUpdateMatchesWithNameEvent(matches, alertMatchIds);
  }

  private List<AlertDto> registerAlerts(Collection<String> alertIds) {
    return alertServiceApi.batchCreateAlerts(alertIds).getAlerts();
  }

  private List<MatchWithAlert> registerMatches(
      Map<String, AlertMatchIdComposite> alertMatchIds, List<AlertDto> alerts) {

    return alerts.stream().map(a -> {
      var alertId = a.getAlertId();
      var alertMatchIdComposite = alertMatchIds.get(alertId);
      var matchExternalIds = alertMatchIdComposite.getMatchExternalIds();
      return registerMatchesForAlert(alertId, matchExternalIds);
    }).flatMap(Collection::stream).collect(Collectors.toList());
  }

  private List<MatchWithAlert> registerMatchesForAlert(
      String alertId, Collection<String> matchIds) {
    var request = BatchCreateAlertMatchesRequestDto.builder()
        .alert(alertId)
        .matchIds(matchIds)
        .build();

    var response = alertServiceApi.batchCreateAlertMatches(request);

    return response.getAlertMatches().stream()
        .map(a -> new MatchWithAlert(alertId, a.getMatchId(), a.getName()))
        .collect(Collectors.toList());
  }

  private void publishUpdateAlertsWithNameEvent(
      List<AlertDto> alerts, Map<String, AlertMatchIdComposite> alertMatchIds) {
    var alertIdsWithNames = alerts
        .stream()
        .map(a -> toAlertIdWithName(alertMatchIds, a))
        .collect(Collectors.toMap(AlertIdWithName::getAlertInternalId, AlertIdWithName::getName));

    eventPublisher.publishEvent(new UpdateAlertWithNameEvent(alertIdsWithNames));
  }

  private AlertIdWithName toAlertIdWithName(
      Map<String, AlertMatchIdComposite> alertMatchIds, AlertDto alert) {
    var alertExternalId = alert.getAlertId();
    var id = alertMatchIds.get(alertExternalId).getAlertInternalId();
    return new AlertIdWithName(id, alert.getName());
  }

  private void publishUpdateMatchesWithNameEvent(
      List<MatchWithAlert> registeredMatches, Map<String, AlertMatchIdComposite> alertIdsWithMatches) {
    var matchIdsWithNames = registeredMatches
        .stream()
        .map(a -> {
          var matchIds = alertIdsWithMatches.get(a.getAlertExternalId()).getMatchIds();
          return toMatchIdWithName(matchIds, a);
        })
        .collect(Collectors.toMap(MatchIdWithName::getMatchInternalId, MatchIdWithName::getName));

    eventPublisher.publishEvent(new UpdateMatchWithNameEvent(matchIdsWithNames));
  }

  private MatchIdWithName toMatchIdWithName(Collection<MatchIdComposite> matchIds, MatchWithAlert matchWithAlert) {
    var matchExternalId = matchWithAlert.getMatchExternalId();
    var id = findMatchInternalIdByExternalId(matchIds, matchExternalId);
    return new MatchIdWithName(id, matchWithAlert.getName());
  }

  private long findMatchInternalIdByExternalId(Collection<MatchIdComposite> matchIds, String matchExternalId) {
    return matchIds
        .stream()
        .filter(m -> m.getExternalId().equals(matchExternalId))
        .map(MatchIdComposite::getInternalId)
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("Match id not found: " + matchExternalId));
  }

  @AllArgsConstructor
  @Getter
  private class AlertIdWithName {

    private long alertInternalId;
    private String name;
  }

  @AllArgsConstructor
  @Getter
  private class MatchIdWithName {

    private long matchInternalId;
    private String name;
  }

  @AllArgsConstructor
  @Getter
  private class MatchWithAlert {

    private String alertExternalId;
    private String matchExternalId;
    private String name;
  }
}
