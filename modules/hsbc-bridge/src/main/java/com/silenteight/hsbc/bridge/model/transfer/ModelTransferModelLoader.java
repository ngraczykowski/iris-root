package com.silenteight.hsbc.bridge.model.transfer;

import com.silenteight.hsbc.bridge.watchlist.LoadingException;

import java.io.InputStream;
import java.net.URI;

public interface ModelTransferModelLoader {

  InputStream loadModel(URI uri) throws LoadingException;
}
