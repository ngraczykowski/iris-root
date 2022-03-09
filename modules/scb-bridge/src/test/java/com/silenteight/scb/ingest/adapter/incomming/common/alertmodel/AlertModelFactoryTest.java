package com.silenteight.scb.ingest.adapter.incomming.common.alertmodel;

import com.silenteight.proto.serp.v1.alert.AlertDescriptor;
import com.silenteight.proto.serp.v1.alert.AlertModel;

import com.google.protobuf.Duration;
import com.google.protobuf.Empty;
import com.google.protobuf.Message;
import com.google.protobuf.Timestamp;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.*;

class AlertModelFactoryTest {

  private List<AlertDescriptor> descriptors;

  @Test
  void emptyAlertModelWhenNoClasses() {
    modelFromClasses();

    assertThat(descriptors).isEmpty();
  }

  @Test
  void createsAlertModelFromClasses() {
    modelFromClasses(Empty.class, Timestamp.class, Duration.class);

    assertThat(descriptors)
        .extracting(AlertDescriptor::getFullName)
        .contains(
            Empty.getDescriptor().getFullName(),
            Timestamp.getDescriptor().getFullName(),
            Duration.getDescriptor().getFullName());
  }

  @NotNull
  private AlertModel modelFromClasses(Class<? extends Message>... classes) {
    AlertModel alertModel = new AlertModelFactory(asList(classes)).get();
    assertThat(alertModel).isNotNull();
    descriptors = alertModel.getAlertDescriptorsList();
    return alertModel;
  }
}