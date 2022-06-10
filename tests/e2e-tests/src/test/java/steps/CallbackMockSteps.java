/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package steps;

import lombok.extern.slf4j.Slf4j;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.path.json.JsonPath;
import org.awaitility.Awaitility;
import org.junit.Assert;
import org.springframework.lang.Nullable;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import utils.datageneration.callback.CallbackDto;

import java.lang.reflect.Type;
import java.time.Duration;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Pattern;

import static steps.Hooks.BASE_URL;
import static steps.Hooks.STOMP_SESSION;
import static steps.Hooks.scenarioContext;

@Slf4j
public class CallbackMockSteps implements En {

  private static final Pattern HTTP_SCHEME = Pattern.compile("^http");
  private static Queue<String> messages = new ConcurrentLinkedQueue<>();

  public CallbackMockSteps() {
    And(
        "Subscribed to callbacks at {string}",
        (String destination) -> {
          WebSocketClient client = new StandardWebSocketClient();
          WebSocketStompClient stompClient = new WebSocketStompClient(client);
          stompClient.setMessageConverter(new MappingJackson2MessageConverter());

          var url = HTTP_SCHEME.matcher(BASE_URL).replaceAll("ws") + "/rest/callback-mock/ws";

          StompSession stompSession =
              stompClient
                  .connect(
                      url,
                      new StompSessionHandlerAdapter() {
                        @Override
                        public void handleException(
                            StompSession session,
                            @Nullable StompCommand command,
                            StompHeaders headers,
                            byte[] payload,
                            Throwable exception) {
                          log.error("Unable to handle payload {}", new String(payload), exception);
                        }
                      })
                  .get();

          var sub =
              stompSession.subscribe(
                  destination,
                  new StompSessionHandlerAdapter() {

                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                      return CallbackDto.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                      var msg = (CallbackDto) payload;
                      log.info("Received : " + msg);
                      messages.add(msg.getBody());
                    }
                  });

          log.info("Subscribed: " + sub.getSubscriptionId() + ", " + sub.getSubscriptionHeaders());

          scenarioContext.set(STOMP_SESSION, stompSession);
        });

    And(
        "Wait for a message for max {int} seconds",
        (Integer waitTime) -> {
          Awaitility.await()
              .atMost(Duration.ofSeconds(waitTime))
              .until(() -> messages.peek() != null);
        });

    And(
        "The last message contains following fields",
        (DataTable dataTable) -> {
          assert messages.peek() != null;
          var json = JsonPath.from(messages.peek());
          dataTable.asMap().forEach((k, v) -> Assert.assertEquals("key " + k, v, json.get(k)));
        });

    After(
        () ->
            Optional.ofNullable(scenarioContext.get(STOMP_SESSION))
                .map(StompSession.class::cast)
                .ifPresent(StompSession::disconnect));
  }
}
