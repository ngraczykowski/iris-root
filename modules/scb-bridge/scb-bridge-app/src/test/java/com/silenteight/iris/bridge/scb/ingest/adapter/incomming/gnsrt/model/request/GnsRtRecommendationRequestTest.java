/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.request;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.assertj.core.api.Assertions.*;

class GnsRtRecommendationRequestTest {

  private ObjectMapper objectMapper;
  private Validator validator;

  @BeforeEach
  void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();

    objectMapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.registerModule(new JavaTimeModule());
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "Request1.json",
      "Request4.json",
      "Request5.json",
      "Request7.json",
      "Request8.json",
      "Request9.json",
      "Request10.json",
      "Request11.json",
      "Request12.json",
      "Request13.json",
      "Request14.json",
      "Request16.json",
      "Request17.json",
      "Request18.json",
      "Request19.json",
      "Request20.json"
  })
  void shouldValidateRequest(String fileName) throws IOException {
    GnsRtRecommendationRequest request = createRequest(fileName);

    assertThat(validate(request))
        .extracting(ConstraintViolation::getMessage)
        .isEmpty();
  }

  private Set<ConstraintViolation<GnsRtRecommendationRequest>> validate(
      GnsRtRecommendationRequest request) {
    return validator.validate(request);
  }

  private GnsRtRecommendationRequest createRequest(String fileName) throws IOException {
    return objectMapper.readValue(
        Path.of("src/test/resources/gnsRtRequests/", fileName).toFile(),
        GnsRtRecommendationRequest.class);
  }
}
