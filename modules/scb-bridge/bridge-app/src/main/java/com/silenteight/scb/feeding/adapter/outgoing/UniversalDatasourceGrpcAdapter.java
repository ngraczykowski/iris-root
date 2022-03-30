package com.silenteight.scb.feeding.adapter.outgoing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.feeding.domain.port.outgoing.UniversalDatasourceApiClient;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.UniversalDataSourceLibraryRuntimeException;
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputServiceClient;
import com.silenteight.universaldatasource.api.library.agentinput.v1.BatchCreateAgentInputsIn;
import com.silenteight.universaldatasource.api.library.category.v2.BatchCreateCategoriesIn;
import com.silenteight.universaldatasource.api.library.category.v2.BatchCreateCategoryValuesIn;
import com.silenteight.universaldatasource.api.library.category.v2.CategoryServiceClient;
import com.silenteight.universaldatasource.api.library.category.v2.CategoryValuesServiceClient;

import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class UniversalDatasourceGrpcAdapter implements UniversalDatasourceApiClient {

  private final CategoryServiceClient categoryServiceClient;
  private final CategoryValuesServiceClient categoryValuesServiceClient;
  private final AgentInputServiceClient agentInputServiceClient;

  @Override
  @Retryable(value = UniversalDataSourceLibraryRuntimeException.class)
  public void registerCategories(BatchCreateCategoriesIn categories) {
    categoryServiceClient.createCategories(categories);
    log.info("Categories has been registered in Universal Datasource.");
  }

  @Override
  @Retryable(value = UniversalDataSourceLibraryRuntimeException.class)
  public void registerCategoryValues(BatchCreateCategoryValuesIn categoryValuesIn) {
    categoryValuesServiceClient.createCategoriesValues(categoryValuesIn);
    log.info("Categories Values have been registered in Universal Datasource.");
  }

  @Override
  @Retryable(value = UniversalDataSourceLibraryRuntimeException.class)
  public <T extends Feature> void registerAgentInputs(BatchCreateAgentInputsIn<T> agentInputs) {
    agentInputServiceClient.createBatchCreateAgentInputs(agentInputs);
    log.info("Agent inputs has been registered in Universal Datasource.");
  }
}