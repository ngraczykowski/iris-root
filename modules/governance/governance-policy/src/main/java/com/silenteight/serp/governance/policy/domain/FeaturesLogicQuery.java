package com.silenteight.serp.governance.policy.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.dto.FeatureLogicDto;
import com.silenteight.serp.governance.policy.domain.dto.FeaturesLogicDto;
import com.silenteight.serp.governance.policy.step.logic.FeatureLogicRequestQuery;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Transactional(readOnly = true)
class FeaturesLogicQuery implements FeatureLogicRequestQuery {

  private final StepRepository stepRepository;
  private final FeatureLogicRepository featureLogicRepository;

  @Override
  public FeaturesLogicDto listStepsFeaturesLogic(UUID stepId) {
    Long id = stepRepository.getIdByStepId(stepId);
    List<FeatureLogicDto> featureLogicDtos = featureLogicRepository
        .findAllLogicByStepId(id)
        .stream()
        .map(FeatureLogic::toDto)
        .collect(toList());

    return FeaturesLogicDto.builder()
        .featuresLogic(featureLogicDtos)
        .build();
  }
}
