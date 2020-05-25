package com.silenteight.serp.aspects.validation.business.model;

import lombok.AllArgsConstructor;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
@AllArgsConstructor  // NOTE(mjasinski): Not validated constructor.
public class BusinessObject {

  @NotBlank
  @Size(max = 50)
  String resultText;

  // NOTE(mjasinski): Validated constructor.
  @Valid
  public BusinessObject(String suffix, String text) {
    this.resultText = suffix + text;
  }

  // NOTE(mjasinski): Validated constructor with parameters validation.
  @Valid
  public BusinessObject(@Size(max = 6) String text, @Max(10) int repeat) {
    this.resultText = text.repeat(repeat);
  }
}
