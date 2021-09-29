package com.silenteight.payments.bridge.firco.adapter.incoming.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(UpperCamelCaseStrategy.class)
public class AttachmentDto implements Serializable {

  private static final long serialVersionUID = -3226178721695595437L;

  private String name;

  private String contents;
}
