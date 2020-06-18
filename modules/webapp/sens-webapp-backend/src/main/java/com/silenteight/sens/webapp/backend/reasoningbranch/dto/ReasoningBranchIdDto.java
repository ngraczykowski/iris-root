package com.silenteight.sens.webapp.backend.reasoningbranch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.backend.reasoningbranch.exception.InvalidBranchIdException;

import static java.lang.Long.parseLong;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReasoningBranchIdDto {

  private static final String BRANCH_ID_DELIMITER = "-";

  private long decisionTreeId;
  private long featureVectorId;

  public static ReasoningBranchIdDto valueOf(String id) {
    String[] branchIdParts = id.split(BRANCH_ID_DELIMITER);
    if (branchIdParts.length != 2) {
      throw new InvalidBranchIdException();
    }

    try {
      long decisionTreeId = parseLong(branchIdParts[0]);
      long featureVectorId = parseLong(branchIdParts[1]);

      return new ReasoningBranchIdDto(decisionTreeId, featureVectorId);
    } catch (NumberFormatException e) {
      throw new InvalidBranchIdException();
    }
  }
}
