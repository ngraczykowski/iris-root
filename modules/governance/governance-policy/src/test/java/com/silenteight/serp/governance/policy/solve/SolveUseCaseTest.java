package com.silenteight.serp.governance.policy.solve;

import com.silenteight.governance.api.v1.*;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.serp.governance.common.signature.CanonicalFeatureVectorFactory;
import com.silenteight.serp.governance.common.signature.Signature;
import com.silenteight.serp.governance.common.signature.SignatureCalculator;
import com.silenteight.serp.governance.policy.domain.Condition;
import com.silenteight.serp.governance.policy.solve.amqp.FeatureVectorSolvedMessageGatewayMock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.silenteight.governance.api.utils.Timestamps.toTimestamp;
import static com.silenteight.governance.api.v1.FeatureVectorSolution.SOLUTION_FALSE_POSITIVE;
import static com.silenteight.governance.api.v1.FeatureVectorSolution.SOLUTION_NO_DECISION;
import static com.silenteight.governance.api.v1.FeatureVectorSolution.SOLUTION_POTENTIAL_TRUE_POSITIVE;
import static com.silenteight.protocol.utils.Uuids.fromJavaUuid;
import static com.silenteight.serp.governance.GovernanceProtoUtils.featureCollection;
import static com.silenteight.serp.governance.GovernanceProtoUtils.featureVector;
import static com.silenteight.serp.governance.GovernanceProtoUtils.solveFeaturesRequest;
import static com.silenteight.serp.governance.common.signature.Signature.fromBase64;
import static com.silenteight.serp.governance.policy.domain.Condition.IS;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class SolveUseCaseTest {

  private static final UUID STEP_ID_1 = fromString("01256804-1ce1-4d52-94d4-d1876910f272");
  private static final UUID STEP_ID_2 = fromString("de1afe98-0b58-4941-9791-4e081f9b8139");
  private static final Instant NOW = Instant.ofEpochMilli(1566469674663L);
  private static final Signature SIGNATURE_1 = fromBase64("o7uPxWV913+ljhPW2uH+g7eAFeQ=");
  private static final Signature SIGNATURE_2 = fromBase64("BWrL65LzOy8daIJSWiZCRxG96XA=");

  private ReconfigurableStepsSupplier stepPolicyFactory;
  private FeatureVectorSolvedMessageGatewayMock gateway;
  private SolveUseCase underTest;

  @BeforeEach
  void setUp() {
    stepPolicyFactory = Mockito.mock(ReconfigurableStepsSupplier.class);
    gateway = new FeatureVectorSolvedMessageGatewayMock();
    CanonicalFeatureVectorFactory canonicalFeatureVectorFactory =
        new CanonicalFeatureVectorFactory(new SignatureCalculator());
    TimeSource fixed = Mockito.mock(TimeSource.class);
    when(fixed.now()).thenReturn(NOW);
    underTest = new SolveUseCase(stepPolicyFactory, new SolvingService(),
        gateway, canonicalFeatureVectorFactory, fixed);
  }

  @Test
  void matchedSolutionReturnsCorrectly() {
    // given
    List<Step> steps = defaultStepsConfiguration();
    when(stepPolicyFactory.get()).thenReturn(steps);
    SolveFeaturesRequest solutionsRequest = solveFeaturesRequest(
        featureCollection("nameAgent", "dateAgent"),
        featureVector("PERFECT_MATCH", "EXACT"),
        featureVector("WEAK_MATCH", "WEAK"));

    // when
    SolveFeaturesResponse response = underTest.solve(solutionsRequest);

    // then
    SolveFeaturesResponseAssert.assertThat(response)
        .hasSolutionsCount(2)
        .solution(0)
        .hasStepId(STEP_ID_2)
        .hasSolution(SOLUTION_POTENTIAL_TRUE_POSITIVE)
        .hasVectorSignature(SIGNATURE_1)
        .and()
        .solution(1)
        .hasStepId(STEP_ID_1)
        .hasSolution(SOLUTION_FALSE_POSITIVE)
        .hasVectorSignature(SIGNATURE_2);
  }

  @Test
  void defaultSolutionReturnsWhenNoStepMatchesValues() {
    // given
    List<Step> steps = defaultStepsConfiguration();
    when(stepPolicyFactory.get()).thenReturn(steps);
    SolveFeaturesRequest solutionsRequest = solveFeaturesRequest(
        featureCollection("nameAgent"),
        featureVector("OUT_OF_RANGE"));

    // when
    SolveFeaturesResponse response = underTest.solve(solutionsRequest);

    // then
    SolveFeaturesResponseAssert.assertThat(response)
        .hasSolutionsCount(1)
        .solution(0)
        .hasNoStepId()
        .hasSolution(SOLUTION_NO_DECISION);
  }

  @Test
  void shouldEmitEvent() {
    // given
    List<Step> steps = defaultStepsConfiguration();
    when(stepPolicyFactory.get()).thenReturn(steps);
    FeatureCollection featureCollection = featureCollection("nameAgent", "dateAgent");
    FeatureVector featureVector = featureVector("PERFECT_MATCH", "EXACT");
    SolveFeaturesRequest solutionsRequest = solveFeaturesRequest(featureCollection, featureVector);

    // when
    underTest.solve(solutionsRequest);

    // then
    FeatureVectorSolvedEvent event = gateway.getLastEvent();
    assertThat(event).isNotNull();
    assertThat(event.getStepId()).isEqualTo(fromJavaUuid(STEP_ID_2));
    assertThat(event.getCreatedAt()).isEqualTo(toTimestamp(NOW));
    assertThat(event.getId()).isNotNull();
    assertThat(event.getCorrelationId()).isNotNull();
    assertThat(event.getFeatureVectorSignature()).isEqualTo(SIGNATURE_1.getValue());
    assertThat(event.getFeatureCollection()).isEqualTo(featureCollection);
    assertThat(event.getFeatureVector()).isEqualTo(featureVector);
  }

  private static List<Step> defaultStepsConfiguration() {
    return List.of(
        new Step(
            SOLUTION_FALSE_POSITIVE,
            STEP_ID_1,
            List.of(
                createFeatureLogic(
                    2,
                    List.of(
                        createMatchCondition(
                            "nameAgent", IS, List.of("WEAK_MATCH", "MO_MATCH")),
                        createMatchCondition(
                            "dateAgent", IS, List.of("WEAK")))))),
        new Step(
            SOLUTION_POTENTIAL_TRUE_POSITIVE,
            STEP_ID_2,
            List.of(
                createFeatureLogic(
                    2,
                    List.of(
                        createMatchCondition(
                            "nameAgent", IS, List.of("PERFECT_MATCH", "NEAR_MATCH")),
                        createMatchCondition(
                            "dateAgent", IS, List.of("EXACT")),
                        createMatchCondition(
                            "documentAgent", IS, List.of("MATCH", "DIGIT_MATCH")))))));
  }

  private static FeatureLogic createFeatureLogic(int count, Collection<MatchCondition> features) {
    return FeatureLogic.builder()
        .count(count)
        .features(features)
        .build();
  }

  private static MatchCondition createMatchCondition(
      String name, Condition condition, Collection<String> values) {

    return MatchCondition.builder()
        .name(name)
        .condition(condition)
        .values(values)
        .build();
  }
}
