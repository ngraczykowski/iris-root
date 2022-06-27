/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.configserver.dynamic;

import java.util.Map;

public interface IrisDynamicPropertiesGenerator {

  Map<String, Object> generate(String application, String profile, String label);
}
