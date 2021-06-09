package com.silenteight.serp.governance.policy.transfer.importing;

import com.silenteight.serp.governance.policy.transfer.dto.*;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.time.Instant;

import static com.silenteight.serp.governance.policy.domain.Condition.IS;
import static com.silenteight.serp.governance.policy.domain.StepType.BUSINESS_LOGIC;
import static com.silenteight.solving.api.v1.FeatureVectorSolution.SOLUTION_FALSE_POSITIVE;
import static java.util.List.of;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransferredPolicyRootParserTest {

  private TransferredPolicyRootDtoParser underTest;

  @BeforeEach
  void setUp() {
    underTest = new TransferredPolicyRootDtoParser();
  }

  @Test
  void throwsPolicyImportExceptionWhenJsonIsEmpty() {
    // given
    InputStream inputStream = loadResource("emptyPolicy.json");

    // when
    ThrowingCallable parseCall = () -> underTest.parse(inputStream);

    // then
    assertThatThrownBy(parseCall).isInstanceOf(PolicyImportException.class);
  }

  @Test
  void throwsPolicyImportExceptionWhenJsonHasInvalidSyntax() {
    // given
    InputStream inputStream = loadResource("invalidSyntaxPolicy.json");

    // when
    ThrowingCallable parseCall = () -> underTest.parse(inputStream);

    // then
    assertThatThrownBy(parseCall).isInstanceOf(PolicyImportException.class);
  }

  @Test
  void parseJson() {
    // given
    InputStream inputStream = loadResource("correctPolicy.json");

    // when
    TransferredPolicyRootDto root = underTest.parse(inputStream);

    // then
    TransferredPolicyMetadataDto transferredPolicyMetadata = root.getMetadata();
    assertThat(transferredPolicyMetadata.getExportedAt())
        .isEqualTo(Instant.parse("2020-04-15T10:58:53.451Z"));

    TransferredPolicyDto transferredPolicy = root.getPolicy();
    assertThat(transferredPolicy.getName()).isEqualTo("policy-name");
    assertThat(transferredPolicy.getPolicyId())
        .isEqualTo(fromString("01256804-1ce1-4d52-94d4-d1876910f272"));
    assertThat(transferredPolicy.getSteps()).hasSize(1);

    TransferredStepDto transferredStep = transferredPolicy.getSteps().get(0);
    assertThat(transferredStep.getSolution()).isEqualTo(SOLUTION_FALSE_POSITIVE);
    assertThat(transferredStep.getStepId())
        .isEqualTo(fromString("de1afe98-0b58-4941-9791-4e081f9b8139"));
    assertThat(transferredStep.getName()).isEqualTo("branchId=1");
    assertThat(transferredStep.getDescription()).isEqualTo("This is step description");
    assertThat(transferredStep.getType()).isEqualTo(BUSINESS_LOGIC);
    assertThat(transferredStep.getFeatureLogics()).hasSize(2);
    assertThat(transferredStep.getFeatureLogics())
        .extracting(TransferredFeatureLogicDto::getToFulfill).containsExactly(2, 2);
    assertThat(transferredStep.getFeatureLogics())
        .extracting(TransferredFeatureLogicDto::getMatchConditions)
        .containsExactly(
            of(
                new TransferredMatchConditionDto("apType", IS, of("I")),
                new TransferredMatchConditionDto("nameAgent", IS, of("HQ_NO_MATCH"))
            ),
            of(
                new TransferredMatchConditionDto("genderAgent", IS, of("NO_DATA")),
                new TransferredMatchConditionDto("dateAgent", IS, of("NO_DATA")),
                new TransferredMatchConditionDto("nationalIdAgent", IS, of("NO_DATA")),
                new TransferredMatchConditionDto("passportAgent", IS, of("NO_DATA"))
            ));
  }

  private static InputStream loadResource(String resource) {
    return TransferredPolicyRootDtoParser.class.getResourceAsStream(resource);
  }
}
