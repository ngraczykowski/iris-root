package com.silenteight.connector.ftcc.ingest.dto.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(UpperCamelCaseStrategy.class)
public class CustomerInfoDto implements Serializable {

  private static final long serialVersionUID = 3238287326136389287L;
  private String account;

  private String city;

  private String country;

  private String dateOfBirth;

  private String name;

  private String placeOfBirth;

  private String state;

  private String userData1;

  private String userData2;
}
