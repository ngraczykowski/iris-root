package com.silenteight.hsbc.bridge.file;

import lombok.Value;

@Value
public class ResourceIdentifier {

  String uri;

  public ResourceIdentifier(String uri) {
    this.uri = uri;
  }
}
