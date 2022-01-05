package com.silenteight.simulator.model.archive;

import com.silenteight.simulator.management.archive.ArchiveSimulationRequest;
import com.silenteight.simulator.management.archive.ArchiveSimulationUseCase;
import com.silenteight.simulator.management.list.ListSimulationsQuery;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.silenteight.simulator.model.archive.ModelsArchivedFixtures.MODELS_ARCHIVED_MESSAGE;
import static com.silenteight.simulator.model.archive.ModelsArchivedFixtures.MODEL_NAMES;
import static com.silenteight.simulator.model.archive.ModelsArchivedFixtures.SIMULATION_DTO;
import static com.silenteight.simulator.model.archive.ModelsArchivedUseCase.ARCHIVED_BY;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModelsArchivedUseCaseTest {

  @InjectMocks
  private ModelsArchivedUseCase underTest;

  @Mock
  private ListSimulationsQuery listSimulationsQuery;
  @Mock
  private ArchiveSimulationUseCase archiveSimulationUseCase;

  @Test
  void shouldArchiveSimulations() {
    // given
    when(listSimulationsQuery.findByModels(MODEL_NAMES)).thenReturn(of(SIMULATION_DTO));

    // when
    underTest.handle(MODELS_ARCHIVED_MESSAGE);

    // then
    ArgumentCaptor<ArchiveSimulationRequest> argumentCaptor = ArgumentCaptor
        .forClass(ArchiveSimulationRequest.class);

    verify(archiveSimulationUseCase).activate(argumentCaptor.capture());

    List<ArchiveSimulationRequest> requests = argumentCaptor.getAllValues();
    assertThat(requests.size()).isEqualTo(1);
    ArchiveSimulationRequest request = requests.get(0);
    assertThat(request.getId()).isEqualTo(SIMULATION_DTO.getId());
    assertThat(request.getArchivedBy()).isEqualTo(ARCHIVED_BY);
  }
}
