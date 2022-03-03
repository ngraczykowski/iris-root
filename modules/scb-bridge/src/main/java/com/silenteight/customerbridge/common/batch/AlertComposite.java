package com.silenteight.customerbridge.common.batch;

import lombok.Value;

import com.silenteight.proto.serp.v1.alert.Alert;

import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;

import javax.annotation.Nonnull;

@Value
public class AlertComposite {

  private static final String FMT_KEY = "fmt";

  String systemId;
  Alert alert;
  int decisionsCount;
  Iterable<Tag> measuringTags;

  static AlertComposite create(RecordToAlertMapper alertMapper, SuspectsCollection suspects) {
    return new AlertComposite(
        alertMapper.toAlert(suspects),
        alertMapper.getDecisionsCollection().size(),
        createMeasuringTags(alertMapper));
  }

  public static AlertComposite create(
      RecordToAlertMapper alertMapper,
      SuspectsCollection suspects,
      int deltaDecisionsCount) {

    return new AlertComposite(
        alertMapper.toAlert(suspects),
        deltaDecisionsCount,
        createMeasuringTags(alertMapper));
  }

  @Nonnull
  private static Iterable<Tag> createMeasuringTags(RecordToAlertMapper alertMapper) {
    return Tags.concat(
        alertMapper.getMeasuringTags(),
        FMT_KEY,
        alertMapper.getAlertData().getFmtName());
  }

  private AlertComposite(Alert alert, int decisionsCount, Iterable<Tag> measuringTags) {
    this.systemId = alert.getId().getSourceId();
    this.alert = alert;
    this.decisionsCount = decisionsCount;
    this.measuringTags = measuringTags;
  }
}
