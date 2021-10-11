package com.silenteight.universaldatasource.app.feature.adapter.incoming;

import com.silenteight.datasource.api.bankidentificationcodes.v1.BatchGetMatchBankIdentificationCodesInputsRequest;

import com.silenteight.datasource.api.bankidentificationcodes.v1.BatchGetMatchBankIdentificationCodesInputsResponse;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsRequest;
import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsResponse;
import com.silenteight.datasource.api.allowlist.v1.BatchGetMatchAllowListInputsRequest;
import com.silenteight.datasource.api.allowlist.v1.BatchGetMatchAllowListInputsResponse;
import com.silenteight.datasource.api.country.v1.BatchGetMatchCountryInputsRequest;
import com.silenteight.datasource.api.country.v1.BatchGetMatchCountryInputsResponse;
import com.silenteight.datasource.api.date.v1.BatchGetMatchDateInputsRequest;
import com.silenteight.datasource.api.date.v1.BatchGetMatchDateInputsResponse;
import com.silenteight.datasource.api.document.v1.BatchGetMatchDocumentInputsRequest;
import com.silenteight.datasource.api.document.v1.BatchGetMatchDocumentInputsResponse;
import com.silenteight.datasource.api.event.v1.BatchGetMatchEventInputsRequest;
import com.silenteight.datasource.api.event.v1.BatchGetMatchEventInputsResponse;
import com.silenteight.datasource.api.freetext.v1.BatchGetMatchFreeTextInputsRequest;
import com.silenteight.datasource.api.freetext.v1.BatchGetMatchFreeTextInputsResponse;
import com.silenteight.datasource.api.gender.v1.BatchGetMatchGenderInputsRequest;
import com.silenteight.datasource.api.gender.v1.BatchGetMatchGenderInputsResponse;
import com.silenteight.datasource.api.historicaldecisions.v1.BatchGetMatchHistoricalDecisionsInputsRequest;
import com.silenteight.datasource.api.historicaldecisions.v1.BatchGetMatchHistoricalDecisionsInputsResponse;
import com.silenteight.datasource.api.ispep.v1.BatchGetMatchIsPepSolutionsRequest;
import com.silenteight.datasource.api.ispep.v1.BatchGetMatchIsPepSolutionsResponse;
import com.silenteight.datasource.api.location.v1.BatchGetMatchLocationInputsRequest;
import com.silenteight.datasource.api.location.v1.BatchGetMatchLocationInputsResponse;
import com.silenteight.datasource.api.name.v1.BatchGetMatchNameInputsRequest;
import com.silenteight.datasource.api.name.v1.BatchGetMatchNameInputsResponse;
import com.silenteight.datasource.api.nationalid.v1.BatchGetMatchNationalIdInputsRequest;
import com.silenteight.datasource.api.nationalid.v1.BatchGetMatchNationalIdInputsResponse;
import com.silenteight.datasource.api.transaction.v1.BatchGetMatchTransactionInputsRequest;
import com.silenteight.datasource.api.transaction.v1.BatchGetMatchTransactionInputsResponse;
import com.silenteight.universaldatasource.app.feature.model.BatchFeatureRequest;
import com.silenteight.universaldatasource.app.feature.port.incoming.BatchCreateMatchFeaturesUseCase;
import com.silenteight.universaldatasource.app.feature.port.incoming.BatchGetFeatureInputUseCase;

import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import javax.validation.Valid;

@Component
@RequiredArgsConstructor
class FeatureAdapter {

  private static final String LOCATION_FEATURE_INPUT = "LocationFeatureInput";
  private static final String NAME_FEATURE_INPUT = "NameFeatureInput";
  private static final String FREE_TEXT_INPUT = "FreeTextFeatureInput";
  private static final String ALLOW_LIST_INPUT = "AllowListFeatureInput";
  private static final String COUNTRY_INPUT = "CountryFeatureInput";
  private static final String DATE_INPUT = "DateFeatureInput";
  private static final String DOCUMENT_INPUT = "DocumentFeatureInput";
  private static final String EVENT_INPUT = "EventFeatureInput";
  private static final String GENDER_INPUT = "GenderFeatureInput";
  private static final String HISTORICAL_DECISIONS_INPUT = "HistoricalDecisionsFeatureInput";
  private static final String IS_PEP_INPUT = "Feature";
  private static final String NATIONAL_ID_INPUT = "NationalIdFeatureInput";
  private static final String TRANSACTION_INPUT = "TransactionFeatureInput";
  private static final String BANK_IDENTIFICATION_CODES_INPUT = "BankIdentificationCodesFeatureInput";

  private final BatchGetFeatureInputUseCase getUseCase;
  private final BatchCreateMatchFeaturesUseCase addUseCase;

  BatchCreateAgentInputsResponse batchAgentInputs(
      @Valid BatchCreateAgentInputsRequest request) {
    return addUseCase.batchCreateMatchFeatures(request.getAgentInputsList());
  }

  void batchGetMatchNameInputs(
      @Valid BatchGetMatchNameInputsRequest request,
      Consumer<BatchGetMatchNameInputsResponse> onNext) {

    var featureRequest = BatchFeatureRequest.builder()
        .agentInputType(NAME_FEATURE_INPUT)
        .matches(request.getMatchesList())
        .features(request.getFeaturesList())
        .build();

    getUseCase.batchGetFeatureInput(
        featureRequest,
        batch -> onNext.accept(batch.castResponse(BatchGetMatchNameInputsResponse.class)));
  }

  void batchGetMatchLocationInputs(
      @Valid BatchGetMatchLocationInputsRequest request,
      Consumer<BatchGetMatchLocationInputsResponse> onNext) {

    var featureRequest = BatchFeatureRequest.builder()
        .agentInputType(LOCATION_FEATURE_INPUT)
        .matches(request.getMatchesList())
        .features(request.getFeaturesList())
        .build();

    getUseCase.batchGetFeatureInput(
        featureRequest,
        batch -> onNext.accept(batch.castResponse(BatchGetMatchLocationInputsResponse.class)));
  }

  void batchGetMatchFreeTextInputs(
      @Valid BatchGetMatchFreeTextInputsRequest request,
      Consumer<BatchGetMatchFreeTextInputsResponse> onNext) {

    var featureRequest = BatchFeatureRequest.builder()
        .agentInputType(FREE_TEXT_INPUT)
        .matches(request.getMatchesList())
        .features(request.getFeaturesList())
        .build();

    getUseCase.batchGetFeatureInput(
        featureRequest,
        batch -> onNext.accept(batch.castResponse(BatchGetMatchFreeTextInputsResponse.class)));
  }

  public void batchGetMatchNationalIdInputs(
      @Valid BatchGetMatchNationalIdInputsRequest request,
      Consumer<BatchGetMatchNationalIdInputsResponse> onNext) {

    var featureRequest = BatchFeatureRequest.builder()
        .agentInputType(NATIONAL_ID_INPUT)
        .matches(request.getMatchesList())
        .features(request.getFeaturesList())
        .build();

    getUseCase.batchGetFeatureInput(
        featureRequest,
        batch -> onNext.accept(batch.castResponse(BatchGetMatchNationalIdInputsResponse.class)));
  }

  public void batchGetMatchTransactionInputs(
      @Valid BatchGetMatchTransactionInputsRequest request,
      Consumer<BatchGetMatchTransactionInputsResponse> onNext) {

    var featureRequest = BatchFeatureRequest.builder()
        .agentInputType(TRANSACTION_INPUT)
        .matches(request.getMatchesList())
        .features(request.getFeaturesList())
        .build();

    getUseCase.batchGetFeatureInput(
        featureRequest,
        batch -> onNext.accept(batch.castResponse(BatchGetMatchTransactionInputsResponse.class)));
  }

  public void batchGetMatchAllowListInputs(
      @Valid BatchGetMatchAllowListInputsRequest request,
      Consumer<BatchGetMatchAllowListInputsResponse> onNext) {

    var featureRequest = BatchFeatureRequest.builder()
        .agentInputType(ALLOW_LIST_INPUT)
        .matches(request.getMatchesList())
        .features(request.getFeaturesList())
        .build();

    getUseCase.batchGetFeatureInput(
        featureRequest,
        batch -> onNext.accept(batch.castResponse(BatchGetMatchAllowListInputsResponse.class)));
  }

  public void batchGetMatchBankIdentificationCodesInputs(
      @Valid BatchGetMatchBankIdentificationCodesInputsRequest request,
      Consumer<BatchGetMatchBankIdentificationCodesInputsResponse> onNext) {

    var featureRequest = BatchFeatureRequest.builder()
        .agentInputType(BANK_IDENTIFICATION_CODES_INPUT)
        .matches(request.getMatchesList())
        .features(request.getFeaturesList())
        .build();

    getUseCase.batchGetFeatureInput(
        featureRequest,
        batch -> onNext.accept(batch.castResponse(BatchGetMatchBankIdentificationCodesInputsResponse.class)));
  }

  public void batchGetMatchCountryInputs(
      @Valid BatchGetMatchCountryInputsRequest request,
      Consumer<BatchGetMatchCountryInputsResponse> onNext) {

    var featureRequest = BatchFeatureRequest.builder()
        .agentInputType(COUNTRY_INPUT)
        .matches(request.getMatchesList())
        .features(request.getFeaturesList())
        .build();

    getUseCase.batchGetFeatureInput(
        featureRequest,
        batch -> onNext.accept(batch.castResponse(BatchGetMatchCountryInputsResponse.class)));
  }

  public void batchGetMatchDateInputs(
      @Valid BatchGetMatchDateInputsRequest request,
      Consumer<BatchGetMatchDateInputsResponse> onNext) {

    var featureRequest = BatchFeatureRequest.builder()
        .agentInputType(DATE_INPUT)
        .matches(request.getMatchesList())
        .features(request.getFeaturesList())
        .build();

    getUseCase.batchGetFeatureInput(
        featureRequest,
        batch -> onNext.accept(batch.castResponse(BatchGetMatchDateInputsResponse.class)));
  }

  public void batchGetMatchDocumentInputs(
      @Valid BatchGetMatchDocumentInputsRequest request,
      Consumer<BatchGetMatchDocumentInputsResponse> onNext) {

    var featureRequest = BatchFeatureRequest.builder()
        .agentInputType(DOCUMENT_INPUT)
        .matches(request.getMatchesList())
        .features(request.getFeaturesList())
        .build();

    getUseCase.batchGetFeatureInput(
        featureRequest,
        batch -> onNext.accept(batch.castResponse(BatchGetMatchDocumentInputsResponse.class)));
  }

  public void batchGetMatchEventInputs(
      @Valid BatchGetMatchEventInputsRequest request,
      Consumer<BatchGetMatchEventInputsResponse> onNext) {

    var featureRequest = BatchFeatureRequest.builder()
        .agentInputType(EVENT_INPUT)
        .matches(request.getMatchesList())
        .features(request.getFeaturesList())
        .build();

    getUseCase.batchGetFeatureInput(
        featureRequest,
        batch -> onNext.accept(batch.castResponse(BatchGetMatchEventInputsResponse.class)));

  }

  public void batchGetMatchGenderInputs(
      @Valid BatchGetMatchGenderInputsRequest request,
      Consumer<BatchGetMatchGenderInputsResponse> onNext) {

    var featureRequest = BatchFeatureRequest.builder()
        .agentInputType(GENDER_INPUT)
        .matches(request.getMatchesList())
        .features(request.getFeaturesList())
        .build();

    getUseCase.batchGetFeatureInput(
        featureRequest,
        batch -> onNext.accept(batch.castResponse(BatchGetMatchGenderInputsResponse.class)));
  }

  public void batchGetMatchHistoricalDecisionsInputs(
      @Valid BatchGetMatchHistoricalDecisionsInputsRequest request,
      Consumer<BatchGetMatchHistoricalDecisionsInputsResponse> onNext) {

    var featureRequest = BatchFeatureRequest.builder()
        .agentInputType(HISTORICAL_DECISIONS_INPUT)
        .matches(request.getMatchesList())
        .features(request.getFeaturesList())
        .build();

    getUseCase.batchGetFeatureInput(
        featureRequest,
        batch -> onNext.accept(
            batch.castResponse(BatchGetMatchHistoricalDecisionsInputsResponse.class)));
  }

  public void batchGetMatchIsPepSolutions(
      @Valid BatchGetMatchIsPepSolutionsRequest request,
      Consumer<BatchGetMatchIsPepSolutionsResponse> onNext) {

    var featureRequest = BatchFeatureRequest.builder()
        .agentInputType(IS_PEP_INPUT)
        .matches(request.getMatchesList())
        .features(request.getFeaturesList())
        .build();

    getUseCase.batchGetFeatureInput(
        featureRequest,
        batch -> onNext.accept(batch.castResponse(BatchGetMatchIsPepSolutionsResponse.class)));
  }
}



























