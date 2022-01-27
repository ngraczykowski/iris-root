package com.silenteight.payments.bridge.firco.datasource.service.process;

import com.silenteight.payments.bridge.common.dto.common.SolutionType;
import com.silenteight.payments.bridge.common.model.AeAlert;
import com.silenteight.payments.bridge.firco.datasource.model.CategoryValueExtractModel;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.HitAndWatchlistPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import java.util.*;
import javax.annotation.Nonnull;

import static com.silenteight.payments.bridge.common.dto.common.WatchlistType.COMPANY;
import static java.util.List.of;

public class EtlProcessFixture {

  public static AeAlert createAeAlert(int numberOfMatches) {
    Map<String, String> matches = createMatches(numberOfMatches);
    var id = UUID.randomUUID();
    return AeAlert.builder()
        .alertId(id).alertName("alerts/" + id).matches(matches).build();
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
    return String.format("%d(%d, #%d)", id, id, id);
  }

  @Nonnull
  public static String getMatchValue(int id) {
    return "matches/" + id;
  }

  @Nonnull
  public static String getAlertName(int id) {
    return "alerts/" + id;
  }

  public static List<HitData> createListOfHitData(int numberOfHits) {
    return createHitDataList(numberOfHits);
  }

  public static CategoryValueExtractModel getCategoryValueExtractModel(int id) {
    return CategoryValueExtractModel.builder()
        .hitData(createHitData(getMatchId(id)))
        .matchName(getMatchValue(id))
        .alertName(getAlertName(id))
        .build();
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
    return new HitData(matchId, createAlertedPartyData(matchId), createHitAndWlPartyData(matchId));
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
        .solutionType(SolutionType.SEARCH_CODE)
        .id(id)
        .tag(id)
        .name("WP_name_" + id)
        .watchlistType(COMPANY)
        .matchingText("matchingText")
        .allMatchingTexts(of("matchingText_" + id))
        .fieldValue("fieldValue")
        .allMatchingFieldValues(of("matchingFieldValue_" + id))
        .countries(of("country_" + id))
        .searchCodes(List.of("searchCodeOne", "searchCodeTwo"))
        .build();
  }
}
