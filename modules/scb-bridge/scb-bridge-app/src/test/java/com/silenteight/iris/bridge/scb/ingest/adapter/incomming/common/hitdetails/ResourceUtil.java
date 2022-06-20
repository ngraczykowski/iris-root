/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;

public class ResourceUtil {

  public static String readTextFromResource(String resource) {
    try (InputStream fis = ResourceUtil.class.getClassLoader().getResourceAsStream(resource)) {
      return IOUtils.toString(fis, Charset.forName("UTF-8"));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static String[] readLinesFromResource(String resource) {
    try (InputStream fis = ResourceUtil.class.getClassLoader().getResourceAsStream(resource)) {
      return IOUtils.readLines(fis, Charset.forName("UTF-8"))
          .toArray(new String[0]);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
