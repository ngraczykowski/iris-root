/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.model;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.model.Tag.TagNotFoundException;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class TagTest {

  @Test(expected = TagNotFoundException.class)
  public void invalidAbbreviationTest() {
    Tag.parse("invalid");
  }

  @Test
  public void validAbbreviationTest() {
    Tag tag = Tag.parse("NAM");

    assertThat(tag).isEqualTo(Tag.NAME);
  }
}
