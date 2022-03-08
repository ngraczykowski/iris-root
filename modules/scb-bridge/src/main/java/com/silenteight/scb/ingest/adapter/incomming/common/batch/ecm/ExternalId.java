package com.silenteight.scb.ingest.adapter.incomming.common.batch.ecm;

import lombok.Value;

import com.google.common.base.Strings;

import static com.google.common.base.Preconditions.checkArgument;

@Value
public class ExternalId {

  public static final String SYSTEM_ID = "SYSTEM_ID";
  public static final String HIT_UNIQUE_ID = "HIT_UNIQUE_ID";

  String systemId;
  String watchlistId;

  private static final String HIT_UNIQUE_ID_SEPARATOR = "_";
  private static final int INDEX_OF_WATCHLIST_ID = 0;

  static String tryToExtractWatchlistIdFromHitUniqueId(String hitUniqueId) {
    checkArgument(!Strings.isNullOrEmpty(hitUniqueId));

    String[] splitHitUniqueId = hitUniqueId.split(HIT_UNIQUE_ID_SEPARATOR);
    if (splitHitUniqueId.length == 3) {
      return splitHitUniqueId[INDEX_OF_WATCHLIST_ID];
    } else {
      throw new IllegalArgumentException(
          "HIT_UNIQUE_ID: " + hitUniqueId
              + ", is in wrong format. Should be watchlistId_tag_sequence.");
    }
  }
}
