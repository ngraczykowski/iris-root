package com.silenteight.searpayments.bridge.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestHitDto implements Serializable {

  private static final long serialVersionUID = 8461482337684197589L;
  @JsonProperty("Hit")
  HitDto hit;
}
