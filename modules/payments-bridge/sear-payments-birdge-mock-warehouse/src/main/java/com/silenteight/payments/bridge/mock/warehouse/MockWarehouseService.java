package com.silenteight.payments.bridge.mock.warehouse;

import com.silenteight.data.api.v2.ProductionDataIndexRequest;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Profile("mockwarehouse")
public class MockWarehouseService {

  private final Set<ProductionDataIndexRequest> indexedAlerts = new HashSet<>();

  public void handle(ProductionDataIndexRequest request) {
    indexedAlerts.add(request);
  }

  public boolean containsIndexedDiscriminator(String discriminator) {
    return indexedAlerts
        .stream()
        .anyMatch(
            ia -> containsDiscriminator(ia, discriminator)
        );
  }

  private static boolean containsDiscriminator(
      ProductionDataIndexRequest request, String discriminator) {
    return request.getAlertsList()
        .stream()
        .anyMatch(a -> a.getDiscriminator().equals(discriminator));
  }
}
