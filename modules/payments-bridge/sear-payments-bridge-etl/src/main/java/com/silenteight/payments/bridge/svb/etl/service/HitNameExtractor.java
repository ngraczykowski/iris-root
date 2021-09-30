package com.silenteight.payments.bridge.svb.etl.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.payments.bridge.common.dto.input.RequestHitDto;

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
