package com.silenteight.payments.bridge.etl.processing.model.firco;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;

import java.util.List;
import javax.annotation.Nonnull;

@Value
@Builder
public class FircoAlert {

  /**
   * Adjudication Engine registered alert name.
   */
  String alertName;

  FircoAlertFields fields;

  @Singular
  List<FircoMatch> matches;

  @Nonnull
  public MessageData getMessageData() {
    return fields.getMessageData();
  }
}
