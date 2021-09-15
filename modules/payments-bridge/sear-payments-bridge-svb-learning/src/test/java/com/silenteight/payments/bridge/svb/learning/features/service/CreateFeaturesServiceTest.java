package com.silenteight.payments.bridge.svb.learning.features.service;

import com.silenteight.payments.bridge.svb.learning.features.port.outgoing.CreateAgentInputsClient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.silenteight.payments.bridge.svb.learning.LearningAlertFixture.createLearningAlert;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateFeaturesServiceTest {

  private CreateFeaturesService createFeaturesService;
  @Mock
  private CreateAgentInputsClient createAgentInputsClient;

  @BeforeEach
  void setUp() {
    createFeaturesService = new CreateFeaturesService(
        createAgentInputsClient,
        List.of(new FreeTextExtractor(), new NameFeatureExtractor(), new GeoFeatureExtractor()));
  }

  @Test
  void shouldExtractAllFeatures() {
    var resp = createFeaturesService.createMatchFeatures(createLearningAlert());
    assertThat(resp.size()).isEqualTo(4);
    assertThat(resp.get(0).getFeatureInputsList().size()).isEqualTo(3);
  }

  @Test
  void shouldSendFeatures() {
    createFeaturesService.createMatchFeatures(createLearningAlert());
    verify(createAgentInputsClient).createAgentInputs(any());
  }
}
