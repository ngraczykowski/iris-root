package com.silenteight.agent.common.io

import spock.lang.Specification
import spock.lang.Unroll

class ResourceDirectoryFilenamesFinderSpec extends Specification {

  @Unroll
  def "validate extracting nested jar file name"() {

    expect:
    ResourceDirectoryFilenamesFinder.getNestedJarPath(uri) == expectedResult

    where:
    uri                                                                                   |
        expectedResult
    "/resources/"                                                                         |
        Optional.empty()
    "test.jar!/resources/"                                                                |
        Optional.empty()
    "outer.jar!/inner.jar!/resources/"                                                    |
        Optional.of("/inner.jar")
    "jar:file:/name-agent-exec.jar!/BOOT-INF/lib/agent-resources.jar!/nltk/corpora/words" |
        Optional.of("/BOOT-INF/lib/agent-resources.jar")

  }
}
