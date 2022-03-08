package com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model;

import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.Tag.TagNotFoundException;

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