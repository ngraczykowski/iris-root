package com.silenteight.hsbc.bridge.unpacker;

import lombok.Value;

import java.io.InputStream;

@Value
public class UnzippedObject {

  InputStream inputStream;
  String name;
}
