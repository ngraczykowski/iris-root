package com.silenteight.payments.bridge.svb.oldetl.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.payments.bridge.common.dto.input.HittedEntityDto;

import org.jetbrains.annotations.Nullable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HitNameExtractor {

  @Nullable
  public static String extractName(
      int synonymIndex, HittedEntityDto hittedEntity) {
    if (hittedEntity.getNames().size() > synonymIndex) {
      return hittedEntity.getNames().get(synonymIndex).getName();
    }
    return null;
  }
}
