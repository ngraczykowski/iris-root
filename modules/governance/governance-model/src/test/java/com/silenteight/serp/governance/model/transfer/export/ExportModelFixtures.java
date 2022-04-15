package com.silenteight.serp.governance.model.transfer.export;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sep.base.common.time.DigitsOnlyDateFormatter;
import com.silenteight.serp.governance.changerequest.approval.dto.ModelApprovalDto;
import com.silenteight.serp.governance.model.domain.dto.ModelDto;
import com.silenteight.serp.governance.model.transfer.dto.TransferredModelDto;
import com.silenteight.serp.governance.model.transfer.dto.TransferredModelMetadataDto;
import com.silenteight.serp.governance.model.transfer.dto.TransferredModelRootDto;
import com.silenteight.serp.governance.policy.domain.Condition;
import com.silenteight.serp.governance.policy.domain.StepType;
import com.silenteight.serp.governance.policy.domain.dto.*;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static com.silenteight.serp.governance.policy.domain.StepType.BUSINESS_LOGIC;
import static com.silenteight.serp.governance.policy.domain.StepType.NARROW;
import static java.time.ZoneOffset.UTC;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.UUID.fromString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class ExportModelFixtures {

  static final UUID MODEL_ID = fromString("d6fb8ae1-ab37-4622-935a-706ea6c53800");
  static final String MODEL_RESOURCE_NAME = "solvingModels/" + MODEL_ID;
  static final UUID POLICY_ID = fromString("b4708d8c-4832-6fde-8dc0-d17b4708d8ca");
  static final String POLICY_NAME = "policy";
  static final String POLICY_DESCRIPTION = "This is policy";
  static final String POLICY_RESOURCE_NAME = "policies/" + POLICY_ID;
  static final OffsetDateTime CREATED_AT =
      OffsetDateTime.of(2021, 3, 12, 11, 25, 10, 0, UTC);
  static final String MODEL_VERSION = DigitsOnlyDateFormatter.INSTANCE.format(CREATED_AT);
  static final OffsetDateTime APPROVED_AT =
      OffsetDateTime.of(2021, 5, 6, 8, 12, 59, 0, UTC);
  static final String APPROVED_BY = "jdoe";
  static final String CHECKSUM = "F5228B0D0F7F78D861609D5875DDE3FA";
  static final Instant POLICY_CREATED_AT = Instant.parse("2020-07-08T22:28:15Z");
  static final String POLICY_CREATED_BY = "asmith";
  static final Instant POLICY_UPDATED_AT = Instant.parse("2020-10-12T15:28:15Z");
  static final String POLICY_UPDATED_BY = "rpaul";

  private static final UUID FIRST_STEP_ID = fromString("ab378ae1-8ae1-4622-935a-706ea6c5706e");
  private static final String FIRST_STEP_NAME = "FIRST_STEP_NAME";
  private static final String FIRST_STEP_DESC = "FIRST_STEP_DESC";
  private static final StepType FIRST_STEP_TYPE = BUSINESS_LOGIC;

  private static final UUID SECOND_STEP_ID = fromString("46228ae1-ab37-8ae1-935a-706ea6c5935a");
  private static final String SECOND_STEP_NAME = "SECOND_STEP_NAME";
  private static final String SECOND_STEP_DESC = "SECOND_STEP_DESC";
  private static final StepType SECOND_STEP_TYPE = NARROW;

  static final ModelDto MODEL =
      ModelDto.builder()
          .name(MODEL_RESOURCE_NAME)
          .policy(POLICY_RESOURCE_NAME)
          .createdAt(CREATED_AT)
          .modelVersion(MODEL_VERSION)
          .build();

  static final ModelApprovalDto MODEL_APPROVAL =
      ModelApprovalDto.builder()
          .approvedAt(APPROVED_AT)
          .approvedBy(APPROVED_BY)
          .build();
  private static final String CONDITION_NAME = "CONDITION_NAME";
  private static final String CONDITION_VALUE = "CONDITION_VALUE";

  static TransferredModelRootDto transferredModelRoot() {
    TransferredModelRootDto root = new TransferredModelRootDto();
    root.setChecksum(CHECKSUM);
    root.setModel(transferredModel());
    return root;
  }

  static TransferredModelDto transferredModel() {
    TransferredModelDto model = new TransferredModelDto();
    model.setMetadata(transferredModelMetadata());
    model.setPolicy(transferredPolicyRoot());
    return model;
  }

  static TransferredModelMetadataDto transferredModelMetadata() {
    TransferredModelMetadataDto metadata = new TransferredModelMetadataDto();
    metadata.setModelId(MODEL_ID);
    metadata.setApprovedAt(APPROVED_AT.toInstant());
    metadata.setApprovedBy(APPROVED_BY);
    return metadata;
  }

  static TransferredPolicyRootDto transferredPolicyRoot() {
    TransferredPolicyRootDto root = new TransferredPolicyRootDto();
    root.setMetadata(transferredPolicyMetadata());
    root.setPolicy(transferredPolicy());
    return root;
  }

  private static TransferredPolicyMetadataDto transferredPolicyMetadata() {
    TransferredPolicyMetadataDto metadata = new TransferredPolicyMetadataDto();
    metadata.setCreatedAt(POLICY_CREATED_AT);
    metadata.setCreatedBy(POLICY_CREATED_BY);
    metadata.setUpdatedAt(POLICY_UPDATED_AT);
    metadata.setUpdatedBy(POLICY_UPDATED_BY);
    return metadata;
  }

  private static TransferredPolicyDto transferredPolicy() {
    TransferredPolicyDto policy = new TransferredPolicyDto();
    policy.setName(POLICY_NAME);
    policy.setPolicyId(POLICY_ID);
    policy.setDescription(POLICY_DESCRIPTION);
    policy.setSteps(transferredSteps());
    return policy;
  }

  private static List<TransferredStepDto> transferredSteps() {
    TransferredStepDto firstStep = getTransferredStepDto(
        FIRST_STEP_ID, FIRST_STEP_NAME, FIRST_STEP_DESC, FIRST_STEP_TYPE, emptyList());
    TransferredStepDto secondStep = getTransferredStepDto(
        SECOND_STEP_ID, SECOND_STEP_NAME, SECOND_STEP_DESC, SECOND_STEP_TYPE, emptyList());
    return asList(firstStep, secondStep);
  }

  @NotNull
  private static TransferredStepDto getTransferredStepDto(
      UUID id, String name, String desc, StepType type,
      List<TransferredFeatureLogicDto> featureLogics) {
    
    TransferredStepDto firstStep = new TransferredStepDto();
    firstStep.setStepId(id);
    firstStep.setName(name);
    firstStep.setDescription(desc);
    firstStep.setType(type);
    firstStep.setFeatureLogics(featureLogics);
    return firstStep;
  }

  private static List<TransferredFeatureLogicDto> getFeatureLogics() {
    return singletonList(getFeatureLogic());
  }

  private static TransferredFeatureLogicDto getFeatureLogic() {
    TransferredMatchConditionDto conditionDto = new TransferredMatchConditionDto(
        CONDITION_NAME,
        Condition.IS,
        singletonList(CONDITION_VALUE));
    return new TransferredFeatureLogicDto(1, singletonList(conditionDto));
  }
}
