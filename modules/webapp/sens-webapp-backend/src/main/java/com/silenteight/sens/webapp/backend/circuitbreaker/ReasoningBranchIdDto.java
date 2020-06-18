package com.silenteight.sens.webapp.backend.circuitbreaker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

import static java.lang.Long.parseLong;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReasoningBranchIdDto {

  private static final Pattern BRANCH_ID_DELIMITER_PATTERN = Pattern.compile("-");

  private long decisionTreeId;
  private long featureVectorId;

  static ReasoningBranchIdDto valueOf(String branchId) {
    String[] branchIdParts = BRANCH_ID_DELIMITER_PATTERN.split(branchId);
    if (branchIdParts.length != 2) {
      throw new InvalidBranchIdException();
    }
    try {
      long decisionTreeId = parseLong(branchIdParts[0]);
      long featureVectorId = parseLong(branchIdParts[1]);
      return new ReasoningBranchIdDto(decisionTreeId, featureVectorId);
    } catch (NumberFormatException e) {
      throw new InvalidBranchIdException(e);
    }
  }
}
