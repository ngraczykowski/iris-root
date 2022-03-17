package com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.support;

import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.Synonym;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class SynonymsList extends ArrayList<Synonym> {

  private static final long serialVersionUID = 8209377434824955481L;

  private static final String DELIMITER = ",";
  private static final int NOT_FOUND_INDEX = -1;

  public List<String> asListOfNames() {
    return stream().map(Synonym::getText).collect(toList());
  }

  public Set<String> asSetOfNames() {
    return stream().map(Synonym::getText).collect(toSet());
  }

  public void merge(List<Synonym> otherList) {
    for (Synonym otherSynonym : otherList) {
      int indexToReplace = findByText(otherSynonym.getText());
      if (indexToReplace >= 0) {
        Synonym synonymToReplace = get(indexToReplace);
        if (otherSynonym.isActive() && !synonymToReplace.isActive())
          set(indexToReplace, otherSynonym);
      } else {
        add(otherSynonym);
      }
    }
  }

  @Override
  public boolean add(Synonym synonym) {
    // XXX(ahaczewski): Don't allow adding synonyms with text being empty or null. According
    //  to Collection#add() we should be throwing the IllegalArgumentException when
    //  element does not meet criteria, but I don't believe in hits details parser tests (they
    //  did not uncovered this bug), and synonyms are external data that we're dealing with,
    //  therefore we'll forgive bogus synonyms, we'll just won't add them to the list.
    if (StringUtils.isEmpty(synonym.getText()))
      return false;
    else
      return super.add(synonym);
  }

  private int findByText(String text) {
    return IntStream
        .range(0, size())
        .filter(index -> get(index).getText().equals(text))
        .findAny()
        .orElse(NOT_FOUND_INDEX);
  }

  public boolean hasAnyActive() {
    return IntStream.range(0, size()).anyMatch(index -> get(index).isActive());
  }

  public boolean hasActiveByText(String text) {
    int index = findByText(text);
    if (index >= 0)
      return get(index).isActive();
    else
      return false;
  }

  @Override
  public String toString() {
    return this.stream().map(Synonym::getText).collect(Collectors.joining(DELIMITER));
  }
}
