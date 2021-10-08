package com.silenteight.payments.bridge.firco.datasource.service.process;

import com.silenteight.payments.bridge.event.AlertRegisteredEvent;
import com.silenteight.payments.bridge.svb.etl.response.AlertEtlResponse;
import com.silenteight.payments.bridge.svb.etl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.etl.response.HitAndWatchlistPartyData;
import com.silenteight.payments.bridge.svb.etl.response.HitData;

import java.util.*;
import javax.annotation.Nonnull;

import static com.silenteight.payments.bridge.common.dto.common.WatchlistType.COMPANY;
import static java.util.List.of;

public class EtlProcessHelper {

  public static AlertRegisteredEvent createAlertRegisteredEvent(int numberOfMatches) {
    Map<String, String> matches = createMatches(numberOfMatches);
    var id = UUID.randomUUID();
    return new AlertRegisteredEvent(id, "alert/" + id, matches);
  }

  @Nonnull
  private static Map<String, String> createMatches(int size) {
    Map<String, String> matches = new HashMap<>();
    for (int i = 0; i < size; i++) {
      matches.put(getMatchId(i), getMatchValue(i));
    }
    return matches;
  }

  @Nonnull
  public static String getMatchId(int id) {
    return String.valueOf(id);
  }

  @Nonnull
  public static String getMatchValue(int id) {
    return "match/" + String.valueOf(id);
  }

  public static AlertEtlResponse createAlertEtlResponse(int numberOfHits) {
    List<HitData> hitDataList = createHitDataList(numberOfHits);
    return AlertEtlResponse.builder().hits(hitDataList).build();
  }

  @Nonnull
  private static List<HitData> createHitDataList(int size) {
    List<HitData> hitDataList = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      hitDataList.add(createHitData(getMatchId(i)));
    }
    return hitDataList;
  }

  @Nonnull
  public static HitData createHitData(String matchId) {
    return new HitData(createAlertedPartyData(matchId), createHitAndWlPartyData(matchId));
  }

  @Nonnull
  private static AlertedPartyData createAlertedPartyData(String id) {
    return AlertedPartyData
        .builder()
        .names(of("AP_name_" + id))
        .addresses(of("AP_address_" + id))
        .build();
  }

  @Nonnull
  private static HitAndWatchlistPartyData createHitAndWlPartyData(String id) {
    return HitAndWatchlistPartyData
        .builder()
        .id(id)
        .name("WP_name_" + id)
        .watchlistType(COMPANY)
        .allMatchingTexts(of("matchingText_" + id))
        .allMatchingFieldValues(of("matchingFieldValue_" + id))
        .countries(of("country_" + id))
        .build();
  }
}
