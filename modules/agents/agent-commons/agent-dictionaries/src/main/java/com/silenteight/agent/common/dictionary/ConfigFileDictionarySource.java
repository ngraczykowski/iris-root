package com.silenteight.agent.common.dictionary;

import static com.silenteight.agent.configloader.ConfigsPathFinder.findFile;

class ConfigFileDictionarySource extends FileDictionarySource {

  ConfigFileDictionarySource(String path) {
    super(findFile(path));
  }
}
