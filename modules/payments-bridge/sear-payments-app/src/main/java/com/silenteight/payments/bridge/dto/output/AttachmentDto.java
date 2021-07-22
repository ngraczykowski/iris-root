package com.silenteight.payments.bridge.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentDto implements Serializable {

  private static final long serialVersionUID = -3226178721695595437L;
  @JsonProperty("Name")
  String name;
  @JsonProperty("Contents")
  String contents;
}
