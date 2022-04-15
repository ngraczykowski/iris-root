package com.silenteight.sens.webapp.backend.parser;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.backend.parser.exception.InvalidReasoningBranchIdException;

import java.util.regex.Pattern;

import static java.lang.Long.parseLong;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReasoningBranchIdParser {

  private static final Pattern BRANCH_ID_DELIMITER_PATTERN = Pattern.compile("-");

  public static ParsedReasoningBranchId parse(String branchId) {
    String[] branchIdParts = BRANCH_ID_DELIMITER_PATTERN.split(branchId);
    if (branchIdParts.length != 2) {
      throw new InvalidReasoningBranchIdException(branchId);
    }

    try {
      long decisionTreeId = parseLong(branchIdParts[0]);
      long featureVectorId = parseLong(branchIdParts[1]);

      return new ParsedReasoningBranchId(decisionTreeId, featureVectorId);
    } catch (NumberFormatException e) {
      throw new InvalidReasoningBranchIdException(branchId, e);
    }
  }
}
