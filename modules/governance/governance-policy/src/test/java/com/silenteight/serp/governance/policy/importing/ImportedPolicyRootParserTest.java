package com.silenteight.serp.governance.policy.importing;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.time.Instant;

import static com.silenteight.proto.governance.v1.api.FeatureVectorSolution.SOLUTION_FALSE_POSITIVE;
import static com.silenteight.serp.governance.policy.domain.Condition.IS;
import static com.silenteight.serp.governance.policy.domain.StepType.BUSINESS_LOGIC;
import static java.util.List.of;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ImportedPolicyRootParserTest {

  private ImportedPolicyRootParser underTest;

  @BeforeEach
  void setUp() {
    underTest = new ImportedPolicyRootParser();
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
    ImportedPolicyRoot root = underTest.parse(inputStream);

    // then
    ImportedMetadata importedMetadata = root.getMetadata();
    assertThat(importedMetadata.getExportedAt())
        .isEqualTo(Instant.parse("2020-04-15T10:58:53.451Z"));

    ImportedPolicy importedPolicy = root.getPolicy();
    assertThat(importedPolicy.getName()).isEqualTo("policy-name");
    assertThat(importedPolicy.getPolicyId())
        .isEqualTo(fromString("01256804-1ce1-4d52-94d4-d1876910f272"));
    assertThat(importedPolicy.getSteps()).hasSize(1);

    ImportedStep importedStep = importedPolicy.getSteps().get(0);
    assertThat(importedStep.getSolution()).isEqualTo(SOLUTION_FALSE_POSITIVE);
    assertThat(importedStep.getStepId())
        .isEqualTo(fromString("de1afe98-0b58-4941-9791-4e081f9b8139"));
    assertThat(importedStep.getName()).isEqualTo("branchId=1");
    assertThat(importedStep.getDescription()).isEqualTo("This is step description");
    assertThat(importedStep.getType()).isEqualTo(BUSINESS_LOGIC);
    assertThat(importedStep.getFeatureLogics()).hasSize(2);
    assertThat(importedStep.getFeatureLogics())
        .extracting(ImportedFeatureLogic::getToFulfill).containsExactly(2, 2);
    assertThat(importedStep.getFeatureLogics())
        .extracting(ImportedFeatureLogic::getMatchConditions)
        .containsExactly(
            of(
                new MatchCondition("apType", IS, of("I")),
                new MatchCondition("nameAgent", IS, of("HQ_NO_MATCH"))
            ),
            of(
                new MatchCondition("genderAgent", IS, of("NO_DATA")),
                new MatchCondition("dateAgent", IS, of("NO_DATA")),
                new MatchCondition("nationalIdAgent", IS, of("NO_DATA")),
                new MatchCondition("passportAgent", IS, of("NO_DATA"))
            ));
  }

  private static InputStream loadResource(String resource) {
    return ImportedPolicyRootParser.class.getResourceAsStream(resource);
  }
}
