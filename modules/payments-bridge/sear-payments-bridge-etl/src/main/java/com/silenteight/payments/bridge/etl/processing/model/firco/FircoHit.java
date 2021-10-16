package com.silenteight.payments.bridge.etl.processing.model.firco;

import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.dto.common.SolutionType;
import com.silenteight.payments.bridge.etl.processing.model.MessageData;

import java.util.Optional;
import javax.annotation.Nonnull;

@Value
@Builder
@Slf4j
public class FircoHit {

  String matchingText;
  Position position;
  String score;
  String tag;
  SolutionType solutionType;
  int synonymIndex;
  RulesContext rulesContext;
  String entityText;
  FircoWatchlistParty watchlistParty;

  public Optional<String> getMatchedPartyName() {
    if (solutionType != SolutionType.NAME) {
      log.debug(
          "SolutionType [{}] indicates not a hit on NAME, watchlist party name is empty",
          getSolutionType());
      return Optional.empty();
    }

    if (synonymIndex < 0) {
      log.warn("Cannot determine matched party name, synonym index is less than zero.");
      return Optional.empty();
    }

    if (synonymIndex >= watchlistParty.getNames().size()) {
      log.warn("Synonym index {} points at non-existing watchlist party name (max index {}).",
          synonymIndex, watchlistParty.getNames().size() - 1);
      return Optional.empty();
    }

    return Optional.of(watchlistParty.getNames().get(synonymIndex));
  }

  @Nonnull
  public String getHitTagValue(MessageData messageData) {
    return messageData.findFirstValue(tag).orElse("");
  }

  @Value
  public static class Position {

    int start;
    int end;
  }

  @Value
  public static class RulesContext {

    String type;
    String priority;
    String confidentiality;
    String info;
  }
}
