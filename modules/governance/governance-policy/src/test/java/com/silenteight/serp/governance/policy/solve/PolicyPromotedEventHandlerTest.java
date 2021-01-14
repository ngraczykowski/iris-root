package com.silenteight.serp.governance.policy.solve;

import com.silenteight.serp.governance.policy.domain.PolicyPromotedEvent;
import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.FeatureDto;
import com.silenteight.serp.governance.policy.domain.dto.FeatureLogicDto;
import com.silenteight.serp.governance.policy.domain.dto.StepDto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static com.silenteight.proto.governance.v1.api.FeatureVectorSolution.SOLUTION_FALSE_POSITIVE;
import static com.silenteight.proto.governance.v1.api.FeatureVectorSolution.SOLUTION_POTENTIAL_TRUE_POSITIVE;
import static com.silenteight.serp.governance.policy.domain.StepType.MANUAL_RULE;
import static java.util.UUID.fromString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PolicyPromotedEventHandlerTest {

  @Mock
  private PolicyService policyService;

  @Mock
  private StepPolicyFactory stepPolicyFactory;

  @InjectMocks
  private PolicyPromotedEventHandler underTest;

  @Test
  void handlePolicyPromotedEvent() {
    // given
    UUID policyId = fromString("01256804-1ce1-4d52-94d4-d1876910f272");
    List<StepDto> steps = createSteps();
    PolicyPromotedEvent event = PolicyPromotedEvent.builder()
                                                   .policyId(policyId)
                                                   .build();
    when(policyService.getPolicySteps(policyId)).thenReturn(steps);

    // when
    underTest.handle(event);

    // then
    verify(stepPolicyFactory).reconfigure(steps);
  }

  private static List<StepDto> createSteps() {
    return List.of(
        StepDto.builder()
               .id(fromString("de1afe98-0b58-4941-9791-4e081f9b8139"))
               .name("step-1")
               .type(MANUAL_RULE)
               .solution(SOLUTION_FALSE_POSITIVE)
               .featureLogics(
                   List.of(
                       FeatureLogicDto.builder()
                                      .count(2)
                                      .features(
                                          List.of(
                                              FeatureDto.builder()
                                                        .name("nameAgent")
                                                        .values(
                                                            List.of("PERFECT_MATCH", "NEAR_MATCH"))
                                                        .build(),
                                              FeatureDto.builder()
                                                        .name("dateAgent")
                                                        .values(List.of("EXACT", "NEAR"))
                                                        .build()))
                                      .build()))
               .build(),
        StepDto.builder()
               .id(fromString("de1afe98-0b58-4941-9791-4e081f9b8139"))
               .name("step-2")
               .type(MANUAL_RULE)
               .solution(SOLUTION_POTENTIAL_TRUE_POSITIVE)
               .featureLogics(
                   List.of(
                       FeatureLogicDto.builder()
                                      .count(1)
                                      .features(
                                          List.of(
                                              FeatureDto.builder()
                                                        .name("documentAgent")
                                                        .values(List.of("MATCH", "DIGIT_MATCH"))
                                                        .build()))
                                      .build()))
               .build());
  }
}
