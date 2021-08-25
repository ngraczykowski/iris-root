package com.silenteight.payments.bridge.agents;

import com.silenteight.payments.bridge.agents.NameAddressCrossmatchAgentResponse.Result;

import java.util.Map;

import static java.util.Arrays.asList;

class NameAddressCrossmatchAgent {

  public static final String EMPTY_ENTITY_TYPE = "";
  public static final String EMPTY_ENTITY_VALUE = "";

  private static final String WILDCARD_SYMBOL = "*";
  private static final String ALERTED_NAME_KEY = "NAME";
  private static final String ALERTED_COMPANY_NAME_KEY = "COMPANY NAME";
  private static final String ALERTED_ADDRESS_KEY = "ADDRESS";
  private static final String ALERTED_COUNTRY_TOWN_KEY = "COUNTRY TOWN";
  private static final String ALERTED_NAMEADDRESS_SEGMENT_KEY = "NAMEADDRESS FIRST SEGMENT";
  private static final String ALERTED_NO_MATCH_KEY = "NO_MATCH";

  public NameAddressCrossmatchAgentResponse call(NameAddressCrossmatchAgentRequest request) {

    WatchlistType wlType = WatchlistType.ofCode(
        request.getWatchlistType());

    if (WatchlistType.ADDRESS == wlType && !isNameWildcard(
        request.getWatchlistName()))
      return NameAddressCrossmatchAgentResponse.of(
          Result.NO_DECISION, Map.of(WILDCARD_SYMBOL, EMPTY_ENTITY_VALUE));

    Map<String, String> apProperties = request.getAlertPartyEntities();
    if (apProperties == null || apProperties
        .isEmpty())
      return NameAddressCrossmatchAgentResponse.of(
          Result.NO_DECISION, Map.of(EMPTY_ENTITY_TYPE, EMPTY_ENTITY_VALUE));

    if (apProperties.containsKey(ALERTED_NO_MATCH_KEY))
      return NameAddressCrossmatchAgentResponse.of(
          Result.NO_DECISION, Map.of(ALERTED_NO_MATCH_KEY, apProperties.get(ALERTED_NO_MATCH_KEY)));

    if (apProperties.containsKey(ALERTED_NAMEADDRESS_SEGMENT_KEY))
      return NameAddressCrossmatchAgentResponse.of(
          Result.NO_DECISION,
          Map.of(
              ALERTED_NAMEADDRESS_SEGMENT_KEY,
              apProperties.get(ALERTED_NAMEADDRESS_SEGMENT_KEY)));

    if (checkNameAddressCrossmatch(apProperties, wlType))
      return NameAddressCrossmatchAgentResponse.of(Result.CROSSMATCH, apProperties);
    else
      return NameAddressCrossmatchAgentResponse.of(Result.NO_CROSSMATCH, apProperties);
  }

  private static boolean checkNameAddressCrossmatch(
      Map<String, String> entityData, WatchlistType wlType) {

    return mapNotContainsKeys(entityData, ALERTED_NAME_KEY, ALERTED_COMPANY_NAME_KEY)
        && asList(WatchlistType.COMPANY, WatchlistType.INDIVIDUAL).contains(wlType)
        || mapNotContainsKeys(entityData, ALERTED_ADDRESS_KEY, ALERTED_COUNTRY_TOWN_KEY)
        && WatchlistType.ADDRESS == wlType;
  }

  private static boolean mapNotContainsKeys(Map<String, String> map, String... keys) {
    return map.keySet().stream().noneMatch(k -> asList(keys).contains(k));
  }

  private static boolean isNameWildcard(String name) {
    return WILDCARD_SYMBOL.equals(name);
  }
}
