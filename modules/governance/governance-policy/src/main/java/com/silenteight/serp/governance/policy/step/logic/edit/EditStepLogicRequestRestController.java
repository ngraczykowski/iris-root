package com.silenteight.serp.governance.policy.step.logic.edit;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.dto.ConfigurePolicyRequest.FeatureConfiguration;
import com.silenteight.serp.governance.policy.domain.dto.ConfigurePolicyRequest.FeatureLogicConfiguration;
import com.silenteight.serp.governance.policy.step.logic.edit.dto.EditStepLogicDto;
import com.silenteight.serp.governance.policy.step.logic.edit.dto.FeatureLogicDto;
import com.silenteight.serp.governance.policy.step.logic.edit.dto.MatchConditionDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;
import javax.validation.Valid;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class EditStepLogicRequestRestController {

  @NonNull
  private final EditStepLogicUseCase editStepLogicUseCase;

  @PutMapping("/v1/steps/{id}/logic")
  @PreAuthorize("isAuthorized('EDIT_STEPS_LOGIC')")
  public ResponseEntity<Void> edit(
      @PathVariable UUID id,
      @Valid @RequestBody EditStepLogicDto editStepLogicDto,
      Authentication authentication) {

    EditStepLogicCommand command = EditStepLogicCommand
        .builder()
        .stepId(id)
        .featureLogicConfigurations(
            mapToFeaturesLogicConfiguration(editStepLogicDto.getFeaturesLogic()))
        .user(authentication.getName())
        .build();
    editStepLogicUseCase.activate(command);
    return ResponseEntity.accepted().build();
  }

  private Collection<FeatureLogicConfiguration> mapToFeaturesLogicConfiguration(
      @NonNull Collection<FeatureLogicDto> featuresLogic) {

    return featuresLogic
        .stream()
        .map(this::mapToFeatureLogicConfiguration)
        .collect(toList());
  }

  private FeatureLogicConfiguration mapToFeatureLogicConfiguration(
      @NonNull FeatureLogicDto featureLogicDto) {
    return FeatureLogicConfiguration
        .builder()
        .toFulfill(featureLogicDto.getToFulfill())
        .featureConfigurations(mapToFeaturesConfiguration(featureLogicDto.getFeatures()))
        .build();
  }

  private Collection<FeatureConfiguration> mapToFeaturesConfiguration(
      @NonNull Collection<MatchConditionDto> featureLogicConfigurationDto) {

    return featureLogicConfigurationDto
        .stream()
        .map(this::mapToFeatureConfiguration)
        .collect(toList());
  }

  private FeatureConfiguration mapToFeatureConfiguration(MatchConditionDto matchConditionDto) {
    return FeatureConfiguration
        .builder()
        .name(matchConditionDto.getName())
        .condition(matchConditionDto.getCondition())
        .values(matchConditionDto.getValues())
        .build();
  }
}
