package com.silenteight.customerbridge.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.silenteight.proto.serp.v1.common.ObjectId;
import com.silenteight.protocol.utils.ObjectIds;
import com.silenteight.protocol.utils.Uuids;

import org.apache.commons.collections4.CollectionUtils;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.annotation.Nullable;

import static com.google.common.base.Strings.nullToEmpty;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AlertParserUtils {

  public static final String SPLIT_ID_CHARACTER = "|";

  @SafeVarargs
  @SuppressWarnings("varargs")
  public static <T> List<T> expand(Collection<T>... lists) {
    return Stream
        .of(lists)
        .filter(CollectionUtils::isNotEmpty)
        .flatMap(Collection::stream)
        .filter(Objects::nonNull)
        .collect(toList());
  }

  public static void mapString(@Nullable String input, @NonNull Consumer<String> consumer) {
    if (isNotEmpty(input))
      consumer.accept(input);
  }

  @Nullable
  public static String determineApType(String typeOfRec, String suspectType) {
    String apType;
    if (asList("I", "C").contains(typeOfRec))
      apType = typeOfRec;
    else if ("I".equals(suspectType))
      apType = suspectType;
    else if (asList("O", "C").contains(suspectType))
      apType = "C";
    else
      apType = null;

    return apType;
  }

  public static ObjectId makeWatchlistPartyId(@NonNull String ofacId, @Nullable String batchId) {
    return ObjectIds.fromUuidAndSource(UUID.randomUUID(), ofacId, nullToEmpty(batchId));
  }

  public static ObjectId makeAlertedPartyId(
      @NonNull String recordId, @NonNull String recordSignature) {
    return ObjectIds.fromUuidAndSource(UUID.randomUUID(), recordId, recordSignature);
  }

  static ObjectId makeId(
      @NonNull String systemId,
      @Nullable String watchlistId,
      @Nullable Instant lastResetDecisionDate) {
    return ObjectId
        .newBuilder()
        .setId(Uuids.random())
        .setSourceId(makeSourceId(systemId, watchlistId))
        .setDiscriminator(String.valueOf(lastResetDecisionDate))
        .build();
  }

  private static String makeSourceId(@NonNull String systemId, @Nullable String watchlistId) {
    return isNotEmpty(watchlistId) ?
           String.join(SPLIT_ID_CHARACTER, systemId, watchlistId) :
           systemId;
  }
}
