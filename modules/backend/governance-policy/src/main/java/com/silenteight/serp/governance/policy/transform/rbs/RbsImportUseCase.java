package com.silenteight.serp.governance.policy.transform.rbs;

import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
class RbsImportUseCase {

  private final RbsParser rbsParser;
  private final RbsToPolicyTransformationService transformationService;

  @Transactional
  UUID apply(RbsImportCommand command) {
    StepsData stepsData = rbsParser.parse(command.getInputStream(), command.getFileName());
    return transformationService.transform(stepsData);
  }
}
