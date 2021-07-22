package com.silenteight.searpayments.scb.etl.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.payments.bridge.dto.input.RequestHitDto;

import org.jetbrains.annotations.Nullable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HitNameExtractor {

  @Nullable
  public static String extractName(RequestHitDto requestHitDto, int synonymIndex) {
    if (requestHitDto.getHit().getHittedEntity().getNames().size() > synonymIndex) {
      return requestHitDto.getHit().getHittedEntity().getNames().get(synonymIndex).getName();
    }
    return null;
  }
}
