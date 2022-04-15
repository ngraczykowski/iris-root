package com.silenteight.serp.governance.model.transfer.importing;

import com.silenteight.serp.governance.model.common.exception.InvalidChecksumException;
import com.silenteight.serp.governance.model.domain.ModelService;
import com.silenteight.serp.governance.policy.transfer.importing.ImportPolicyCommand;
import com.silenteight.serp.governance.policy.transfer.importing.ImportPolicyUseCase;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.UUID;

import static java.nio.charset.Charset.forName;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImportModelUseCaseTest {

  @Mock
  private ImportPolicyUseCase importPolicyUseCase;

  @Mock
  private ModelService modelService;

  @InjectMocks
  private ImportModelUseCase underTest;

  @Test
  void createModel() throws IOException {
    // given
    UUID policyId = fromString("01256804-1ce1-4d52-94d4-d1876910f272");
    UUID modelId = fromString("de1afe98-0b58-4941-9791-4e081f9b8139");
    String modelJson = loadResourceAsString("correctModel.json");
    when(importPolicyUseCase.apply(any(ImportPolicyCommand.class))).thenReturn(policyId);
    when(modelService.createModelWithModelVersion(
        modelId, "policies/" + policyId, "asmith", "20200601112534")).thenReturn(modelId);

    // when
    UUID result = underTest.apply(modelJson);

    // then
    assertThat(result).isEqualTo(modelId);
  }

  @Test
  void throwsExceptionWhenChecksumIsInvalid() throws IOException {
    // given
    String modelJson = loadResourceAsString("invalidChecksumModel.json");

    // when
    Exception exception = assertThrows(
        InvalidChecksumException.class, () -> underTest.apply(modelJson));

    // then
    assertThat(exception.getMessage()).isEqualTo("Checksum is different from expected.");
  }

  private static String loadResourceAsString(String resource) throws IOException {
    return IOUtils.toString(
        ImportModelUseCase.class.getResourceAsStream(resource),
        forName("UTF-8"));
  }
}
