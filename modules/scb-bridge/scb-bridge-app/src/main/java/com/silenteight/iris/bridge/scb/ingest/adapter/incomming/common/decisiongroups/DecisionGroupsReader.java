/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.decisiongroups;

import java.util.Collection;

interface DecisionGroupsReader {

  Collection<String> readAll();
}
