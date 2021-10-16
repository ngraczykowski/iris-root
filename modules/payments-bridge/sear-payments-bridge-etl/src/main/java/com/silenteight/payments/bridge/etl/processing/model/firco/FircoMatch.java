package com.silenteight.payments.bridge.etl.processing.model.firco;

import lombok.*;

import java.util.List;
import java.util.stream.Stream;

@Value
@Builder
public class FircoMatch {

  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  FircoAlert parentAlert;

  /**
   * Adjudication Engine registered alert name.
   */
  String matchName;

  @Singular
  List<FircoHit> hits;

  Stream<String> findAllTags() {
    return hits.stream().map(FircoHit::getTag);
  }
}
