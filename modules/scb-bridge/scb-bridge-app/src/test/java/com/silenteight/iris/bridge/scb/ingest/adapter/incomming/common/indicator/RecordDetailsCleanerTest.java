/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.indicator;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class RecordDetailsCleanerTest {

  private static final String INPUT = "EBBS~900146362~I~MANJU KUMAR LAKHANI~~~~~~~~~~"
      + "AE~~AE~~IN~~IN~~~~~IND~~~Passport No~Z2442571~Visa No~7014588~Favorite city/c~~Driving "
      + "Lic. No~~Deferred Docs~~1951-04-02~XX~~~~AE~HOUSEWIFE~EDD~~031~Pvt Bnk Ons UAE~"
      + "21~ACTIVE~~2010-07-01~~~~CUSTOMER                      ~N~~~P20~~~~~~~Active CASA~~"
      + "D~2018-12-02 20:43:24";

  private static final String OUTPUT = "EBBS900146362IMANJU KUMAR LAKHANI"
      + "AEAEINININDPassport NoZ2442571Visa No7014588"
      + "Favorite city/cDriving Lic. NoDeferred Docs1951-04-02XXAE"
      + "HOUSEWIFEEDD031Pvt Bnk Ons UAE21ACTIVE2010-07-01"
      + "CUSTOMERNP20Active CASAD2018-12-02 20:43:24";

  @Test
  public void longTextWithSpaceInData() {
    assertThat(RecordDetailsCleaner.clean(INPUT, '~')).isEqualTo(OUTPUT);
  }
}
