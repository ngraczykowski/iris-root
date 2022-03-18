package com.silenteight.payments.bridge.svb.learning.step.etl.category.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;
import com.silenteight.payments.bridge.datasource.FeatureInputSpecification;
import com.silenteight.payments.bridge.datasource.category.CreateCategoryValuesProcess;
import com.silenteight.payments.bridge.datasource.category.dto.CategoryValueStructured;
import com.silenteight.payments.bridge.datasource.category.dto.CategoryValueStructured.AlertedData;
import com.silenteight.payments.bridge.datasource.category.dto.CategoryValueStructured.WatchlistData;
import com.silenteight.payments.bridge.datasource.category.dto.CategoryValueUnstructured;
import com.silenteight.payments.bridge.svb.learning.domain.EtlHit;
import com.silenteight.payments.bridge.svb.learning.domain.HitComposite;
import com.silenteight.payments.bridge.svb.learning.step.etl.category.port.CreateCategoriesUseCase;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
class CreateCategoriesValuesService implements CreateCategoriesUseCase {

  private final CreateCategoryValuesProcess createCategoryValues;

  @Override
  public void createCategoryValues(
      final List<EtlHit> etlHits, final RegisterAlertResponse registeredAlert,
      final FeatureInputSpecification featureInputSpecification) {

    var categoryValuesStructured = etlHits.stream()
        .map(hit -> getCategoryValueStructured(registeredAlert, hit))
        .collect(toList());

    createCategoryValues.createStructuredCategoryValues(
        categoryValuesStructured, featureInputSpecification);
  }

  @Override
  public void createUnstructuredCategoryValues(
      List<HitComposite> hitComposites, RegisterAlertResponse registeredAlert,
      FeatureInputSpecification featureInputSpecification) {

    var categoryValueUnstructured = hitComposites.stream()
        .map(hit -> createCategoryValueUnstructured(registeredAlert, hit))
        .collect(toList());

    createCategoryValues.createUnstructuredCategoryValues(
        categoryValueUnstructured, featureInputSpecification);
  }

  private static CategoryValueUnstructured createCategoryValueUnstructured(
      RegisterAlertResponse registeredAlert, HitComposite hit) {
    return CategoryValueUnstructured.builder()
        .matchName(registeredAlert.getMatchName(hit.getMatchId()))
        .alertName(registeredAlert.getAlertName())
        .tag(hit.getFkcoVMatchedTag())
        .solutionType(hit.getSolutionType())
        .watchlistType(hit.getWatchlistType())
        .allMatchingFieldValues(hit.getFkcoVMatchedTagContent())
        .build();
  }

  private static CategoryValueStructured getCategoryValueStructured(
      RegisterAlertResponse registeredAlert, EtlHit hit) {
    return CategoryValueStructured.builder()
        .matchName(registeredAlert.getMatchName(hit.getMatchId()))
        .alertName(registeredAlert.getAlertName())
        .alertedData(getAlertedData(hit))
        .watchlistData(getWatchlistData(hit))
        .build();
  }

  private static AlertedData getAlertedData(EtlHit hit) {
    return AlertedData.builder()
        .names(hit.getAlertedPartyNames())
        .accountNumber(getAccountNumber(hit.getAlertedPartyData()))
        .alertPartyEntities(hit.getAlertedPartyEntities())
        .build();
  }

  private static WatchlistData getWatchlistData(EtlHit hit) {
    return WatchlistData.builder()
        .ofacId(hit.getFkcoVListFmmId())
        .watchlistName(hit.getWatchlistName())
        .watchlistType(hit.getWatchlistType().toString())
        .country(hit.getHitComposite().getFkcoVListCountry())
        .build();
  }

  private static String getAccountNumber(AlertedPartyData alertedPartyData) {
    var accountNumber = alertedPartyData.getAccountNumber();
    return StringUtils.isBlank(accountNumber) ? "N/A" : accountNumber.toUpperCase().trim();
  }
}
