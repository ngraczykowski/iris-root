package com.silenteight.warehouse.common.opendistro.tenant;

import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.common.opendistro.elastic.OpendistroElasticClient;
import com.silenteight.warehouse.common.opendistro.kibana.KibanaIndexPatternDto;
import com.silenteight.warehouse.common.opendistro.kibana.KibanaReportDefinitionDto;
import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaClient;
import com.silenteight.warehouse.common.opendistro.kibana.SearchDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.silenteight.warehouse.common.opendistro.kibana.SavedObjectType.KIBANA_INDEX_PATTERN;
import static java.util.UUID.randomUUID;

@RequiredArgsConstructor
public class TenantService {

  private final OpendistroKibanaClient opendistroKibanaClient;
  private final OpendistroElasticClient opendistroElasticClient;
  private final int maxObjectCount;

  public TenantCloningResult cloneTenant(TenantCloningSpecification tenantCloningSpecification) {
    String targetTenant = tenantCloningSpecification.getTargetTenant();
    String tenantDescription = tenantCloningSpecification.getTenantDescription();
    opendistroElasticClient.createTenant(targetTenant, tenantDescription);

    String sourceTenant = tenantCloningSpecification.getSourceTenant();
    String elasticIndexName = tenantCloningSpecification.getElasticIndexName();
    var indexMapping = copyKibanaIndices(sourceTenant, targetTenant, elasticIndexName);
    var searchMapping = copySearchDefinition(sourceTenant, targetTenant, indexMapping);
    var reportMapping = copyReportDefinition(sourceTenant, targetTenant, searchMapping);

    return TenantCloningResult.builder()
        .reportMapping(reportMapping)
        .build();
  }

  public Map<String, String> copyKibanaIndices(
      String sourceTenant, String targetTenant, String newElasticIndex) {

    List<KibanaIndexPatternDto> kibanaIndexPatterns = opendistroKibanaClient
        .listKibanaIndexPattern(sourceTenant, maxObjectCount);

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
        .listSavedSearchDefinitions(sourceTenant, maxObjectCount);

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

    List<KibanaReportDefinitionDto> kibanaReportDefinitions =
        opendistroKibanaClient.listReportDefinitions(sourceTenant);

    Map<String, String> idMapping = new HashMap<>();
    for (KibanaReportDefinitionDto reportDefinition : kibanaReportDefinitions) {
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
