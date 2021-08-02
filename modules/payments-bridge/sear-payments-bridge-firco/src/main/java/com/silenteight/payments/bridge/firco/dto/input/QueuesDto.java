package com.silenteight.payments.bridge.firco.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor
public class QueuesDto implements Serializable {

  private static final long serialVersionUID = -2348988436620390430L;

  @JsonProperty("Queues")
  private final Map<String, QueueDto> queues;

  @RequiredArgsConstructor
  @Getter
  public static class QueueDto implements Serializable {

    private static final long serialVersionUID = -4995971922261417901L;

    @JsonProperty("Size")
    private final int size;
  }
}
