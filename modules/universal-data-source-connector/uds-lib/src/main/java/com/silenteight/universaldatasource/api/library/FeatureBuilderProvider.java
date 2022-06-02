package com.silenteight.universaldatasource.api.library;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.universaldatasource.api.library.allowlist.v1.AllowListFeatureInputOut;
import com.silenteight.universaldatasource.api.library.bankidentificationcodes.v1.BankIdentificationCodesFeatureInputOut;
import com.silenteight.universaldatasource.api.library.comparedates.v1.CompareDatesFeatureInputOut;
import com.silenteight.universaldatasource.api.library.country.v1.CountryFeatureInputOut;
import com.silenteight.universaldatasource.api.library.date.v1.DateFeatureInputOut;
import com.silenteight.universaldatasource.api.library.document.v1.DocumentFeatureInputOut;
import com.silenteight.universaldatasource.api.library.event.v1.EventFeatureOut;
import com.silenteight.universaldatasource.api.library.freetext.v1.FreeTextFeatureInputOut;
import com.silenteight.universaldatasource.api.library.gender.v1.GenderFeatureInputOut;
import com.silenteight.universaldatasource.api.library.historicaldecisions.v1.HistoricalDecisionsFeatureInputOut;
import com.silenteight.universaldatasource.api.library.isofgivendocumenttype.v1.IsOfGivenDocumentTypeFeatureInputOut;
import com.silenteight.universaldatasource.api.library.ispep.v2.IsPepFeatureInputOut;
import com.silenteight.universaldatasource.api.library.location.v1.LocationFeatureInputOut;
import com.silenteight.universaldatasource.api.library.name.v1.NameFeatureInputOut;
import com.silenteight.universaldatasource.api.library.nationalid.v1.NationalIdFeatureInputOut;
import com.silenteight.universaldatasource.api.library.transaction.v1.TransactionFeatureInputOut;

public interface FeatureBuilderProvider {

  void build(AllowListFeatureInputOut input, FeatureInput.Builder builder);

  void build(BankIdentificationCodesFeatureInputOut input, FeatureInput.Builder builder);

  void build(CountryFeatureInputOut input, FeatureInput.Builder builder);

  void build(DateFeatureInputOut input, FeatureInput.Builder builder);

  void build(DocumentFeatureInputOut input, FeatureInput.Builder builder);

  void build(EventFeatureOut input, FeatureInput.Builder builder);

  void build(FreeTextFeatureInputOut input, FeatureInput.Builder builder);

  void build(GenderFeatureInputOut input, FeatureInput.Builder builder);

  void build(HistoricalDecisionsFeatureInputOut input, FeatureInput.Builder builder);

  void build(IsPepFeatureInputOut input, FeatureInput.Builder builder);

  void build(LocationFeatureInputOut input, FeatureInput.Builder builder);

  void build(NameFeatureInputOut input, FeatureInput.Builder builder);

  void build(NationalIdFeatureInputOut input, FeatureInput.Builder builder);

  void build(TransactionFeatureInputOut input, FeatureInput.Builder builder);

  void build(IsOfGivenDocumentTypeFeatureInputOut input, FeatureInput.Builder builder);

  void build(CompareDatesFeatureInputOut input, FeatureInput.Builder builder);
}
