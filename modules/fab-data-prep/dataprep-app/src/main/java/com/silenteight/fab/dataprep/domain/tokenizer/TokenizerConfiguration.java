package com.silenteight.fab.dataprep.domain.tokenizer;

import com.fasterxml.jackson.dataformat.csv.CsvSchema;

interface TokenizerConfiguration {

  char SEPARATOR = ';';

  CsvSchema getConfiguration();
}
