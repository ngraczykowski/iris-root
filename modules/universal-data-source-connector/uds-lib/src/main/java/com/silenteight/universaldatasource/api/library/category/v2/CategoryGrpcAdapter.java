package com.silenteight.universaldatasource.api.library.category.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.categories.api.v2.CategoryServiceGrpc.CategoryServiceBlockingStub;
import com.silenteight.universaldatasource.api.library.UniversalDataSourceLibraryRuntimeException;

import io.vavr.control.Try;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class CategoryGrpcAdapter implements CategoryServiceClient {

  public static final String COULD_NOT_CREATE_CATEGORIES_ERROR_MSG = "Couldn't create categories";

  private final CategoryServiceBlockingStub blockingStub;
  private final long deadlineInSeconds;

  @Override
  public BatchCreateCategoriesOut createCategories(BatchCreateCategoriesIn request) {
    return Try
        .of(() -> getStub().batchCreateCategories(request.toBatchCreateCategoriesRequest()))
        .map(BatchCreateCategoriesOut::createFrom)
        .onSuccess(response -> log.info("Created categories with result = {}", response))
        .onFailure(e -> log.error(COULD_NOT_CREATE_CATEGORIES_ERROR_MSG, e))
        .getOrElseThrow(e -> new UniversalDataSourceLibraryRuntimeException(
            COULD_NOT_CREATE_CATEGORIES_ERROR_MSG, e));
  }

  private CategoryServiceBlockingStub getStub() {
    return blockingStub.withDeadlineAfter(deadlineInSeconds, TimeUnit.SECONDS);
  }
}
