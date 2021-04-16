package com.silenteight.serp.governance.model.domain;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.serp.governance.model.domain.exception.ModelAlreadyExistsException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static com.silenteight.serp.governance.model.domain.dto.AddModelRequest.POST_AUDIT_TYPE;
import static com.silenteight.serp.governance.model.domain.dto.AddModelRequest.PRE_AUDIT_TYPE;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModelServiceTest {

  private static final UUID MODEL_ID = fromString("d6fb8ae1-ab37-4622-935a-706ea6c53800");
  private static final String POLICY_NAME = "policies/b4708d8c-4832-6fde-8dc0-d17b4708d8ca";
  private static final String USERNAME = "asmith";

  private final ModelRepository modelRepository = new InMemoryModelRepository();
  @Mock
  private AuditingLogger auditingLogger;

  private ModelService underTest;

  @BeforeEach
  void setUp() {
    underTest = new ModelDomainConfiguration()
        .modelService(modelRepository, auditingLogger);
  }

  @Test
  void createModel() {
    // when
    UUID modelId = underTest.createModel(MODEL_ID, POLICY_NAME, USERNAME);

    // then
    assertThat(modelId).isEqualTo(MODEL_ID);

    var logCaptor = ArgumentCaptor.forClass(AuditDataDto.class);

    verify(auditingLogger, times(2)).log(logCaptor.capture());
    AuditDataDto preAudit = getPreAudit(logCaptor);
    assertThat(preAudit.getType()).isEqualTo(PRE_AUDIT_TYPE);
    AuditDataDto postAudit = getPostAudit(logCaptor);
    assertThat(postAudit.getType()).isEqualTo(POST_AUDIT_TYPE);

    var modelOpt = modelRepository.findByPolicyName(POLICY_NAME);
    assertThat(modelOpt).isNotEmpty();

    var model = modelOpt.get();
    assertThat(model.getModelId()).isEqualTo(MODEL_ID);
    assertThat(model.getPolicyName()).isEqualTo(POLICY_NAME);
  }

  @Test
  void addModelThrowsExceptionWhenModelExists() {
    //given
    saveModel(MODEL_ID, POLICY_NAME);

    // when, then
    assertThatThrownBy(() -> underTest.createModel(MODEL_ID, POLICY_NAME, USERNAME))
        .isInstanceOf(ModelAlreadyExistsException.class);
  }

  private static AuditDataDto getPreAudit(ArgumentCaptor<AuditDataDto> logCaptor) {
    return getAudit(logCaptor, 0);
  }

  private static AuditDataDto getPostAudit(ArgumentCaptor<AuditDataDto> logCaptor) {
    return getAudit(logCaptor, 1);
  }

  private static AuditDataDto getAudit(ArgumentCaptor<AuditDataDto> logCaptor, int index) {
    List<AuditDataDto> logs = logCaptor.getAllValues();
    assertThat(logs).hasSizeGreaterThanOrEqualTo(index + 1);
    return logs.get(index);
  }

  private void saveModel(UUID modelId, String policyName) {
    modelRepository.save(new Model(modelId, policyName));
  }
}
