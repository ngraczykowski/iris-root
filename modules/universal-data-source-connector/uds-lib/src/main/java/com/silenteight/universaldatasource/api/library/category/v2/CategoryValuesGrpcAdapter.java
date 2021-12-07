package com.silenteight.universaldatasource.api.library.category.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.categories.api.v2.CategoryValueServiceGrpc.CategoryValueServiceBlockingStub;
import com.silenteight.universaldatasource.api.library.UniversalDataSourceLibraryRuntimeException;

import io.vavr.control.Try;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class CategoryValuesGrpcAdapter implements CategoryValuesServiceClient {

  public static final String COULD_NOT_FETCH_CATEGORY_VALUES_ERR_MSG =
      "Couldn't fetch category values";
  public static final String FETCHED_CATEGORY_VALUES = "Fetched {} category values";

  private final CategoryValueServiceBlockingStub blockingStub;
  private final long deadlineInSeconds;

  @Override
  public BatchCreateCategoryValuesOut createCategoriesValues(BatchCreateCategoryValuesIn request) {
    return Try
        .of(() -> getStub().batchCreateCategoryValues(request.toBatchCreateCategoryValuesRequest()))
        .map(BatchCreateCategoryValuesOut::createFrom)
        .onSuccess(
            response -> log.info(
                FETCHED_CATEGORY_VALUES, response.getCreatedCategoryValues().size()))
        .onFailure(e -> log.error(COULD_NOT_FETCH_CATEGORY_VALUES_ERR_MSG, e))
        .getOrElseThrow(
            e -> new UniversalDataSourceLibraryRuntimeException(
                COULD_NOT_FETCH_CATEGORY_VALUES_ERR_MSG, e));
  }

  private CategoryValueServiceBlockingStub getStub() {
    return blockingStub.withDeadlineAfter(deadlineInSeconds, TimeUnit.SECONDS);
  }
}
