/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.model.HitDetails;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.request.GnsRtHit;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class HitDetailsParserTest {

  private final HitDetailsParser underTest = new HitDetailsParser();

  @Test
  void givenHitDetails_expectAccurateNumberOfSuspects() {
    var hitDetailsText = ResourceUtil.readTextFromResource(
        "com/silenteight/iris/bridge/scb/ingest/adapter/incomming/common/hitdetails/test_hit_details.txt");

    HitDetails hitDetails = underTest.parse(hitDetailsText);
    assertThat(hitDetails.getSuspects()).hasSize(13);
  }

  @Test
  void givenHitDetailsFromGnsRt_expectAccurateNumberOfSuspects() {
    // given
    var hitDetailsText1 = ResourceUtil.readTextFromResource(
        "com/silenteight/iris/bridge/scb/ingest/adapter/incomming/common/hitdetails/gns_rt_hit_details_1.txt");
    var hitDetailsText2 = ResourceUtil.readTextFromResource(
        "com/silenteight/iris/bridge/scb/ingest/adapter/incomming/common/hitdetails/gns_rt_hit_details_2.txt");
    var hitDetailsTexts = List.of(
        createGnsRtHit(hitDetailsText1),
        createGnsRtHit(hitDetailsText2)
    );

    var hitDetails = underTest.parseHitDetailsFromGnsRt(hitDetailsTexts);
    assertThat(hitDetails.extractUniqueSuspects()).hasSize(1);
  }

  private static GnsRtHit createGnsRtHit(String text) {
    var gnsRtHit = new GnsRtHit();
    gnsRtHit.setHitDetails(text);
    return gnsRtHit;
  }

  @Test
  void givenNullHitDetails_throwsNPE() {
    ThrowingCallable when = () -> underTest.parse(null);

    assertThatThrownBy(when).isInstanceOf(NullPointerException.class);
  }
}
