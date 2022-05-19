package com.silenteight.connector.ftcc.callback.response;

import com.silenteight.connector.ftcc.callback.exception.NonRecoverableCallbackException;
import com.silenteight.connector.ftcc.common.resource.MessageResource;
import com.silenteight.connector.ftcc.request.details.dto.MessageDetailsDto;
import com.silenteight.recommendation.api.library.v1.AlertOut;
import com.silenteight.recommendation.api.library.v1.RecommendationOut;
import com.silenteight.recommendation.api.library.v1.RecommendationsOut;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import static com.silenteight.connector.ftcc.callback.CallbackFixtures.createRecommendationsOut;
import static com.silenteight.connector.ftcc.callback.response.ClientRequestDtoBuilder.EMPTY_RECOMMENDATION;
import static com.silenteight.connector.ftcc.common.testing.CommonFixtures.ANALYSIS_NAME;
import static com.silenteight.connector.ftcc.common.testing.CommonFixtures.BATCH_NAME;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ClientRequestDtoBuilderTest {

  @Mock
  private ResponseCreator responseCreator;

  @Mock
  private MessageDetailsService messageDetailsService;

  @InjectMocks
  private ClientRequestDtoBuilder underTest;

  @DisplayName("When CallbackRequestDto can't be build then NonRecoverableException is thrown")
  @Test
  void whenCallbackRequestDtoCantBeBuildThenNonRecoverableExceptionIsThrown() {
    when(responseCreator.build(anyList())).thenThrow(RuntimeException.class);
    RecommendationsOut recommendationsOut = createRecommendationsOut(1);

    assertThrows(
        NonRecoverableCallbackException.class,
        () -> underTest.build(BATCH_NAME, ANALYSIS_NAME, recommendationsOut)).getMessage();
  }

  @DisplayName("Every Alert create ReceiveDecisionMessageDto")
  @Test
  void everyAlertCreateReceiveDecisionMessageDto() {
    var recommendations = createRecommendationsOut(2);
    var messagesIds = recommendations.getRecommendations().stream()
        .map(RecommendationOut::getAlert)
        .map(AlertOut::getId)
        .map(MessageResource::fromResourceName)
        .collect(toList());

    messagesIds.add(UUID.randomUUID());
    Map<UUID, MessageDetailsDto> messagesMap = messagesIds.stream()
        .map(id -> MessageDetailsDto.builder()
            .id(id)
            .build())
        .collect(toMap(MessageDetailsDto::getId, Function.identity()));

    when(messageDetailsService.messages(BATCH_NAME)).thenReturn(messagesMap);
    underTest.build(BATCH_NAME, ANALYSIS_NAME, recommendations);

    verify(responseCreator, times(1)).build(anyList());
    verify(responseCreator, times(1)).buildMessageDto(
        eq(messagesMap.get(messagesIds.get(0))), eq(recommendations.getRecommendations().get(0)));
    verify(responseCreator, times(1)).buildMessageDto(
        eq(messagesMap.get(messagesIds.get(1))), eq(recommendations.getRecommendations().get(1)));
    verify(responseCreator, times(1)).buildMessageDto(
        eq(messagesMap.get(messagesIds.get(2))), eq(EMPTY_RECOMMENDATION));
  }
}
