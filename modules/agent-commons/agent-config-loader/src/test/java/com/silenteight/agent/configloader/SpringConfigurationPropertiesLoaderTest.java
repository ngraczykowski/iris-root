package com.silenteight.agent.configloader;

import lombok.Data;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.boot.context.properties.bind.validation.BindValidationException;

import java.time.Duration;
import javax.validation.constraints.NotBlank;

import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.*;

class SpringConfigurationPropertiesLoaderTest {

  private static final String EXPECTED_STRING = "A String";
  private static final long EXPECTED_NUMBER = 12345L;
  private static final boolean EXPECTED_BOOL = false;
  private static final Duration EXPECTED_DURATION = ofSeconds(10);

  private static final String PREFIX = "prefix";
  private static final String EXPECTED_PREFIX_STRING = "A Prefixed String";
  private static final long EXPECTED_PREFIX_NUMBER = 123456L;
  private static final boolean EXPECTED_PREFIX_BOOL = true;
  private static final Duration EXPECTED_PREFIX_DURATION = ofSeconds(10);

  @Test
  void givenPropertiesFile_loadsExpectedProperties() {
    SimpleProperties simpleProperties = createLoader("simple.properties")
        .load("", SimpleProperties.class);

    assertThat(simpleProperties.getString()).isEqualTo(EXPECTED_STRING);
    assertThat(simpleProperties.getNumber()).isEqualTo(EXPECTED_NUMBER);
    assertThat(simpleProperties.getBool()).isEqualTo(EXPECTED_BOOL);
    assertThat(simpleProperties.getDuration()).isEqualTo(EXPECTED_DURATION);
  }

  private static SpringConfigurationPropertiesLoader createLoader(String testFileName) {
    return new SpringConfigurationPropertiesLoader(
        "classpath:com/silenteight/agent/configloader/" + testFileName);
  }

  @Test
  void givenPropertiesFile_loadsPrefixedProperties() {
    SimpleProperties simpleProperties = createLoader("prefixed.properties")
        .load(PREFIX, SimpleProperties.class);

    assertThat(simpleProperties.getString()).isEqualTo(EXPECTED_PREFIX_STRING);
    assertThat(simpleProperties.getNumber()).isEqualTo(EXPECTED_PREFIX_NUMBER);
    assertThat(simpleProperties.getBool()).isEqualTo(EXPECTED_PREFIX_BOOL);
    assertThat(simpleProperties.getDuration()).isEqualTo(EXPECTED_PREFIX_DURATION);
  }

  @Test
  void givenPropertiesFileWithInvalidProperties_throwsWhenLoading() {
    assertThatThrownBy(() -> createLoader("invalid.properties").load("", ValidProperties.class))
        .isInstanceOf(BindException.class);
  }

  @Test
  void givenYamlFileWithNestedProperties_loadsThem() {
    NestedProperties properties = createLoader("nested.yml").load(PREFIX, NestedProperties.class);
    SimpleProperties nested = properties.getNested();

    assertThat(nested).isNotNull();
    assertThat(nested.getString()).isEqualTo(EXPECTED_PREFIX_STRING);
    assertThat(nested.getNumber()).isEqualTo(EXPECTED_PREFIX_NUMBER);
    assertThat(nested.getBool()).isEqualTo(EXPECTED_PREFIX_BOOL);
  }

  @Test
  void givenValidation_failsOnConstraint() {
    assertThatThrownBy(
        () -> createLoader("validating.properties").load("", ValidatedProperties.class))
        .hasCauseInstanceOf(BindValidationException.class);
  }

  @Data
  static class SimpleProperties {

    private String string;
    private Long number;
    private Boolean bool;
    private Duration duration;
  }

  @Data
  static class ValidProperties {

    private Long number;
  }

  @Data
  static class NestedProperties {

    private SimpleProperties nested = new SimpleProperties();
  }

  @Data
  static class ValidatedProperties {

    @NotBlank
    private String notBlankString;
  }
}
