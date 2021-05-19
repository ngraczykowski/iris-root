package com.silenteight.warehouse.report.tenant;

import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.common.opendistro.kibana.KibanaIndexPatternDto;
import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaClient;
import com.silenteight.warehouse.common.opendistro.kibana.ReportDefinitionDto;
import com.silenteight.warehouse.common.opendistro.kibana.SearchDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.silenteight.warehouse.common.opendistro.kibana.SavedObjectType.KIBANA_INDEX_PATTERN;
import static java.util.UUID.randomUUID;

@RequiredArgsConstructor
public class TenantService {

  private final OpendistroKibanaClient opendistroKibanaClient;
  private static final Integer MAX_ELEMENT_COUNT = 100;

  public Map<String, String> copyKibanaIndices(
      String sourceTenant, String targetTenant, String newElasticIndex) {

    List<KibanaIndexPatternDto> kibanaIndexPatterns = opendistroKibanaClient
        .listKibanaIndexPattern(sourceTenant, MAX_ELEMENT_COUNT);

    Map<String, String> idMapping = new HashMap<>();
    for (KibanaIndexPatternDto kibanaIndexPattern : kibanaIndexPatterns) {
      String sourceId = kibanaIndexPattern.getId();
      String targetId = randomUUID().toString();
      kibanaIndexPattern.setId(targetId);
      kibanaIndexPattern.setElasticIndexName(newElasticIndex);
      opendistroKibanaClient.createKibanaIndexPattern(targetTenant, kibanaIndexPattern);
      idMapping.put(sourceId, targetId);
    }

    return idMapping;
  }

  public Map<String, String> copySearchDefinition(
      String sourceTenant, String targetTenant, Map<String, String> kibanaIndexMapping) {

    List<SearchDto> searches = opendistroKibanaClient
        .listSavedSearchDefinitions(sourceTenant, MAX_ELEMENT_COUNT);

    Map<String, String> idMapping = new HashMap<>();
    for (SearchDto search : searches) {
      String sourceId = search.getId();
      String targetId = randomUUID().toString();
      search.setId(targetId);
      search.substituteReferences(KIBANA_INDEX_PATTERN, kibanaIndexMapping);
      opendistroKibanaClient.createSavedSearchObjects(targetTenant, search);
      idMapping.put(sourceId, targetId);
    }

    return idMapping;
  }

  public Map<String, String> copyReportDefinition(
      String sourceTenant, String targetTenant, Map<String, String> searchMapping) {

    List<ReportDefinitionDto> reportDefinitions =
        opendistroKibanaClient.listReportDefinitions(sourceTenant);

    Map<String, String> idMapping = new HashMap<>();
    for (ReportDefinitionDto reportDefinition : reportDefinitions) {
      String sourceId = reportDefinition.getId();
      String searchId = reportDefinition.getSearchId();
      reportDefinition.replaceExistingSearchId(searchMapping.get(searchId));
      String targetId =
          opendistroKibanaClient.createReportDefinition(targetTenant, reportDefinition);
      idMapping.put(sourceId, targetId);
    }

    return idMapping;
  }
}
