package com.silenteight.connector.ftcc.ingest.dto.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.connector.ftcc.ingest.dto.output.SolutionType;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@JsonNaming(UpperCamelCaseStrategy.class)
public class HitDto implements Serializable {

  private static final long serialVersionUID = 2413139844772921911L;

  private String matchingText; // "OFAC"

  private List<RequestPositionDto> positions;

  private String score; // "0.0"

  private String tag; // "MTS_BNI"

  private String solutionType; // "1"

  private String synonymIndex; // "0"

  private RulesContextDto rulesContext;

  private String entityText; // "OFAC"

  private HittedEntityDto hittedEntity;

  public String extractWlName() {
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

  public boolean isBlocking() {
    return getRulesContext().getType().equals("1");
  }

  public boolean isNotBlocking() {
    return !isBlocking();
  }

  public String getMatchId(int sequenceIndex) {
    var sequenceNumber = sequenceIndex + 1;
    return hittedEntity.getId() + "(" + getTag() + ", #" + sequenceNumber + ")";
  }

  public List<String> findSearchCodes() {
    return getCodes().stream()
        .filter(codeDto -> "SearchCode".equals(codeDto.getType()))
        .map(CodeDto::getName)
        .collect(Collectors.toList());
  }

  public List<String> findPassports() {
    return getCodes().stream()
        .filter(codeDto -> "Passport".equals(codeDto.getType()))
        .map(CodeDto::getName)
        .collect(Collectors.toList());
  }

  public List<String> findNatIds() {
    return getCodes().stream()
        .filter(codeDto -> "NationalID".equals(codeDto.getType()))
        .map(CodeDto::getName)
        .collect(Collectors.toList());
  }

  public List<String> findBics() {
    return getCodes().stream()
        .filter(codeDto -> "Bic".equals(codeDto.getType()))
        .map(CodeDto::getName)
        .collect(Collectors.toList());
  }

  private List<CodeDto> getCodes() {
    return hittedEntity.findCodes();
  }
}
