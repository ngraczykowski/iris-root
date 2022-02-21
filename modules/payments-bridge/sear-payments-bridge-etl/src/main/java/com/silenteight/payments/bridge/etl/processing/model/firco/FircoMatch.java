package com.silenteight.payments.bridge.etl.processing.model.firco;

import lombok.*;

import java.util.List;

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
}
