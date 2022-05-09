package features;

import com.intuit.karate.junit5.Karate;
import utils.KarateHooks;

class AllTestsRunner {

  @Karate.Test
  Karate testAll() {
    return Karate.run().hook(new KarateHooks()).relativeTo(getClass());
  }

  //    In case You need to run specific scenario
  @Karate.Test
  Karate testSpecificTC() {
    return Karate.run()
        .scenarioName("QA-T1 AI Reasoning report generated for batch with 5 alerts contains 5 rows")
        .hook(new KarateHooks())
        .relativeTo(getClass());
  }
}
