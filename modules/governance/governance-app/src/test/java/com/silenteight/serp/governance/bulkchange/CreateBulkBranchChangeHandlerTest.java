package com.silenteight.serp.governance.bulkchange;

import com.silenteight.proto.serp.v1.governance.CreateBulkBranchChangeCommand;
import com.silenteight.protocol.utils.Uuids;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateBulkBranchChangeHandlerTest {

  private final Fixtures fixtures = new Fixtures();

  @Mock
  private BulkBranchChangeRepository repository;
  @Mock
  private BulkBranchChangeMapper mapper;

  private CreateBulkBranchChangeHandler handler;

  @BeforeEach
  void setUp() {
    handler = new CreateBulkBranchChangeHandler(mapper, repository);
  }

  @Test
  void shouldStoreRequestAndReturnEvent() {
    doReturn(fixtures.expectedChange).when(mapper).map(fixtures.command);

    var event = handler.create(fixtures.command);

    verify(repository).save(fixtures.expectedChange);
    assertThat(event.getId()).isEqualTo(fixtures.command.getId());
    assertThat(event.getCorrelationId()).isEqualTo(fixtures.command.getCorrelationId());
  }

  private static class Fixtures {

    UUID id = UUID.randomUUID();
    UUID correlationId = UUID.randomUUID();

    CreateBulkBranchChangeCommand command = CreateBulkBranchChangeCommand.newBuilder()
        .setId(Uuids.fromJavaUuid(id))
        .setCorrelationId(Uuids.fromJavaUuid(correlationId))
        .build();

    BulkBranchChange expectedChange = new BulkBranchChange(id, Set.of());
  }
}
