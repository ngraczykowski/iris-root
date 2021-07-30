package com.silenteight.warehouse.configurationmanagement.loader;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.common.opendistro.elastic.OpendistroElasticClient;
import com.silenteight.warehouse.common.opendistro.kibana.OpendistroReportLoader;
import com.silenteight.warehouse.common.opendistro.kibana.OpendistroSavedObjectsLoader;
import com.silenteight.warehouse.internal.v1.ImportConfigurationRequest;

import com.google.common.io.BaseEncoding;

import java.io.ByteArrayInputStream;

@RequiredArgsConstructor
public class LoadOpendistroConfigurationUseCase {

  private final OpendistroElasticClient opendistroElasticClient;
  private final OpendistroSavedObjectsLoader opendistroSavedObjectsLoader;
  private final OpendistroReportLoader opendistroReportLoader;

  public void load(ImportConfigurationRequest request) {
    opendistroElasticClient.createTenant(request.getTenant(), "");

    ByteArrayInputStream savedObjectsPayload = fromBase64String(request.getSavedObjects());
    opendistroSavedObjectsLoader.loadAll(request.getTenant(), savedObjectsPayload);

    ByteArrayInputStream reportInstancePayload = fromBase64String(request.getReportInstances());
    opendistroReportLoader.loadAll(request.getTenant(), reportInstancePayload);
  }

  public static ByteArrayInputStream fromBase64String(@NonNull String string) {
    byte[] decodedBytes = BaseEncoding.base64().decode(string);
    return new ByteArrayInputStream(decodedBytes);
  }
}
