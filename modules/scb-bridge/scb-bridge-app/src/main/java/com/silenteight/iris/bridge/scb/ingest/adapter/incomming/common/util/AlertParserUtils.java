/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.ObjectId;

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
  public static final String UNSPECIFIED_CLIENT_TYPE = "UNSPECIFIED";

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
      apType = UNSPECIFIED_CLIENT_TYPE;

    return apType;
  }

  public static ObjectId makeWatchlistPartyId(@NonNull String ofacId, @Nullable String batchId) {
    return ObjectId.builder()
        .id(UUID.randomUUID())
        .sourceId(ofacId)
        .discriminator(nullToEmpty(batchId))
        .build();
  }

  public static ObjectId makeAlertedPartyId(
      @NonNull String recordId, @NonNull String recordSignature) {
    return ObjectId.builder()
        .id(UUID.randomUUID())
        .sourceId(recordId)
        .discriminator(recordSignature)
        .build();
  }

  public static ObjectId makeId(
      @NonNull String systemId,
      @Nullable String watchlistId,
      @Nullable Instant lastResetDecisionDate) {
    return ObjectId
        .builder()
        .id(UUID.randomUUID())
        .sourceId(makeSourceId(systemId, watchlistId))
        .discriminator(String.valueOf(lastResetDecisionDate))
        .build();
  }

  private static String makeSourceId(@NonNull String systemId, @Nullable String watchlistId) {
    return isNotEmpty(watchlistId) ?
           String.join(SPLIT_ID_CHARACTER, systemId, watchlistId) :
           systemId;
  }
}
