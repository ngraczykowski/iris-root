package com.silenteight.sens.webapp.user.sync.analyst.bulk;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class BulkResult {

  private static final String MESSAGE_JOINER = " / ";

  private final List<SingleResult> singleResults;

  public String asMessage() {
    return getSuccessResultCount() + MESSAGE_JOINER + getAllResultsSize();
  }

  private long getSuccessResultCount() {
    return singleResults.stream().filter(SingleResult::isSuccess).count();
  }

  private long getAllResultsSize() {
    return singleResults.size();
  }

  public List<String> errorMessagesWithMaxSizeOf(int maxSize) {
    return singleResults
        .stream()
        .filter(singleResult -> !singleResult.isSuccess())
        .map(SingleResult::getMessage)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .limit(maxSize)
        .collect(toList());
  }
}
