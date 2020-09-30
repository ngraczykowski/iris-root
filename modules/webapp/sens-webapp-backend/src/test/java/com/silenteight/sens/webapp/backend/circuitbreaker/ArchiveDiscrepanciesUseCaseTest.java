package com.silenteight.sens.webapp.backend.circuitbreaker;

import com.silenteight.sens.webapp.audit.api.trace.AuditTracer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArchiveDiscrepanciesUseCaseTest {

  @Mock
  private CircuitBreakerMessageGateway gateway;

  @Mock
  private AuditTracer auditTracer;

  @InjectMocks
  private ArchiveDiscrepanciesUseCase useCase;

  @Test
  void shouldPassTheCommandToGateway() {
    List<Long> discrepancyIds = List.of(2L, 3L, 5L);
    useCase.apply(new ArchiveCircuitBreakerDiscrepanciesCommand(discrepancyIds));

    verify(gateway).send(argThat(
        command -> command.getDiscrepancyIdsList().equals(discrepancyIds)));
  }

  @Test
  void savesAuditLogEvent() {
    ArchiveCircuitBreakerDiscrepanciesCommand
        command = new ArchiveCircuitBreakerDiscrepanciesCommand(List.of(1L));

    useCase.apply(command);

    verify(auditTracer).save(
        argThat(e -> {
          assertThat(e.getType()).isEqualTo("DiscrepancyArchivingRequested");
          assertThat(e.getDetails()).isEqualTo(command);
          return true;
        })
    );
  }
}
