package com.silenteight.payments.bridge.etl.processing.model.firco;

import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.dto.common.SolutionType;
import com.silenteight.payments.bridge.etl.processing.model.MessageData;

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
