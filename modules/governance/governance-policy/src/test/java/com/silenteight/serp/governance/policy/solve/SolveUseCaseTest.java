package com.silenteight.serp.governance.policy.solve;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.serp.governance.common.signature.CanonicalFeatureVectorFactory;
import com.silenteight.serp.governance.common.signature.Signature;
import com.silenteight.serp.governance.common.signature.SignatureCalculator;
import com.silenteight.serp.governance.policy.domain.Condition;
import com.silenteight.serp.governance.policy.solve.amqp.FeatureVectorSolvedMessageGatewayMock;
import com.silenteight.solving.api.v1.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.silenteight.protocol.utils.Uuids.fromJavaUuid;
import static com.silenteight.serp.governance.GovernanceProtoUtils.featureCollection;
import static com.silenteight.serp.governance.GovernanceProtoUtils.featureVector;
import static com.silenteight.serp.governance.GovernanceProtoUtils.solveFeaturesRequest;
import static com.silenteight.serp.governance.common.signature.Signature.fromBase64;
import static com.silenteight.serp.governance.policy.domain.Condition.IS;
import static com.silenteight.solving.api.utils.Timestamps.toTimestamp;
import static com.silenteight.solving.api.v1.FeatureVectorSolution.SOLUTION_FALSE_POSITIVE;
import static com.silenteight.solving.api.v1.FeatureVectorSolution.SOLUTION_NO_DECISION;
import static com.silenteight.solving.api.v1.FeatureVectorSolution.SOLUTION_POTENTIAL_TRUE_POSITIVE;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class SolveUseCaseTest {

  private static final String POLICY_NAME_RESOURCE_PREFIX = "policies/";
  private static final String STEP_NAME_RESOURCE_PREFIX = "steps/";
  private static final UUID POLICY_ID = fromString("1f9b8139-9791-1ce1-0b58-4e08de1afe98");
  private static final String POLICY_NAME = POLICY_NAME_RESOURCE_PREFIX + POLICY_ID;
  private static final UUID STEP_ID_1 = fromString("01256804-1ce1-4d52-94d4-d1876910f272");
  private static final String STEP_NAME_1 = STEP_NAME_RESOURCE_PREFIX + STEP_ID_1;
  private static final String STEP_TITLE_1 = "First step";
  private static final UUID STEP_ID_2 = fromString("de1afe98-0b58-4941-9791-4e081f9b8139");
  private static final String STEP_NAME_2 = STEP_NAME_RESOURCE_PREFIX + STEP_ID_2;
  private static final String STEP_TITLE_2 = "Second step";
  private static final Instant NOW = Instant.ofEpochMilli(1566469674663L);
  private static final Signature SIGNATURE_1 = fromBase64("o7uPxWV913+ljhPW2uH+g7eAFeQ=");
  private static final Signature SIGNATURE_2 = fromBase64("BWrL65LzOy8daIJSWiZCRxG96XA=");
  private static final String POLICY_TITLE = "Test Policy";

  private StepsSupplier stepsSupplier;
  private FeatureVectorSolvedMessageGatewayMock gateway;
  private SolveUseCase underTest;

  @BeforeEach
  void setUp() {
    stepsSupplier = mock(StepsSupplier.class);
    gateway = new FeatureVectorSolvedMessageGatewayMock();
    CanonicalFeatureVectorFactory canonicalFeatureVectorFactory =
        new CanonicalFeatureVectorFactory(new SignatureCalculator());
    TimeSource fixed = mock(TimeSource.class);
    when(fixed.now()).thenReturn(NOW);
    PolicyTitleQuery policyTitleQuery = mock(PolicyTitleQuery.class);
    when(policyTitleQuery.getTitle(POLICY_ID)).thenReturn(POLICY_TITLE);
    underTest = new SolveUseCase(
        policyId -> stepsSupplier,
        new SolvingService(),
        gateway,
        canonicalFeatureVectorFactory,
        policyTitleQuery,
        fixed);
  }

  @Test
  void matchedSolutionReturnsCorrectly() {
    // given
    List<Step> steps = defaultStepsConfiguration();
    when(stepsSupplier.get()).thenReturn(steps);
    BatchSolveFeaturesRequest solutionsRequest = solveFeaturesRequest(
        POLICY_NAME,
        featureCollection("nameAgent", "dateAgent"),
        featureVector("PERFECT_MATCH", "EXACT"),
        featureVector("WEAK_MATCH", "WEAK"));

    // when
    BatchSolveFeaturesResponse response = underTest.solve(solutionsRequest);

    // then
    SolveFeaturesResponseAssert.assertThat(response)
        .hasSolutionsCount(2)
        .solution(0)
        .hasStepId(STEP_ID_2)
        .hasSolution(SOLUTION_POTENTIAL_TRUE_POSITIVE)
        .hasVectorSignature(SIGNATURE_1)
        .hasReasonField("feature_vector_signature", SIGNATURE_1.asString())
        .hasReasonField("policy", POLICY_NAME)
        .hasReasonField("policy_title", POLICY_TITLE)
        .hasReasonField("step", STEP_NAME_2)
        .hasReasonField("step_title", STEP_TITLE_2)
        .and()
        .solution(1)
        .hasStepId(STEP_ID_1)
        .hasSolution(SOLUTION_FALSE_POSITIVE)
        .hasVectorSignature(SIGNATURE_2)
        .hasReasonField("feature_vector_signature", SIGNATURE_2.asString())
        .hasReasonField("policy", POLICY_NAME)
        .hasReasonField("step", STEP_NAME_1);
  }

  @Test
  void defaultSolutionReturnsWhenNoStepMatchesValues() {
    // given
    List<Step> steps = defaultStepsConfiguration();
    when(stepsSupplier.get()).thenReturn(steps);
    BatchSolveFeaturesRequest solutionsRequest = solveFeaturesRequest(
        POLICY_NAME,
        featureCollection("nameAgent"),
        featureVector("OUT_OF_RANGE"));

    // when
    BatchSolveFeaturesResponse response = underTest.solve(solutionsRequest);

    // then
    SolveFeaturesResponseAssert.assertThat(response)
        .hasSolutionsCount(1)
        .solution(0)
        .hasNoStepId()
        .hasSolution(SOLUTION_NO_DECISION)
        .hasReasonField("policy", POLICY_NAME);
  }

  @Test
  void shouldEmitEvent() {
    // given
    List<Step> steps = defaultStepsConfiguration();
    when(stepsSupplier.get()).thenReturn(steps);
    FeatureCollection featureCollection = featureCollection("nameAgent", "dateAgent");
    FeatureVector featureVector = featureVector("PERFECT_MATCH", "EXACT");
    BatchSolveFeaturesRequest solutionsRequest =
        solveFeaturesRequest(POLICY_NAME, featureCollection, featureVector);

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
            STEP_TITLE_1,
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
            STEP_TITLE_2,
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
    return new FeatureLogic(count, features);
  }

  private static MatchCondition createMatchCondition(
      String name, Condition condition, Collection<String> values) {

    return new MatchCondition(name, condition, values);
  }
}
