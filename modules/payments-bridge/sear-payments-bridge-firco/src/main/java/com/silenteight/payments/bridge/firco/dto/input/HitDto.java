package com.silenteight.payments.bridge.firco.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.dto.common.SolutionType;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class HitDto implements Serializable {

  private static final long serialVersionUID = 2413139844772921911L;

  @JsonProperty("MatchingText")
  private String matchingText; // "OFAC"

  @JsonProperty("Positions")
  private List<RequestPositionDto> positions;

  @JsonProperty("Score")
  private String score; // "0.0"

  @JsonProperty("Tag")
  private String tag; // "MTS_BNI"

  @JsonProperty("SolutionType")
  private String solutionType; // "1"

  @JsonProperty("SynonymIndex")
  private String synonymIndex; // "0"

  @JsonProperty("RulesContext")
  private RulesContextDto rulesContex;

  @JsonProperty("EntityText")
  private String entityText; // "OFAC"

  @JsonProperty("HittedEntity")
  private HittedEntityDto hittedEntity;

  String extractWlName() {
    String wlName = "";
    if (SolutionType.NAME.getCode().equals(getSolutionType())) {
      try {
        int parsedIndex;
        if (getSynonymIndex().isEmpty() && getHittedEntity().getNames().size() > 0) {
          parsedIndex = 0;
        } else {
          parsedIndex = Integer.parseInt(getSynonymIndex());
        }
        wlName = (parsedIndex == -1) ? "" : getHittedEntity()
            .getNames()
            .get(parsedIndex)
            .getName()
            .trim();
      } catch (@SuppressWarnings("java:S1696") NullPointerException
          | NumberFormatException | IndexOutOfBoundsException exception) {
        log.warn(
            "There is no watchlist party name at index [{}] of [Hit][HittedEntity][Names]",
            getSynonymIndex());
      }
    } else {
      log.debug(
          "SolutionType [{}] indicates not a hit on NAME, watchlist party name is empty",
          getSolutionType());
    }
    return wlName;
  }
}
