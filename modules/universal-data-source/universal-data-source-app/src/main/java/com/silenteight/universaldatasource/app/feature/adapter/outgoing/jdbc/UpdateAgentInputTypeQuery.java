package com.silenteight.universaldatasource.app.feature.adapter.outgoing.jdbc;

import lombok.RequiredArgsConstructor;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class UpdateAgentInputTypeQuery {

  @Language("PostgreSQL")
  private static final String SQL =
      "UPDATE uds_feature_input\n"
          + "SET agent_input_type = CASE agent_input_type\n"
          + "  WHEN 'NameFeatureInput' \n"
          + "    THEN 'com.silenteight.datasource.api.name.v1.NameFeatureInput'\n"
          + "  WHEN 'LocationFeatureInput' \n"
          + "    THEN 'com.silenteight.datasource.api.location.v1.LocationFeatureInput'\n"
          + "  WHEN 'FreeTextFeatureInput' \n"
          + "    THEN 'com.silenteight.datasource.api.freetext.v1.FreeTextFeatureInput'\n"
          + "  WHEN 'AllowListFeatureInput' \n"
          + "    THEN 'com.silenteight.datasource.api.allowlist.v1.AllowListFeatureInput'\n"
          + "  WHEN 'CountryFeatureInput' \n"
          + "    THEN 'com.silenteight.datasource.api.country.v1.CountryFeatureInput'\n"
          + "  WHEN 'DateFeatureInput' \n"
          + "    THEN 'com.silenteight.datasource.api.date.v1.DateFeatureInput'\n"
          + "  WHEN 'DocumentFeatureInput' \n"
          + "    THEN 'com.silenteight.datasource.api.document.v1.DocumentFeatureInput'\n"
          + "  WHEN 'EventFeatureInput' \n"
          + "    THEN 'com.silenteight.datasource.api.event.v1.EventFeatureInput'\n"
          + "  WHEN 'GenderFeatureInput' \n"
          + "    THEN 'com.silenteight.datasource.api.gender.v1.GenderFeatureInput'\n"
          + "  WHEN 'HistoricalDecisionsFeatureInput' \n"
          + "    THEN 'com.silenteight.datasource.api.historicaldecisions.v1."
          + "HistoricalDecisionsInput.HistoricalDecisionsFeatureInput'\n"
          + "  WHEN 'Feature' \n"
          + "    THEN 'com.silenteight.datasource.api.ispep.v1."
          + "BatchGetMatchIsPepSolutionsResponse.Feature'\n"
          + "  WHEN 'NationalIdFeatureInput' \n"
          + "    THEN 'com.silenteight.datasource.api.nationalid.v1.NationalIdFeatureInput'\n"
          + "  WHEN 'TransactionFeatureInput' \n"
          + "    THEN 'com.silenteight.datasource.api.transaction.v1.TransactionFeatureInput'\n"
          + "  WHEN 'BankIdentificationCodesFeatureInput' \n"
          + "    THEN 'com.silenteight.datasource.api.bankidentificationcodes.v1."
          + "BankIdentificationCodesFeatureInput'\n"
          + "END\n"
          + "WHERE agent_input_type IN ('NameFeatureInput',\n"
          + "                           'LocationFeatureInput',\n"
          + "                           'FreeTextFeatureInput',\n"
          + "                           'AllowListFeatureInput',\n"
          + "                           'CountryFeatureInput',\n"
          + "                           'DateFeatureInput',\n"
          + "                           'DocumentFeatureInput',\n"
          + "                           'EventFeatureInput',\n"
          + "                           'GenderFeatureInput',\n"
          + "                           'HistoricalDecisionsFeatureInput',\n"
          + "                           'Feature',\n"
          + "                           'NationalIdFeatureInput',\n"
          + "                           'TransactionFeatureInput',\n"
          + "                           'BankIdentificationCodesFeatureInput')";

  private final JdbcTemplate jdbcTemplate;

  int execute() {
    return jdbcTemplate.update(SQL);
  }
}
