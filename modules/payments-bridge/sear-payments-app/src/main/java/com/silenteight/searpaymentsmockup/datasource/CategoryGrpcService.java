package com.silenteight.searpaymentsmockup.datasource;

import com.silenteight.datasource.categories.api.v1.*;
import com.silenteight.datasource.categories.api.v1.CategoryServiceGrpc.CategoryServiceImplBase;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
class CategoryGrpcService extends CategoryServiceImplBase {

  @Override
  public void listCategories(
      ListCategoriesRequest request,
      StreamObserver<ListCategoriesResponse> responseObserver) {
    var categories = findAllCategories();
    responseObserver.onNext(
        ListCategoriesResponse.newBuilder().addAllCategories(categories).build());
    responseObserver.onCompleted();
  }

  @Override
  public void batchGetMatchCategoryValues(
      BatchGetMatchCategoryValuesRequest request,
      StreamObserver<BatchGetMatchCategoryValuesResponse> responseObserver) {
    var categoryValues = getMatchCategoryValues(
        request.getMatchValuesList());
    responseObserver.onNext(
        BatchGetMatchCategoryValuesResponse
            .newBuilder()
            .addAllCategoryValues(categoryValues)
            .build());
    responseObserver.onCompleted();
  }

  private List<Category> findAllCategories() {
    return List.of(
        Category.newBuilder().setName("country")
            .setType(CategoryType.ANY_STRING)
            .setDisplayName("Country")
            .setMultiValue(true)
            .build(),
        Category.newBuilder().setName("customerType")
            .setDisplayName("Customer Type (Individual / Company)")
            .setType(CategoryType.ENUMERATED)
            .setMultiValue(false)
            .addAllAllowedValues(List.of("I", "C"))
            .build(),
        Category.newBuilder().setName("hitType")
            .setDisplayName("Hit Type (PEP / AM / SAN)")
            .setType(CategoryType.ENUMERATED)
            .setMultiValue(false)
            .addAllAllowedValues(List.of("SAN", "PEP", "AM"))
            .build());
  }

  private List<CategoryValue> getMatchCategoryValues(List<String> matchValues) {
    return matchValues.stream()
        .map(this::getCategoryValue)
        .collect(Collectors.toList());
  }

  private CategoryValue getCategoryValue(String matchValue) {
    var builder = CategoryValue.newBuilder()
        .setName(matchValue);

    if (matchValue.contains("categories/source_system")) {
      builder.setSingleValue("ECDD");
    } else if (matchValue.contains("categories/country")) {
      builder.setSingleValue("PL");
    } else if (matchValue.contains("categories/customer_type")) {
      builder.setSingleValue("I");
    } else if (matchValue.contains("categories/hit_type")) {
      builder.setSingleValue("DENY");
    } else if (matchValue.contains("categories/segment")) {
      builder.setSingleValue("CONSUMER");
    } else if (matchValue.contains("categories/hit_category")) {
      builder.setSingleValue("DENY");
    } else {
      builder.setSingleValue("UNKNOWN");
    }

    return builder.build();
  }
}
