package com.silenteight.universaldatasource.app.feature.adapter.incoming;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsRequest;
import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsResponse;
import com.silenteight.datasource.api.allowlist.v1.AllowListFeatureInput;
import com.silenteight.datasource.api.allowlist.v1.BatchGetMatchAllowListInputsRequest;
import com.silenteight.datasource.api.allowlist.v1.BatchGetMatchAllowListInputsResponse;
import com.silenteight.datasource.api.bankidentificationcodes.v1.BankIdentificationCodesFeatureInput;
import com.silenteight.datasource.api.bankidentificationcodes.v1.BatchGetMatchBankIdentificationCodesInputsRequest;
import com.silenteight.datasource.api.bankidentificationcodes.v1.BatchGetMatchBankIdentificationCodesInputsResponse;
import com.silenteight.datasource.api.country.v1.BatchGetMatchCountryInputsRequest;
import com.silenteight.datasource.api.country.v1.BatchGetMatchCountryInputsResponse;
import com.silenteight.datasource.api.country.v1.CountryFeatureInput;
import com.silenteight.datasource.api.date.v1.BatchGetMatchDateInputsRequest;
import com.silenteight.datasource.api.date.v1.BatchGetMatchDateInputsResponse;
import com.silenteight.datasource.api.date.v1.DateFeatureInput;
import com.silenteight.datasource.api.document.v1.BatchGetMatchDocumentInputsRequest;
import com.silenteight.datasource.api.document.v1.BatchGetMatchDocumentInputsResponse;
import com.silenteight.datasource.api.document.v1.DocumentFeatureInput;
import com.silenteight.datasource.api.event.v1.BatchGetMatchEventInputsRequest;
import com.silenteight.datasource.api.event.v1.BatchGetMatchEventInputsResponse;
import com.silenteight.datasource.api.event.v1.EventFeatureInput;
import com.silenteight.datasource.api.freetext.v1.BatchGetMatchFreeTextInputsRequest;
import com.silenteight.datasource.api.freetext.v1.BatchGetMatchFreeTextInputsResponse;
import com.silenteight.datasource.api.freetext.v1.FreeTextFeatureInput;
import com.silenteight.datasource.api.gender.v1.BatchGetMatchGenderInputsRequest;
import com.silenteight.datasource.api.gender.v1.BatchGetMatchGenderInputsResponse;
import com.silenteight.datasource.api.gender.v1.GenderFeatureInput;
import com.silenteight.datasource.api.historicaldecisions.v1.BatchGetMatchHistoricalDecisionsInputsRequest;
import com.silenteight.datasource.api.historicaldecisions.v1.BatchGetMatchHistoricalDecisionsInputsResponse;
import com.silenteight.datasource.api.historicaldecisions.v1.HistoricalDecisionsInput.HistoricalDecisionsFeatureInput;
import com.silenteight.datasource.api.ispep.v2.BatchGetMatchIsPepInputsRequest;
import com.silenteight.datasource.api.ispep.v2.BatchGetMatchIsPepInputsResponse;
import com.silenteight.datasource.api.ispep.v2.IsPepFeatureInput;
import com.silenteight.datasource.api.location.v1.BatchGetMatchLocationInputsRequest;
import com.silenteight.datasource.api.location.v1.BatchGetMatchLocationInputsResponse;
import com.silenteight.datasource.api.location.v1.LocationFeatureInput;
import com.silenteight.datasource.api.name.v1.BatchGetMatchNameInputsRequest;
import com.silenteight.datasource.api.name.v1.BatchGetMatchNameInputsResponse;
import com.silenteight.datasource.api.name.v1.NameFeatureInput;
import com.silenteight.datasource.api.nationalid.v1.BatchGetMatchNationalIdInputsRequest;
import com.silenteight.datasource.api.nationalid.v1.BatchGetMatchNationalIdInputsResponse;
import com.silenteight.datasource.api.nationalid.v1.NationalIdFeatureInput;
import com.silenteight.datasource.api.transaction.v1.BatchGetMatchTransactionInputsRequest;
import com.silenteight.datasource.api.transaction.v1.BatchGetMatchTransactionInputsResponse;
import com.silenteight.datasource.api.transaction.v1.TransactionFeatureInput;
import com.silenteight.universaldatasource.app.feature.port.incoming.BatchCreateMatchFeaturesUseCase;
import com.silenteight.universaldatasource.app.feature.port.incoming.BatchGetFeatureInputUseCase;

import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import javax.validation.Valid;

@Component
@RequiredArgsConstructor
class FeatureAdapter {

  private static final String LOCATION_FEATURE_INPUT =
      LocationFeatureInput.class.getCanonicalName();
  private static final String NAME_FEATURE_INPUT = NameFeatureInput.class.getCanonicalName();
  private static final String FREE_TEXT_INPUT = FreeTextFeatureInput.class.getCanonicalName();
  private static final String ALLOW_LIST_INPUT = AllowListFeatureInput.class.getCanonicalName();
  private static final String COUNTRY_INPUT = CountryFeatureInput.class.getCanonicalName();
  private static final String DATE_INPUT = DateFeatureInput.class.getCanonicalName();
  private static final String DOCUMENT_INPUT = DocumentFeatureInput.class.getCanonicalName();
  private static final String EVENT_INPUT = EventFeatureInput.class.getCanonicalName();
  private static final String GENDER_INPUT = GenderFeatureInput.class.getCanonicalName();
  private static final String HISTORICAL_DECISIONS_INPUT =
      HistoricalDecisionsFeatureInput.class.getCanonicalName();
  private static final String IS_PEP_INPUT = IsPepFeatureInput.class.getCanonicalName();
  private static final String NATIONAL_ID_INPUT = NationalIdFeatureInput.class.getCanonicalName();
  private static final String TRANSACTION_INPUT = TransactionFeatureInput.class.getCanonicalName();
  private static final String BANK_IDENTIFICATION_CODES_INPUT =
      BankIdentificationCodesFeatureInput.class.getCanonicalName();

  private final BatchGetFeatureInputUseCase getUseCase;
  private final BatchCreateMatchFeaturesUseCase addUseCase;
  private final BatchFeatureRequestFactory batchFeatureRequestFactory;

  BatchCreateAgentInputsResponse batchAgentInputs(
      @Valid BatchCreateAgentInputsRequest request) {
    return addUseCase.batchCreateMatchFeatures(request.getAgentInputsList());
  }

  void batchGetMatchNameInputs(
      @Valid BatchGetMatchNameInputsRequest request,
      Consumer<BatchGetMatchNameInputsResponse> onNext) {

    var featureRequest = batchFeatureRequestFactory.create(NAME_FEATURE_INPUT,
        request.getMatchesList(), request.getFeaturesList());

    getUseCase.batchGetFeatureInput(
        featureRequest,
        batch -> onNext.accept(batch.castResponse(BatchGetMatchNameInputsResponse.class)));
  }

  void batchGetMatchLocationInputs(
      @Valid BatchGetMatchLocationInputsRequest request,
      Consumer<BatchGetMatchLocationInputsResponse> onNext) {

    var featureRequest = batchFeatureRequestFactory.create(LOCATION_FEATURE_INPUT,
        request.getMatchesList(), request.getFeaturesList());

    getUseCase.batchGetFeatureInput(
        featureRequest,
        batch -> onNext.accept(batch.castResponse(BatchGetMatchLocationInputsResponse.class)));
  }

  void batchGetMatchFreeTextInputs(
      @Valid BatchGetMatchFreeTextInputsRequest request,
      Consumer<BatchGetMatchFreeTextInputsResponse> onNext) {

    var featureRequest = batchFeatureRequestFactory.create(FREE_TEXT_INPUT,
        request.getMatchesList(), request.getFeaturesList());

    getUseCase.batchGetFeatureInput(
        featureRequest,
        batch -> onNext.accept(batch.castResponse(BatchGetMatchFreeTextInputsResponse.class)));
  }

  public void batchGetMatchNationalIdInputs(
      @Valid BatchGetMatchNationalIdInputsRequest request,
      Consumer<BatchGetMatchNationalIdInputsResponse> onNext) {

    var featureRequest = batchFeatureRequestFactory.create(NATIONAL_ID_INPUT,
        request.getMatchesList(), request.getFeaturesList());

    getUseCase.batchGetFeatureInput(
        featureRequest,
        batch -> onNext.accept(batch.castResponse(BatchGetMatchNationalIdInputsResponse.class)));
  }

  public void batchGetMatchTransactionInputs(
      @Valid BatchGetMatchTransactionInputsRequest request,
      Consumer<BatchGetMatchTransactionInputsResponse> onNext) {

    var featureRequest = batchFeatureRequestFactory.create(TRANSACTION_INPUT,
        request.getMatchesList(), request.getFeaturesList());

    getUseCase.batchGetFeatureInput(
        featureRequest,
        batch -> onNext.accept(batch.castResponse(BatchGetMatchTransactionInputsResponse.class)));
  }

  public void batchGetMatchAllowListInputs(
      @Valid BatchGetMatchAllowListInputsRequest request,
      Consumer<BatchGetMatchAllowListInputsResponse> onNext) {

    var featureRequest = batchFeatureRequestFactory.create(ALLOW_LIST_INPUT,
        request.getMatchesList(), request.getFeaturesList());

    getUseCase.batchGetFeatureInput(
        featureRequest,
        batch -> onNext.accept(batch.castResponse(BatchGetMatchAllowListInputsResponse.class)));
  }

  public void batchGetMatchBankIdentificationCodesInputs(
      @Valid BatchGetMatchBankIdentificationCodesInputsRequest request,
      Consumer<BatchGetMatchBankIdentificationCodesInputsResponse> onNext) {

    var featureRequest = batchFeatureRequestFactory.create(BANK_IDENTIFICATION_CODES_INPUT,
        request.getMatchesList(), request.getFeaturesList());

    getUseCase.batchGetFeatureInput(
        featureRequest,
        batch -> onNext.accept(
            batch.castResponse(BatchGetMatchBankIdentificationCodesInputsResponse.class)));
  }

  public void batchGetMatchCountryInputs(
      @Valid BatchGetMatchCountryInputsRequest request,
      Consumer<BatchGetMatchCountryInputsResponse> onNext) {

    var featureRequest = batchFeatureRequestFactory.create(COUNTRY_INPUT,
        request.getMatchesList(), request.getFeaturesList());

    getUseCase.batchGetFeatureInput(
        featureRequest,
        batch -> onNext.accept(batch.castResponse(BatchGetMatchCountryInputsResponse.class)));
  }

  public void batchGetMatchDateInputs(
      @Valid BatchGetMatchDateInputsRequest request,
      Consumer<BatchGetMatchDateInputsResponse> onNext) {

    var featureRequest = batchFeatureRequestFactory.create(DATE_INPUT,
        request.getMatchesList(), request.getFeaturesList());

    getUseCase.batchGetFeatureInput(
        featureRequest,
        batch -> onNext.accept(batch.castResponse(BatchGetMatchDateInputsResponse.class)));
  }

  public void batchGetMatchDocumentInputs(
      @Valid BatchGetMatchDocumentInputsRequest request,
      Consumer<BatchGetMatchDocumentInputsResponse> onNext) {

    var featureRequest = batchFeatureRequestFactory.create(DOCUMENT_INPUT,
        request.getMatchesList(), request.getFeaturesList());

    getUseCase.batchGetFeatureInput(
        featureRequest,
        batch -> onNext.accept(batch.castResponse(BatchGetMatchDocumentInputsResponse.class)));
  }

  public void batchGetMatchEventInputs(
      @Valid BatchGetMatchEventInputsRequest request,
      Consumer<BatchGetMatchEventInputsResponse> onNext) {

    var featureRequest = batchFeatureRequestFactory.create(EVENT_INPUT,
        request.getMatchesList(), request.getFeaturesList());

    getUseCase.batchGetFeatureInput(
        featureRequest,
        batch -> onNext.accept(batch.castResponse(BatchGetMatchEventInputsResponse.class)));

  }

  public void batchGetMatchGenderInputs(
      @Valid BatchGetMatchGenderInputsRequest request,
      Consumer<BatchGetMatchGenderInputsResponse> onNext) {

    var featureRequest = batchFeatureRequestFactory.create(GENDER_INPUT,
        request.getMatchesList(), request.getFeaturesList());

    getUseCase.batchGetFeatureInput(
        featureRequest,
        batch -> onNext.accept(batch.castResponse(BatchGetMatchGenderInputsResponse.class)));
  }

  public void batchGetMatchHistoricalDecisionsInputs(
      @Valid BatchGetMatchHistoricalDecisionsInputsRequest request,
      Consumer<BatchGetMatchHistoricalDecisionsInputsResponse> onNext) {

    var featureRequest = batchFeatureRequestFactory.create(HISTORICAL_DECISIONS_INPUT,
        request.getMatchesList(), request.getFeaturesList());

    getUseCase.batchGetFeatureInput(
        featureRequest,
        batch -> onNext.accept(
            batch.castResponse(BatchGetMatchHistoricalDecisionsInputsResponse.class)));
  }

  public void batchGetMatchIsPepInputs(
      @Valid BatchGetMatchIsPepInputsRequest request,
      Consumer<BatchGetMatchIsPepInputsResponse> onNext) {

    var featureRequest = batchFeatureRequestFactory.create(IS_PEP_INPUT,
        request.getMatchesList(), request.getFeaturesList());

    getUseCase.batchGetFeatureInput(
        featureRequest,
        batch -> onNext.accept(batch.castResponse(BatchGetMatchIsPepInputsResponse.class)));
  }
}
