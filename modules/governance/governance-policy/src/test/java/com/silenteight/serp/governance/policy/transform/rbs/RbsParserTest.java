package com.silenteight.serp.governance.policy.transform.rbs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.io.IOException;
import java.io.InputStream;

import static com.silenteight.serp.governance.policy.transform.rbs.RbsImportFixture.IMPORT_FILE_NAME;
import static com.silenteight.serp.governance.policy.transform.rbs.RbsImportFixture.STEPS_DATA;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitConfig
@ContextConfiguration(classes = { RbsImportConfiguration.class })
@TestPropertySource("classpath:data-test.properties")
class RbsParserTest {

  @Autowired
  private RbsParser parser;

  @MockBean
  private RbsToPolicyTransformationService rbsToPolicyTransformationService;

  @Test
  void stepsDataShouldBeCreated() throws IOException {
    //given
    InputStream inputStream = RbsImportFixture.IMPORT_FILE_URL.openStream();

    //when
    StepsData stepsData = parser.parse(inputStream, IMPORT_FILE_NAME);

    //then
    assertThat(stepsData).isEqualTo(STEPS_DATA);
  }

  @Test
  void throwExceptionIfFeatureHeadersAreMissing() throws IOException {
    InputStream inputStream = RbsImportFixture.INVALID_IMPORT_FILE_URL.openStream();

    assertThrows(RbsParserException.class, () -> parser.parse(inputStream, IMPORT_FILE_NAME));
  }
}
