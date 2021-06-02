package com.silenteight.serp.governance.model.transfer.importing;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;
import com.silenteight.serp.governance.model.common.exception.InvalidChecksumException;
import com.silenteight.serp.governance.model.domain.ModelService;
import com.silenteight.serp.governance.model.transfer.dto.TransferredModelDto;
import com.silenteight.serp.governance.model.transfer.dto.TransferredModelRootDto;
import com.silenteight.serp.governance.policy.transfer.dto.TransferredPolicyRootDto;
import com.silenteight.serp.governance.policy.transfer.importing.ImportPolicyCommand;
import com.silenteight.serp.governance.policy.transfer.importing.ImportPolicyUseCase;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

import static com.silenteight.serp.governance.model.common.ChecksumCalculator.calculate;
import static com.silenteight.serp.governance.policy.common.PolicyResource.toResourceName;
import static java.nio.charset.Charset.forName;

@RequiredArgsConstructor
public class ImportModelUseCase {

  private static final JsonConversionHelper JSON_CONVERTER = JsonConversionHelper.INSTANCE;

  @NonNull
  private final ImportPolicyUseCase importPolicyUseCase;
  @NonNull
  private final ModelService modelService;

  public UUID apply(@NonNull String modelJson) {
    TransferredModelRootDto root = parse(modelJson);
    TransferredModelDto transferredModel = root.getModel();
    validateChecksum(root.getChecksum(), transferredModel);
    UUID policyId = importPolicyUseCase.apply(toImportPolicyCommand(transferredModel));

    return modelService.createModel(
        transferredModel.getModelId(),
        toResourceName(policyId),
        transferredModel.getApprovedBy());
  }

  private static TransferredModelRootDto parse(String modelJson) {
    return JSON_CONVERTER.deserializeObject(
        JSON_CONVERTER.deserializeFromString(modelJson),
        TransferredModelRootDto.class);
  }

  private static void validateChecksum(
      String expectedChecksum, TransferredModelDto transferredModel) {

    String checksum = calculate(transferredModel);

    if (!expectedChecksum.equals(checksum))
      throw new InvalidChecksumException("Checksum is different from expected.");
  }

  private static ImportPolicyCommand toImportPolicyCommand(
      TransferredModelDto transferredModel) {

    return ImportPolicyCommand.builder()
        .inputStream(getPolicyAsStream(transferredModel.getPolicy()))
        .importedBy(transferredModel.getApprovedBy())
        .build();
  }

  private static InputStream getPolicyAsStream(TransferredPolicyRootDto policy) {
    String policyJson = JSON_CONVERTER.serializeToString(policy);
    return new ByteArrayInputStream(policyJson.getBytes(forName("UTF-8")));
  }
}
