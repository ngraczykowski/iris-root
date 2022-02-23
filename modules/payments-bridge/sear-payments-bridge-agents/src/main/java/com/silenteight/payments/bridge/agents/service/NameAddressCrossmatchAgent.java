package com.silenteight.payments.bridge.agents.service;

import com.silenteight.payments.bridge.agents.model.AlertedPartyKey;
import com.silenteight.payments.bridge.agents.model.NameAddressCrossmatchAgentRequest;
import com.silenteight.payments.bridge.agents.model.NameAddressCrossmatchAgentResponse;
import com.silenteight.payments.bridge.agents.port.NameAddressCrossmatchUseCase;
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;

import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static com.silenteight.payments.bridge.agents.model.AlertedPartyKey.*;
import static com.silenteight.payments.bridge.agents.model.NameAddressCrossmatchAgentResponse.CROSSMATCH;
import static com.silenteight.payments.bridge.agents.model.NameAddressCrossmatchAgentResponse.NO_CROSSMATCH;
import static com.silenteight.payments.bridge.agents.model.NameAddressCrossmatchAgentResponse.NO_DECISION;
import static java.util.Arrays.asList;

@Service
class NameAddressCrossmatchAgent implements NameAddressCrossmatchUseCase {

  public NameAddressCrossmatchAgentResponse call(NameAddressCrossmatchAgentRequest request) {

    WatchlistType wlType = WatchlistType.ofCode(
        request.getWatchlistType());

    Map<AlertedPartyKey, String> apProperties = request.getAlertPartyEntities();

    if (WatchlistType.ADDRESS == wlType && isNameNotWildcard(request.getWatchlistName())) {
      return NO_DECISION;
    }

    if (MapUtils.isEmpty(apProperties)) {
      return NO_DECISION;
    }

    if (mapContainsAtLeastKeys(apProperties,
        ALERTED_NO_MATCH_KEY, ALERTED_NAMEADDRESS_SEGMENT_KEY)) {
      return NO_DECISION;
    }

    return checkNameAddressCrossmatch(apProperties, wlType) ? CROSSMATCH : NO_CROSSMATCH;
  }

  private static boolean checkNameAddressCrossmatch(
      Map<AlertedPartyKey, String> entityData, WatchlistType wlType) {

    var isNameCrossMatch = checkNameCrossMatch(entityData, wlType);
    var isAddressCrossMatch = checkAddressCrossMatch(entityData, wlType);

    return isNameCrossMatch || isAddressCrossMatch;
  }

  private static boolean checkNameCrossMatch(
      Map<AlertedPartyKey, String> entityData,
      WatchlistType wlType) {
    return mapNotContainsKeys(entityData, ALERTED_NAME_KEY, ALERTED_COMPANY_NAME_KEY)
        && asList(WatchlistType.COMPANY, WatchlistType.INDIVIDUAL).contains(wlType);
  }

  private static boolean checkAddressCrossMatch(
      Map<AlertedPartyKey, String> entityData,
      WatchlistType wlType) {
    return mapNotContainsKeys(entityData, ALERTED_ADDRESS_KEY, ALERTED_COUNTRY_TOWN_KEY)
        && WatchlistType.ADDRESS == wlType;
  }

  private static boolean mapContainsAtLeastKeys(
      Map<AlertedPartyKey, String> map, AlertedPartyKey... keys) {
    var alertedPartyKeys = Arrays.stream(keys).collect(Collectors.toSet());
    return map.keySet().stream().anyMatch(alertedPartyKeys::contains);
  }

  private static boolean mapNotContainsKeys(
      Map<AlertedPartyKey, String> map, AlertedPartyKey... keys) {
    return map.keySet().stream()
        .noneMatch(k -> asList(keys).contains(k));
  }

  private static boolean isNameNotWildcard(String name) {
    return !name.equals("*");
  }
}
