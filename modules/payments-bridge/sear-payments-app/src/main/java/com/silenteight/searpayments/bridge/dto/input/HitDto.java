package com.silenteight.searpayments.bridge.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

import static com.silenteight.tsaas.bridge.model.SolutionType.NAME;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class HitDto implements Serializable {

  private static final long serialVersionUID = 2413139844772921911L;
  @JsonProperty("MatchingText")
  String matchingText; // "OFAC"
  @JsonProperty("Positions")
  List<RequestPositionDto> positions;
  @JsonProperty("Score")
  String score; // "0.0"
  @JsonProperty("Tag")
  String tag; // "MTS_BNI"
  @JsonProperty("SolutionType")
  String solutionType; // "1"
  @JsonProperty("SynonymIndex")
  String synonymIndex; // "0"
  @JsonProperty("RulesContext")
  RulesContextDto rulesContex;
  @JsonProperty("EntityText")
  String entityText; // "OFAC"
  @JsonProperty("HittedEntity")
  HittedEntityDto hittedEntity;

  String extractWlName() {
    String wlName = "";
    if (NAME.getCode().equals(getSolutionType())) {
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
