package com.silenteight.sens.webapp.backend.decisiontree.rest;

import com.silenteight.sens.webapp.backend.decisiontree.DecisionTreeFacade;
import com.silenteight.sens.webapp.backend.decisiontree.dto.DecisionTreesDto;
import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;

import static com.silenteight.sens.webapp.backend.decisiontree.DecisionTreeDtoFixtures.ACTIVE;
import static com.silenteight.sens.webapp.backend.decisiontree.DecisionTreeDtoFixtures.INACTIVE;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

@Import({DecisionTreeRestController.class})
class DecisionTreeRestControllerTest extends BaseRestControllerTest {

  @MockBean
  private DecisionTreeFacade facade;

  @Test
  void shouldProduceSuccessOutput_whenServiceReturnsEmptyList() {
    when(facade.list()).thenReturn(new DecisionTreesDto(emptyList()));

    get("/decision-trees")
        .statusCode(HttpStatus.OK.value())
        .body("total", is(0))
        .body("results.size()", is(0));
  }

  @Test
  void shouldProduceSuccessOutput_whenServiceReturnsDecisionTreeList() {
    when(facade.list()).thenReturn(new DecisionTreesDto(asList(INACTIVE, ACTIVE)));

    get("/decision-trees")
        .statusCode(HttpStatus.OK.value())
        .body("total", is(2))
        .body("results.size()", is(2))
        .body("results[0].id", equalTo((int) INACTIVE.getId()))
        .body("results[0].name", equalTo(INACTIVE.getName()))
        .body("results[0].status.name", equalTo(INACTIVE.getStatus().getName()))
        .body("results[0].activations", equalTo(INACTIVE.getActivations()))
        .body("results[1].id", equalTo((int) ACTIVE.getId()))
        .body("results[1].name", equalTo(ACTIVE.getName()))
        .body("results[1].status.name", equalTo(ACTIVE.getStatus().getName()))
        .body("results[1].activations", equalTo(ACTIVE.getActivations()));
  }
}
