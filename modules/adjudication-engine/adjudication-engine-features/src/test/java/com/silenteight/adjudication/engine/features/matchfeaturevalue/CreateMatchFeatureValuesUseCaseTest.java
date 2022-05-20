package com.silenteight.adjudication.engine.features.matchfeaturevalue;

import com.silenteight.adjudication.engine.features.matchfeaturevalue.dto.MatchFeatureValue;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class CreateMatchFeatureValuesUseCaseTest {

  private final InMemoryMatchFeatureValueDataAccess dataAccess =
      new InMemoryMatchFeatureValueDataAccess();
  private final CreateMatchFeatureValuesUseCase useCase =
      new CreateMatchFeatureValuesUseCase(dataAccess);

  @Test
  void whenEmptyInput_shouldNotSaveAnyValue() {
    useCase.createMatchFeatureValues(List.of());
    assertThat(dataAccess.isEmpty()).isTrue();
  }

  @Test
  void shouldConvertReasonToJson() {
    var reason = Struct
        .newBuilder()
        .putFields("reason", Value.newBuilder().setStringValue("why").build())
        .build();

    var valueDto = MatchFeatureValue
        .builder()
        .matchId(1)
        .agentConfigFeatureId(2)
        .value("MATCH")
        .reason(reason)
        .build();

    useCase.createMatchFeatureValues(List.of(valueDto));

    var stored = dataAccess.getById(1, 2);

    assertThat(stored.getReason().getFieldsCount()).isOne();
    assertThat(stored.getReason().getFieldsMap().get("reason").getStringValue()).isEqualTo("why");
  }
}
