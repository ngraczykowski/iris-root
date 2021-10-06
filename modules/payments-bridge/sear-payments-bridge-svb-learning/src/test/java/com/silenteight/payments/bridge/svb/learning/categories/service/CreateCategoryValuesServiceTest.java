package com.silenteight.payments.bridge.svb.learning.categories.service;

import com.silenteight.payments.bridge.agents.port.OneLinerUseCase;
import com.silenteight.payments.bridge.categories.port.outgoing.CreateCategoryValuesClient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.silenteight.payments.bridge.agents.model.OneLinerAgentResponse.YES;
import static com.silenteight.payments.bridge.svb.learning.LearningAlertFixture.createLearningAlert;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCategoryValuesServiceTest {

  private CreateCategoryValuesService createCategoryValuesService;

  @Mock
  private CreateCategoryValuesClient createCategoryValuesClient;

  @Mock
  private OneLinerUseCase oneLinerUseCase;

  @BeforeEach
  void setUp() {
    when(oneLinerUseCase.invoke(any())).thenReturn(YES);

    createCategoryValuesService = new CreateCategoryValuesService(
        List.of(
            new OneLinerExtractor(oneLinerUseCase)),
        createCategoryValuesClient);
  }

  @Test
  void shouldExtractAllFeatures() {
    var resp = createCategoryValuesService.createCategoryValues(createLearningAlert());
    assertThat(resp.size()).isEqualTo(4);
  }

  @Test
  void shouldSendFeatures() {
    createCategoryValuesService.createCategoryValues(createLearningAlert());
    verify(createCategoryValuesClient).createCategoriesValues(any());
  }
}
