package com.silenteight.hsbc.bridge.adjudication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.Alert;
import com.silenteight.hsbc.bridge.alert.AlertServiceClient;
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

import static com.silenteight.hsbc.bridge.common.util.TimestampUtil.fromOffsetDateTime;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Slf4j
class AlertService {

  private final AlertServiceClient alertServiceClient;
  private final ApplicationEventPublisher eventPublisher;

  Collection<String> registerAlertsWithMatches(Map<String, AlertMatchIdComposite> alertMatchIds) {
    var alerts = registerAlerts(alertMatchIds.values());
    var matches = registerMatches(alertMatchIds, alerts);

    publishUpdateAlertsWithNameEvent(alerts, alertMatchIds);
    publishUpdateMatchesWithNameEvent(matches, alertMatchIds);

    return alerts.stream()
        .map(AlertDto::getName)
        .collect(toList());
  }

  private List<AlertDto> registerAlerts(Collection<AlertMatchIdComposite> alertMatchIdComposites) {
    List<Alert> alertsForCreation = alertMatchIdComposites.stream()
        .map(a -> Alert
            .newBuilder()
            .setAlertId(a.getAlertExternalId())
            .setAlertTime(fromOffsetDateTime(a.getAlertTime()))
            .build())
        .collect(toList());

    return alertServiceClient.batchCreateAlerts(alertsForCreation).getAlerts();
  }

  private List<MatchWithAlert> registerMatches(
      Map<String, AlertMatchIdComposite> alertMatchIds, List<AlertDto> alerts) {

    return alerts.stream().map(a -> {
      var alertId = a.getAlertId();
      var alertMatchIdComposite = alertMatchIds.get(alertId);
      var matchExternalIds = alertMatchIdComposite.getMatchExternalIds();
      return registerMatchesForAlert(alertId, a.getName(), matchExternalIds);
    }).flatMap(Collection::stream).collect(toList());
  }

  private List<MatchWithAlert> registerMatchesForAlert(
      String alertInternalId, String alertName, Collection<String> matchIds) {
    var request = BatchCreateAlertMatchesRequestDto.builder()
        .alert(alertName)
        .matchIds(matchIds)
        .build();

    var response = alertServiceClient.batchCreateAlertMatches(request);

    return response.getAlertMatches().stream()
        .map(a -> new MatchWithAlert(alertInternalId, alertName, a.getMatchId(), a.getName()))
        .collect(toList());
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
      List<MatchWithAlert> registeredMatches,
      Map<String, AlertMatchIdComposite> alertIdsWithMatches) {
    var matchIdsWithNames = registeredMatches
        .stream()
        .map(a -> {
          var matchIds = alertIdsWithMatches.get(a.getAlertExternalId()).getMatchIds();
          return toMatchIdWithName(matchIds, a);
        })
        .collect(Collectors.toMap(MatchIdWithName::getMatchInternalId, MatchIdWithName::getName));

    eventPublisher.publishEvent(new UpdateMatchWithNameEvent(matchIdsWithNames));
  }

  private MatchIdWithName toMatchIdWithName(
      Collection<MatchIdComposite> matchIds, MatchWithAlert matchWithAlert) {
    var matchExternalId = matchWithAlert.getMatchExternalId();
    var id = findMatchInternalIdByExternalId(matchIds, matchExternalId);
    return new MatchIdWithName(id, matchWithAlert.getMatchName());
  }

  private long findMatchInternalIdByExternalId(
      Collection<MatchIdComposite> matchIds, String matchExternalId) {
    return matchIds
        .stream()
        .filter(m -> m.getExternalId().equals(matchExternalId))
        .map(MatchIdComposite::getInternalId)
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("Match id not found: " + matchExternalId));
  }

  @AllArgsConstructor
  @Getter
  private static class AlertIdWithName {

    private long alertInternalId;
    private String name;
  }

  @AllArgsConstructor
  @Getter
  private static class MatchIdWithName {

    private long matchInternalId;
    private String name;
  }

  @AllArgsConstructor
  @Getter
  private static class MatchWithAlert {

    private String alertExternalId;
    private String alertName;
    private String matchExternalId;
    private String matchName;
  }
}
