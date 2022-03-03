package com.silenteight.customerbridge.common.hitdetails.builder;

import lombok.NonNull;

import com.silenteight.customerbridge.common.hitdetails.element.ElementCollectorEventListener;
import com.silenteight.customerbridge.common.hitdetails.model.Attribute;
import com.silenteight.customerbridge.common.hitdetails.model.HitDetails;
import com.silenteight.customerbridge.common.hitdetails.model.Suspect;
import com.silenteight.customerbridge.common.hitdetails.model.Synonym;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HitDetailsBuilder implements ElementCollectorEventListener {

  private static final Pattern SUSPECT_DETECTED_PATTERN = Pattern
      .compile("^Suspect detected #(?<index>[0-9]+)$");

  private final HitDetails hitDetails = new HitDetails();
  private final HitDetailsBuilderConfig config;

  private String lastAttributeKey;
  private Suspect tempSuspect;

  public HitDetailsBuilder(@NonNull HitDetailsBuilderConfig config) {
    this.config = config;
  }

  @Override
  public void onDivider() {
    if (tempSuspect != null) {
      hitDetails.getSuspects().add(tempSuspect);
      tempSuspect = null;
    }
  }

  @Override
  public void onAttribute(@NonNull Attribute attribute) {
    if (tempSuspect == null)
      consumeHitDetailsAttribute(attribute);
    else
      consumeSuspectAttribute(attribute);
    lastAttributeKey = attribute.getKey();
  }

  @Override
  public void onSynonym(@NonNull Synonym synonym) {
    if (lastAttributeKey != null && tempSuspect != null) {
      consumeSuspectSynonym(synonym);
    }
  }

  @Override
  public void onText(@NonNull String text) {
    findPattern(text);
  }

  private void findPattern(String text) {
    if (!findNewSuspect(text)) {
      findHasSndRcvInFlag(text);
    }
  }

  private boolean findNewSuspect(@NonNull String text) {
    Matcher m = SUSPECT_DETECTED_PATTERN.matcher(text);
    if (m.matches()) {
      tempSuspect = new Suspect();
      config.getSuspectAttributeConsumerRepo()
          .find("index")
          .ifPresent(c -> c.accept(tempSuspect, m.group("index")));
      return true;
    }
    return false;
  }

  private void findHasSndRcvInFlag(@NonNull String text) {
    if (text.equals("HasSndRcvIn")) {
      config.getHitDetailsAttributeConsumerRepo()
          .find("HasSndRcvIn")
          .ifPresent(c -> c.accept(hitDetails, text));
    }
  }

  private void consumeSuspectSynonym(Synonym synonym) {
    config.getSuspectSynonymConsumerRepo()
        .find(lastAttributeKey)
        .ifPresent(c -> c.accept(tempSuspect, synonym));
  }

  private void consumeHitDetailsAttribute(Attribute attribute) {
    config.getHitDetailsAttributeConsumerRepo()
        .find(attribute.getKey())
        .ifPresent(c -> c.accept(hitDetails, attribute.getValue()));
  }

  private void consumeSuspectAttribute(Attribute attribute) {
    config.getSuspectAttributeConsumerRepo()
        .find(attribute.getKey())
        .ifPresent(c -> c.accept(tempSuspect, attribute.getValue()));
  }

  public HitDetails build() {
    return hitDetails;
  }
}
