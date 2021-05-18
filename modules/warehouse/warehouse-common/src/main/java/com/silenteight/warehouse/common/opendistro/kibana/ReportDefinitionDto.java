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
class ReportDefinitionDto {

  String id;

  @Getter(AccessLevel.PACKAGE)
  ReportDefinitionDetails reportDefinitionDetails;

  public String getReportName() {
    return getReportParams().getReportName();
  }

  public void replaceExistingSearchId(String newSearchId) {
    CoreParams coreParams = getCoreParams();
    String currentSearchId = of(coreParams)
        .map(CoreParams::getSavedSearchId)
        .filter(searchId -> !searchId.isBlank())
        .orElseThrow(() -> new IllegalStateException(
            "savedSearchId is empty: id=" + id + ", body=" + reportDefinitionDetails));

    String newBaseUrl = coreParams.getBaseUrl().replace(currentSearchId, newSearchId);

    coreParams.setBaseUrl(newBaseUrl);
    coreParams.setSavedSearchId(newSearchId);
  }

  private CoreParams getCoreParams() {
    return of(getReportParams())
        .map(ReportParams::getCoreParams)
        .orElseThrow(() -> new IllegalStateException(
            "coreParams is empty: id=" + id + ", body=" + reportDefinitionDetails));
  }

  private ReportParams getReportParams() {
    return ofNullable(reportDefinitionDetails)
        .map(ReportDefinitionDetails::getReportParams)
        .orElseThrow(() -> new IllegalStateException(
            "reportParams is empty: id=" + id + ", body=" + reportDefinitionDetails));
  }

  String getOrigin() {
    return getCoreParams().getOrigin();
  }

  void clearOrigin() {
    getCoreParams().setOrigin(null);
  }
}
