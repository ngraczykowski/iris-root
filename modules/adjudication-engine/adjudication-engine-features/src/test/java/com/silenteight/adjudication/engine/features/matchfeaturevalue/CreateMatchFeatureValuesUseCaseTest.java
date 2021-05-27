package com.silenteight.adjudication.engine.features.matchfeaturevalue;

import com.silenteight.adjudication.engine.common.protobuf.ProtoMessageToObjectNodeConverter;
import com.silenteight.adjudication.engine.features.matchfeaturevalue.dto.MatchFeatureValueDto;
import com.silenteight.sep.base.common.protocol.MessageRegistryFactory;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class CreateMatchFeatureValuesUseCaseTest {

  private static final ProtoMessageToObjectNodeConverter CONVERTER =
      new ProtoMessageToObjectNodeConverter(
          new MessageRegistryFactory("com.google.protobuf").create());

  private final InMemoryMatchFeatureValueRepository repository =
      new InMemoryMatchFeatureValueRepository();
  private final CreateMatchFeatureValuesUseCase useCase =
      new CreateMatchFeatureValuesUseCase(repository, CONVERTER);

  @Test
  void whenEmptyInput_shouldNotSaveAnyValue() {
    useCase.createMatchFeatureValues(List.of());
    assertThat(repository.isEmpty()).isTrue();
  }

  @Test
  void shouldConvertReasonToJson() {
    var reason = Struct
        .newBuilder()
        .putFields("reason", Value.newBuilder().setStringValue("why").build())
        .build();

    var valueDto = MatchFeatureValueDto
        .builder()
        .matchId(1)
        .agentConfigFeatureId(2)
        .value("MATCH")
        .reason(reason)
        .build();

    useCase.createMatchFeatureValues(List.of(valueDto));

    var stored = repository.getById(1, 2);

    assertThat(stored.getReason()).isNotEmpty();
    assertThat(stored.getReason().get("reason").asText()).isEqualTo("why");
  }
}
