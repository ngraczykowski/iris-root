package com.silenteight.serp.common.testing;

import lombok.Getter;

import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public class SimpleObserver<V> implements StreamObserver<V> {

  private int onCompletedCount = 0;
  private int onErrorCount = 0;
  private List<V> values = new ArrayList<>();

  public Optional<V> getValue() {
    checkState();

    if (values.isEmpty())
      return Optional.empty();
    else if (values.size() == 1)
      // FIXME: what if the first value is null?
      return Optional.ofNullable(values.get(0));
    else
      throw new IllegalStateException("getValue() expects only one value, actual size is " +
                                          values.size());
  }

  public List<V> getValues() {
    checkState();

    return values;
  }

  private void checkState() {
    if (onCompletedCount == 0)
      throw new IllegalStateException("onCompletedCount should be 1, not 0");

    if (onErrorCount > 0)
      throw new IllegalStateException("onErrorCount is " + onErrorCount);
  }

  @Override
  public void onNext(V value) {
    values.add(value);
  }

  @Override
  public void onError(Throwable t) {
    onErrorCount++;
  }

  @Override
  public void onCompleted() {
    onCompletedCount++;
  }

  public boolean isCompleted() {
    return onCompletedCount > 0;
  }
}
