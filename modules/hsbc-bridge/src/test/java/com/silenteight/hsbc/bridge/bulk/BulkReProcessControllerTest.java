package com.silenteight.hsbc.bridge.bulk;

import com.silenteight.hsbc.bridge.bulk.rest.AlertIdWrapper;
import com.silenteight.hsbc.bridge.bulk.rest.AlertReRecommend;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class BulkReProcessControllerTest {

  @Mock
  private CreateSolvingBulkUseCase createSolvingBulkUseCase;
  private BulkReProcessController underTest;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    underTest = new BulkReProcessController(createSolvingBulkUseCase);
  }

  @Test
  public void postJson_shouldReturnStatus200() throws Exception {
    //when
    var bulkId = "re-process/some_uuid";

    //given
    given(createSolvingBulkUseCase.createBulkWithAlerts(any()))
        .willReturn(bulkId);
    var mockMvc = standaloneSetup(underTest).build();

    //then
    var mvcResult = mockMvc.perform(post("/async/batch/s8/reRecommend")
        .contentType(MediaType.APPLICATION_JSON).content(createRequestContent()))
        .andExpect(status().isOk())
        .andReturn();

    var response = mvcResult.getResponse().getContentAsString();
    assertThat(response).contains(bulkId);
  }

  private String createRequestContent(){
    var gson = new Gson();
    var alerts = new AlertReRecommend(List.of(
        new AlertIdWrapper("alert/1"),
        new AlertIdWrapper("alert/2"),
        new AlertIdWrapper("alert/3")
    ));
    return gson.toJson(alerts);
  }
}
