package com.silenteight.sens.webapp.backend.bulkchange;

import com.silenteight.sens.webapp.audit.api.trace.AuditTracer;
import com.silenteight.sens.webapp.backend.reasoningbranch.list.dto.ReasoningBranchIdDto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateBulkChangeUseCaseTest {

  @Mock
  private AuditTracer auditTracer;

  @Mock
  CreateBulkChangeMessageGateway gateway;

  @InjectMocks
  private CreateBulkChangeUseCase useCase;

  @Test
  void savesAuditLogEvent() {
    UUID bulkChangeId = UUID.randomUUID();
    CreateBulkChangeCommand command = createBulkChangeCommandWithId(bulkChangeId);

    useCase.apply(command);

    verify(auditTracer).save(
        argThat(e -> {
          assertThat(e.getType()).isEqualTo("BulkChangeCreationRequested");
          assertThat(e.getDetails()).isEqualTo(command);
          return true;
        })
    );
  }

  private CreateBulkChangeCommand createBulkChangeCommandWithId(UUID bulkChangeId) {
    return CreateBulkChangeCommand.builder()
        .bulkChangeId(bulkChangeId)
        .reasoningBranchIds(List.of(new ReasoningBranchIdDto(1L, 1L)))
        .build();
  }
}
