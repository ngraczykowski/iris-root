package com.silenteight.warehouse.report.tenant;

import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaClient;
import com.silenteight.warehouse.common.opendistro.kibana.SavedObject;
import com.silenteight.warehouse.common.opendistro.kibana.dto.SavedObjectDto;
import com.silenteight.warehouse.common.opendistro.kibana.dto.SavedObjectDto.SavedObjectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.silenteight.warehouse.common.opendistro.kibana.SavedObjectType.KIBANA_INDEX_PATTERN;
import static java.util.UUID.randomUUID;

@RequiredArgsConstructor
public class TenantService {

  private static final Integer MAX_ELEMENT_COUNT = 100;
  private final OpendistroKibanaClient opendistroKibanaClient;

  public Map<String, String> copyKibanaIndices(
      String sourceTenant, String targetTenant, String elasticIndex) {

    List<SavedObjectDto> sourceObjects = opendistroKibanaClient.listSavedObjects(
        sourceTenant, KIBANA_INDEX_PATTERN, MAX_ELEMENT_COUNT);

    Map<String, String> idMapping = new HashMap<>();
    for (SavedObjectDto sourceObject : sourceObjects) {
      SavedObject targetObject = copyAttributesWithUpdatedElasticIndex(sourceObject, elasticIndex);
      String targetId = randomUUID().toString();
      opendistroKibanaClient.createSavedObjects(
          targetTenant, KIBANA_INDEX_PATTERN, targetId, targetObject);
      idMapping.put(sourceObject.getId(), targetId);
    }

    return idMapping;
  }

  private static SavedObject copyAttributesWithUpdatedElasticIndex(
      SavedObjectDto sourceObject, String elasticIndex) {

    SavedObjectAttributes attributes = sourceObject.getAttributes();
    attributes.setElasticIndex(elasticIndex);

    return SavedObject.builder()
        .attributes(attributes)
        .build();
  }
}
