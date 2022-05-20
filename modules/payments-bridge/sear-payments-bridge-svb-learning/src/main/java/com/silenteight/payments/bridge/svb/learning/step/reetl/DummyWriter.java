package com.silenteight.payments.bridge.svb.learning.step.reetl;

import org.springframework.batch.item.ItemWriter;

import java.util.List;

class DummyWriter implements ItemWriter<Void> {

  @Override
  public void write(List<? extends Void> items) throws Exception {

  }
}
