package com.silenteight.serp.governance.vector.domain.details;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.serp.governance.vector.domain.dto.FeatureVectorWithUsageDto;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.APPROVER;
import static com.silenteight.sens.governance.common.testing.rest.TestRoles.AUDITOR;
import static com.silenteight.sens.governance.common.testing.rest.TestRoles.MODEL_TUNER;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.OK;

@Import({
    DetailsVectorsRestController.class,
    GenericExceptionControllerAdvice.class
})
class DetailsVectorsRestControllerTest extends BaseRestControllerTest {

  private static final String DETAILS_URL = "/v1/vectors/details";

  private final Fixtures fixtures = new Fixtures();

  @MockBean
  private VectorDetailQuery vectorDetailQuery;

  @TestWithRole(roles = { APPROVER, MODEL_TUNER, AUDITOR })
  void its200_whenFeatureVectorWithUsageFound() {
    given(vectorDetailQuery.findByFvSignature(fixtures.fvSignature))
        .willReturn(fixtures.featureVectorWithUsage);

    get(detailsUrl(fixtures.fvSignature))
        .statusCode(OK.value())
        .body("signature", is(fixtures.fvSignature))
        .body("usageCount", is((int) fixtures.usageCount))
        .body("names", is(fixtures.names))
        .body("values", is(fixtures.values));
  }

  private static String detailsUrl(String signature) {
    return DETAILS_URL + "?fvSignature=" + signature;
  }

  private class Fixtures {

    String fvSignature = "qC4MMVPvDOpB/vA+hn8tM8mUgt4=";
    long usageCount = 5L;
    List<String> names = List.of("name-agent", "date-agent");
    List<String> values = List.of("POTENTIAL_TRUE_POSITIVE", "FALSE_POSITIVE");

    FeatureVectorWithUsageDto featureVectorWithUsage =
        FeatureVectorWithUsageDto.builder()
            .signature(fvSignature)
            .usageCount(usageCount)
            .names(names)
            .values(values)
            .build();
  }
}
