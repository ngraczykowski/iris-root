package com.silenteight.serp.governance.app.grpc;

import com.silenteight.proto.serp.v1.api.ListDecisionTreesResponse;
import com.silenteight.proto.serp.v1.governance.DecisionTreeSummary;
import com.silenteight.serp.governance.decisiontreesummaryquery.DecisionTreeSummaryFinder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListDecisionTreesUseCaseTest {

  private final Fixtures fixtures = new Fixtures();

  @Mock
  private DecisionTreeSummaryFinder finder;

  private ListDecisionTreesUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase = new ListDecisionTreesUseCase(finder);
  }

  @Test
  void getAllTest() {
    when(finder.getAll()).thenReturn(fixtures.summaries);

    ListDecisionTreesResponse response = useCase.activate();

    assertThat(response.getDecisionTreeList()).isEqualTo(fixtures.summaries);
  }

  private static class Fixtures {

    DecisionTreeSummary summary1 = DecisionTreeSummary.newBuilder()
        .setName("name1")
        .build();

    DecisionTreeSummary summary2 = DecisionTreeSummary.newBuilder()
        .setName("name2")
        .build();

    List<DecisionTreeSummary> summaries = asList(summary1, summary2);
  }
}
