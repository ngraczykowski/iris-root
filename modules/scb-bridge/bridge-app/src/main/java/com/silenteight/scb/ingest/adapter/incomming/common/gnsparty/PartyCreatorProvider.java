package com.silenteight.scb.ingest.adapter.incomming.common.gnsparty;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.commons.collections.MapBuilder;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.StringUtils.isBlank;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class PartyCreatorProvider {

  public static final long GROUP_ONE_TOKEN_COUNT = 35L;
  public static final long GROUP_TWO_TOKEN_COUNT = 70L;
  public static final long GROUP_FOUR_TOKEN_COUNT = 26L;
  public static final long GROUP_FIVE_TOKEN_COUNT = 84L;

  private static final GnsPartyCreator GROUP_ONE_CREATOR = new GnsPartyGroupOneCreator();
  private static final GnsPartyCreator GROUP_TWO_CREATOR = new GnsPartyGroupTwoCreator();
  private static final GnsPartyCreator GROUP_FOUR_CREATOR = new GnsPartyGroupFourCreator();
  private static final GnsPartyCreator GROUP_FIVE_CREATOR = new GnsPartyGroupFiveCreator();

  private static final Map<Long, List<GnsPartyCreator>> FIELD_COUNT_CREATOR_MAP =
      MapBuilder.from(
          GROUP_ONE_TOKEN_COUNT, singletonList(GROUP_ONE_CREATOR),
          GROUP_TWO_TOKEN_COUNT, singletonList(GROUP_TWO_CREATOR),
          GROUP_FOUR_TOKEN_COUNT, singletonList(GROUP_FOUR_CREATOR),
          GROUP_FIVE_TOKEN_COUNT, singletonList(GROUP_FIVE_CREATOR)
      );

  static Optional<List<GnsPartyCreator>> getCreatorsByColumnCount(String record, char separator) {
    return Optional.ofNullable(FIELD_COUNT_CREATOR_MAP.get(countFields(record, separator)));
  }

  static long countFields(String record, char separator) {
    if (isBlank(record))
      return 0;
    return Pattern.compile(Character.toString(separator))
        .matcher(record)
        .results()
        .count() + 1;
  }
}
