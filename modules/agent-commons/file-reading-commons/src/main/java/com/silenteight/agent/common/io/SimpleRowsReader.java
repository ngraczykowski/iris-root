package com.silenteight.agent.common.io;

import lombok.RequiredArgsConstructor;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Deprecated
public class SimpleRowsReader implements RowsReader<String> {

  private final Supplier<InputStream> inputStreamSupplier;

  @NotNull
  @Override
  public Iterator<String> iterator() {
    var reader = new BufferedReader(new InputStreamReader(inputStreamSupplier.get()));

    return reader.lines().iterator();
  }
}
