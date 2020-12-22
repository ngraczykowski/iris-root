package com.silenteight.serp.governance.bulkchange;

import com.silenteight.proto.serp.v1.governance.CreateBulkBranchChangeCommand;
import com.silenteight.proto.serp.v1.governance.CreateBulkBranchChangeCommand.BranchSolutionChange;
import com.silenteight.proto.serp.v1.governance.CreateBulkBranchChangeCommand.EnablementChange;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchId;
import com.silenteight.proto.serp.v1.recommendation.BranchSolution;
import com.silenteight.protocol.utils.Uuids;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class BulkBranchChangeMapperTest {

  private final Fixtures fixtures = new Fixtures();

  private BulkBranchChangeMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new BulkBranchChangeMapper();
  }

  @Test
  void shouldMapEmptyRequest() {
    var id = fixtures.emptyCommandId;
    var correlationId = fixtures.correlationId;
    var command = fixtures.emptyCommand;

    var result = mapper.map(command);

    assertThat(result.getBulkBranchChangeId()).isEqualTo(id);
    assertThat(result.getCorrelationId()).isEqualTo(correlationId);
    assertThat(result.getReasoningBranchIds()).isEmpty();
    assertThat(result.getSolutionChange()).isNull();
    assertThat(result.getEnablementChange()).isNull();
  }

  @Test
  void shouldMapRequestWithAllProperties() {
    var id = fixtures.commandWithAllPropertiesId;
    var correlationId = fixtures.correlationId;
    var command = fixtures.commandWithAllProperties;

    var result = mapper.map(command);

    assertThat(result.getBulkBranchChangeId()).isEqualTo(id);
    assertThat(result.getCorrelationId()).isEqualTo(correlationId);
    assertThat(result.getReasoningBranchIds())
        .hasSize(2)
        .anySatisfy(bid -> assertBranchIdAsExpected(bid, fixtures.branch1))
        .anySatisfy(bid -> assertBranchIdAsExpected(bid, fixtures.branch2));
    assertThat(result.getSolutionChange())
        .isEqualTo(command.getSolutionChange().getSolution());
    assertThat(result.getEnablementChange())
        .isEqualTo(command.getEnablementChange().getEnabled());
  }

  private static void assertBranchIdAsExpected(
      ReasoningBranchIdToChange id, ReasoningBranchId expected) {

    assertThat(id.getDecisionTreeId()).isEqualTo(expected.getDecisionTreeId());
    assertThat(id.getFeatureVectorId()).isEqualTo(expected.getFeatureVectorId());
  }

  private static class Fixtures {

    UUID emptyCommandId = UUID.randomUUID();
    UUID correlationId = UUID.randomUUID();
    UUID commandWithAllPropertiesId = UUID.randomUUID();

    CreateBulkBranchChangeCommand emptyCommand = CreateBulkBranchChangeCommand.newBuilder()
        .setId(Uuids.fromJavaUuid(emptyCommandId))
        .setCorrelationId(Uuids.fromJavaUuid(correlationId))
        .build();

    BranchSolutionChange solutionChange = BranchSolutionChange.newBuilder()
        .setSolution(BranchSolution.BRANCH_FALSE_POSITIVE)
        .build();
    EnablementChange enablementChange = EnablementChange.newBuilder()
        .setEnabled(true)
        .build();

    ReasoningBranchId branch1 = ReasoningBranchId.newBuilder()
        .setDecisionTreeId(1)
        .setFeatureVectorId(2)
        .build();
    ReasoningBranchId branch2 = ReasoningBranchId.newBuilder()
        .setDecisionTreeId(2)
        .setFeatureVectorId(3)
        .build();

    CreateBulkBranchChangeCommand commandWithAllProperties =
        CreateBulkBranchChangeCommand.newBuilder()
            .setId(Uuids.fromJavaUuid(commandWithAllPropertiesId))
            .setCorrelationId(Uuids.fromJavaUuid(correlationId))
            .setEnablementChange(enablementChange)
            .setSolutionChange(solutionChange)
            .addReasoningBranchIds(branch1)
            .addReasoningBranchIds(branch2)
            .build();
  }
}
