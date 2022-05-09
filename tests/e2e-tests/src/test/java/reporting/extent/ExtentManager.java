package reporting.extent;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Protocol;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.reporter.configuration.ViewName;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentManager {
  private static ExtentReports extent;

  public static ExtentReports getInstance() {
    return extent;
  }

  public static void createInstance(String fileName) {
    ExtentSparkReporter htmlReporter = new ExtentSparkReporter(fileName);
    htmlReporter.config().setTheme(Theme.DARK);
    htmlReporter.config().setEncoding("UTF-8");
    htmlReporter.config().setProtocol(Protocol.HTTPS);
    htmlReporter.config().setDocumentTitle("Karate Extent Report");
    htmlReporter.config().setReportName("Karate Auto Reporter");
    htmlReporter.config().enableOfflineMode(true);
    htmlReporter
        .viewConfigurer()
        .viewOrder()
        .as(
            new ViewName[] {
              ViewName.DASHBOARD,
              ViewName.TEST,
              ViewName.AUTHOR,
              ViewName.DEVICE,
              ViewName.EXCEPTION,
              ViewName.LOG
            })
        .apply();

    htmlReporter.config().setTimeStampFormat("MM/dd/yyyy hh:mm:ss a");
    extent = new ExtentReports();
    extent.attachReporter(htmlReporter);
  }

  public static void createReport() {
    if (ExtentManager.getInstance() == null) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
      String formattedDate = dateFormat.format(new Date());

      ExtentManager.createInstance("results/" + "Extent_Report" + formattedDate + ".html");
    }
  }
}
