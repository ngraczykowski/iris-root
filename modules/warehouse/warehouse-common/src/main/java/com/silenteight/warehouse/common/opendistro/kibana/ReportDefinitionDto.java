package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.*;

import com.silenteight.warehouse.common.opendistro.kibana.ReportDefinition.CoreParams;
import com.silenteight.warehouse.common.opendistro.kibana.ReportDefinition.ReportDefinitionDetails;
import com.silenteight.warehouse.common.opendistro.kibana.ReportDefinition.ReportParams;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

@Value
@AllArgsConstructor
@Builder
public class ReportDefinitionDto {

  String id;

  @Getter(AccessLevel.PACKAGE)
  ReportDefinitionDetails reportDefinitionDetails;

  public String getReportName() {
    return getReportParams().getReportName();
  }

  public String getSearchId() {
    return of(getCoreParams())
        .map(CoreParams::getSavedSearchId)
        .filter(searchId -> !searchId.isBlank())
        .orElseThrow(() -> new KibanaObjectEmptyFieldException("savedSearchId", getId(), this));
  }

  String getOrigin() {
    return getCoreParams().getOrigin();
  }

  void clearOrigin() {
    getCoreParams().setOrigin(null);
  }

  public void replaceExistingSearchId(String newSearchId) {
    CoreParams coreParams = getCoreParams();
    String currentSearchId = getSearchId();

    String newBaseUrl = coreParams.getBaseUrl().replace(currentSearchId, newSearchId);

    coreParams.setBaseUrl(newBaseUrl);
    coreParams.setSavedSearchId(newSearchId);
  }

  private CoreParams getCoreParams() {
    return of(getReportParams())
        .map(ReportParams::getCoreParams)
        .orElseThrow(() -> new KibanaObjectEmptyFieldException("coreParams", getId(), this));
  }

  private ReportParams getReportParams() {
    return ofNullable(reportDefinitionDetails)
        .map(ReportDefinitionDetails::getReportParams)
        .orElseThrow(() -> new KibanaObjectEmptyFieldException("reportParams", getId(), this));
  }
}
