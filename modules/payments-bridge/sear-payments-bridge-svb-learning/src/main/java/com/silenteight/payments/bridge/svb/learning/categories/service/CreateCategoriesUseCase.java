package com.silenteight.payments.bridge.svb.learning.categories.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoriesRequest;
import com.silenteight.datasource.categories.api.v2.Category;
import com.silenteight.datasource.categories.api.v2.CategoryType;
import com.silenteight.payments.bridge.svb.learning.categories.port.outgoing.CreateCategoriesClient;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
class CreateCategoriesUseCase {

  private final CreateCategoriesClient createCategoriesClient;

  @EventListener(ApplicationReadyEvent.class)
  public void createCategories() {
    createCategoriesClient.createCategories(BatchCreateCategoriesRequest
        .newBuilder()
        .addAllCategories(
            List.of(
                chineseCommercialCategory(), delimiterCategory(), crossmatchCategory(),
                firstTokenAddressCategory(), oneLinerCategory(), sanctionedNationalityCategory(),
                specificCommonTermsCategory(), specificTermsCategory(), stripCategory(),
                twoLinesCategory()))
        .build());
  }

  private static Category chineseCommercialCategory() {
    return Category
        .newBuilder()
        .setName("categories/chineseCode")
        .setDisplayName("Chinese Commercial Code Category")
        .setType(CategoryType.ENUMERATED)
        .setMultiValue(false)
        .addAllAllowedValues(List.of("YES", "NO"))
        .build();
  }

  private static Category delimiterCategory() {
    return Category
        .newBuilder()
        .setName("categories/delimiter")
        .setDisplayName("Delimiter Category")
        .setType(CategoryType.ENUMERATED)
        .setMultiValue(false)
        .addAllAllowedValues(List.of("YES", "NO"))
        .build();
  }

  private static Category crossmatchCategory() {
    return Category
        .newBuilder()
        .setName("categories/crossmatch")
        .setDisplayName("Name Address Crossmatch Category")
        .setType(CategoryType.ENUMERATED)
        .setMultiValue(false)
        .addAllAllowedValues(List.of("NO_DECISION", "CROSSMATCH", "NO_CROSSMATCH"))
        .build();
  }

  private static Category firstTokenAddressCategory() {
    return Category
        .newBuilder()
        .setName("categories/firstTokenAddress")
        .setDisplayName("Matchtext First Token of Address Category")
        .setType(CategoryType.ENUMERATED)
        .setMultiValue(false)
        .addAllAllowedValues(List.of("YES", "NO"))
        .build();
  }

  private static Category oneLinerCategory() {
    return Category
        .newBuilder()
        .setName("categories/oneLiner")
        .setDisplayName("One Liner Category")
        .setType(CategoryType.ENUMERATED)
        .setMultiValue(false)
        .addAllAllowedValues(List.of("YES", "NO", "NO_DATA"))
        .build();
  }

  private static Category sanctionedNationalityCategory() {
    return Category
        .newBuilder()
        .setName("categories/sanctionedNationality")
        .setDisplayName("Sanctioned Nationality Category")
        .setType(CategoryType.ENUMERATED)
        .setMultiValue(false)
        .addAllAllowedValues(List.of("YES", "NO"))
        .build();
  }

  private static Category specificCommonTermsCategory() {
    return Category
        .newBuilder()
        .setName("categories/specificCommonTerms")
        .setDisplayName("Specific Common Terms Category")
        .setType(CategoryType.ENUMERATED)
        .setMultiValue(false)
        .addAllAllowedValues(List.of("YES", "NO"))
        .build();
  }

  private static Category specificTermsCategory() {
    return Category
        .newBuilder()
        .setName("categories/specificTerms")
        .setDisplayName("Specific Terms Category")
        .setType(CategoryType.ENUMERATED)
        .setMultiValue(false)
        .addAllAllowedValues(List.of("YES", "NO"))
        .build();
  }

  private static Category stripCategory() {
    return Category
        .newBuilder()
        .setName("categories/strip")
        .setDisplayName("Strip Category")
        .setType(CategoryType.ENUMERATED)
        .setMultiValue(false)
        .addAllAllowedValues(List.of("STRIPPED", "NOT_STRIPPED"))
        .build();
  }

  private static Category twoLinesCategory() {
    return Category
        .newBuilder()
        .setName("categories/twoLines")
        .setDisplayName("Two Lines Name Category")
        .setType(CategoryType.ENUMERATED)
        .setMultiValue(false)
        .addAllAllowedValues(List.of("YES", "NO", "NO_DATA"))
        .build();
  }
}
