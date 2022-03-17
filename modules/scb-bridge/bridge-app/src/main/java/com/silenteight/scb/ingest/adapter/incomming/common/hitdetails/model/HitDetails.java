package com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model;

import lombok.Data;

import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;

@Data
public class HitDetails {

  private static final Pattern STARTS_WITH_DIGITS = Pattern.compile("^\\d+.*");
  private static final Pattern STARTS_WITH_LETTERS = Pattern.compile("^[A-Za-z]+.*");
  private final List<Suspect> suspects = new ArrayList<>();
  private String systemId;
  private Boolean hasSndRcvIn;
  private Integer limited;

  public Collection<Suspect> extractUniqueSuspects() {
    Map<String, List<Suspect>> groupedSuspects = groupSuspectsByOfacId();
    Collection<Suspect> result = new LinkedHashSet<>();
    for (Entry<String, List<Suspect>> entry : groupedSuspects.entrySet()) {
      Optional<Suspect> merged = merge(entry.getValue());
      merged.ifPresent(result::add);
    }
    return result;
  }

  private Map<String, List<Suspect>> groupSuspectsByOfacId() {
    return getSuspects()
        .stream()
        .collect(groupingBy(Suspect::getOfacId, LinkedHashMap::new, Collectors.toList()));
  }

  private static Optional<Suspect> merge(List<Suspect> suspects) {
    String newestBatchId = determineNewestBatchId(suspects)
        .orElse(null);

    return suspects
        .stream()
        .filter(x -> Objects.equals(newestBatchId, x.getBatchId()))
        .reduce(Suspect::merge);
  }

  private static Optional<String> determineNewestBatchId(List<Suspect> suspects) {
    Optional<String> filteredByDigits = getNewestBatchIdByPattern(suspects, STARTS_WITH_DIGITS);
    if (filteredByDigits.isPresent())
      return filteredByDigits;

    Optional<String> filteredByLetters = getNewestBatchIdByPattern(suspects, STARTS_WITH_LETTERS);
    if (filteredByLetters.isPresent())
      return filteredByLetters;

    return getNewestNonNullBatchId(suspects);
  }

  private static Optional<String> getNewestNonNullBatchId(List<Suspect> suspects) {
    return suspects
        .stream()
        .filter(s -> s.getBatchId() != null)
        .sorted(comparing(Suspect::getBatchId).reversed())
        .map(Suspect::getBatchId)
        .findFirst();
  }

  private static Optional<String> getNewestBatchIdByPattern(
      List<Suspect> suspects, Pattern pattern) {

    return suspects
        .stream()
        .filter(s -> s.getBatchId() != null)
        .filter(s -> pattern.matcher(s.getBatchId()).matches())
        .sorted(comparing(Suspect::getBatchId).reversed())
        .map(Suspect::getBatchId)
        .findFirst();
  }
}
