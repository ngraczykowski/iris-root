package com.silenteight.payments.bridge.agents.service;

import com.silenteight.payments.bridge.agents.model.AlertedPartyKey;
import com.silenteight.payments.bridge.agents.model.NameAddressCrossmatchAgentRequest;
import com.silenteight.payments.bridge.agents.model.NameAddressCrossmatchAgentResponse;
import com.silenteight.payments.bridge.agents.port.NameAddressCrossmatchUseCase;
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;

import org.springframework.stereotype.Service;

import java.util.Map;

import static com.silenteight.payments.bridge.agents.model.AlertedPartyKey.*;
import static java.util.Arrays.asList;

@Service
class NameAddressCrossmatchAgent implements NameAddressCrossmatchUseCase {

  public NameAddressCrossmatchAgentResponse call(NameAddressCrossmatchAgentRequest request) {

    WatchlistType wlType = WatchlistType.ofCode(
        request.getWatchlistType());

    if (WatchlistType.ADDRESS == wlType && !isNameWildcard(request.getWatchlistName()))
      return NameAddressCrossmatchAgentResponse.NO_DECISION;

    Map<AlertedPartyKey, String> apProperties = request.getAlertPartyEntities();
    if (apProperties == null || apProperties.isEmpty())
      return NameAddressCrossmatchAgentResponse.NO_DECISION;

    if (apProperties.containsKey(ALERTED_NO_MATCH_KEY) ||
        apProperties.containsKey(ALERTED_NAMEADDRESS_SEGMENT_KEY)) {
      return NameAddressCrossmatchAgentResponse.NO_DECISION;
    }

    if (checkNameAddressCrossmatch(apProperties, wlType))
      return NameAddressCrossmatchAgentResponse.CROSSMATCH;
    else
      return NameAddressCrossmatchAgentResponse.NO_CROSSMATCH;
  }

  private static boolean checkNameAddressCrossmatch(
      Map<AlertedPartyKey, String> entityData, WatchlistType wlType) {

    return mapNotContainsKeys(entityData, ALERTED_NAME_KEY, ALERTED_COMPANY_NAME_KEY)
        && asList(WatchlistType.COMPANY, WatchlistType.INDIVIDUAL).contains(wlType)
        || mapNotContainsKeys(entityData, ALERTED_ADDRESS_KEY, ALERTED_COUNTRY_TOWN_KEY)
        && WatchlistType.ADDRESS == wlType;
  }

  private static boolean mapNotContainsKeys(
      Map<AlertedPartyKey, String> map, AlertedPartyKey... keys) {
    return map.keySet().stream().noneMatch(k -> asList(keys).contains(k));
  }

  private static boolean isNameWildcard(String name) {
    return name.equals("*");
  }
}
