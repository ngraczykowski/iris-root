package com.silenteight.hsbc.bridge.file;

import lombok.Value;

@Value(staticConstructor = "of")
public class ResourceIdentifier {

  String uri;
}
