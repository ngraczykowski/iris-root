package com.silenteight.warehouse.indexer.match.mapping;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.indexer.support.PayloadConverter;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.INDEX_TIMESTAMP;
import static com.silenteight.warehouse.indexer.match.mapping.MatchMapperConstants.DISCRIMINATOR;
import static com.silenteight.warehouse.indexer.match.mapping.MatchMapperConstants.MATCH_NAME;
import static com.silenteight.warehouse.indexer.match.mapping.MatchMapperConstants.MATCH_PREFIX;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static java.util.Optional.ofNullable;
import static java.util.function.Predicate.not;

@RequiredArgsConstructor
public class MatchMapper {

  @NonNull
  private final TimeSource timeSource;
  @NonNull
  private final PayloadConverter payloadConverter;

  public Map<String, Object> convertMatchToAttributes(
      Map<String, Object> alertAttributes, MatchDefinition match) {

    OffsetDateTime now = timeSource.now().atOffset(UTC);

    Map<String, Object> documentAttributes = new LinkedHashMap<>(alertAttributes);
    documentAttributes.putAll(
        payloadConverter.convertPayloadToMap(match.getPayload(), MATCH_PREFIX));
    documentAttributes.put(INDEX_TIMESTAMP, now.format(ISO_DATE_TIME));
    documentAttributes.put(DISCRIMINATOR, match.getDiscriminator());

    extractMatchName(match)
        .ifPresent(matchName -> documentAttributes.put(MATCH_NAME, matchName));

    return documentAttributes;
  }

  private Optional<String> extractMatchName(MatchDefinition match) {
    return ofNullable(match.getName())
        .filter(not(String::isEmpty));
  }
}
