package com.silenteight.serp.governance.policy.domain;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.serp.governance.policy.domain.events.NewPolicyInUseEvent;
import com.silenteight.serp.governance.policy.domain.events.PolicyImportedEvent;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PolicyCreatedEventHandlerTest {

  @Mock
  private AuditingLogger auditingLogger;

  @Mock
  private ApplicationEventPublisher eventPublisher;

  @InjectMocks
  private PolicyImportedEventHandler underTest;

  @Test
  void handlePolicyCreatedEvent() {
    // given
    var policyId = fromString("01256804-1ce1-4d52-94d4-d1876910f272");
    var correlationId = fromString("de1afe98-0b58-4941-9791-4e081f9b8139");

    // when
    PolicyImportedEvent importedEvent = PolicyImportedEvent.builder()
        .policyId(policyId)
        .correlationId(correlationId)
        .build();
    underTest.handle(importedEvent);

    // then
    var logCaptor = ArgumentCaptor.forClass(AuditDataDto.class);

    verify(auditingLogger).log(logCaptor.capture());

    var log = logCaptor.getValue();
    assertThat(log.getCorrelationId()).isEqualTo(correlationId);
    assertThat(log.getType()).isEqualTo("PolicyCreated");
    assertThat(log.getEntityId()).isEqualTo(policyId.toString());
    assertThat(log.getEntityClass()).isEqualTo("Policy");
    assertThat(log.getEntityAction()).isEqualTo("CREATE");

    var policyPromotedEventCaptor = ArgumentCaptor.forClass(NewPolicyInUseEvent.class);

    verify(eventPublisher).publishEvent(policyPromotedEventCaptor.capture());

    var policyPromotedEvent = policyPromotedEventCaptor.getValue();
    assertThat(policyPromotedEvent.getPolicyId()).isEqualTo(policyId);
  }
}
