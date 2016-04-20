package com.danimaniarqsoft.brain.util;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIUtils;

import com.danimaniarqsoft.brain.pdes.exceptions.ReportException;

/**
 * UrlContext Class used to Create the Url context for create the reports.
 * 
 * @author Daniel Cortes Pichardo
 *
 */
public class UrlPd {
  private String scheme;
  private String host;
  private String port;
  private String projectName;

  private UrlPd() {

  }

  public static UrlPd createUrl() {
    return new UrlPd();
  }

  public UrlPd withScheme(String scheme) {
    this.scheme = scheme;
    return this;
  }

  public UrlPd withHost(String host) {
    this.host = host;
    return this;
  }

  public UrlPd withPort(String port) {
    this.port = port;
    return this;
  }

  public UrlPd withProjectName(String projectName) {
    this.projectName = projectName;
    return this;
  }

  public URI getWeekReportUrl() throws URISyntaxException {
    return URIUtils.createURI(scheme, host, Integer.parseInt(port),
        projectName + "//reports/week.class", "tl=auto&labelFilterAuto=t&pathFilterAuto=t", null);
  }

  public URI getGeneralReportUrl() throws NumberFormatException, URISyntaxException {
    return URIUtils.createURI(scheme, host, Integer.parseInt(port),
        projectName + "//cms/TSP/indiv_plan_summary", "frame=content", null);
  }

  public URI getOveralMetricsUrl() throws NumberFormatException, URISyntaxException {
    return URIUtils.createURI(scheme, host, Integer.parseInt(port),
        projectName + "//cms/TSP/indiv_plan_summary", "frame=content&section=100", null);
  }

  public URI getReportsPlanSummaryUrl() throws NumberFormatException, URISyntaxException {
    return URIUtils.createURI(scheme, host, Integer.parseInt(port), "reports" + "/form2html.class",
        null, null);
  }

  public URI getWeeklyTasksUrl() throws NumberFormatException, URISyntaxException {
    return URIUtils.createURI(scheme, host, Integer.parseInt(port), projectName+"/+" + "/reports/week.class",
        null, null);
  }

  public URI getEvImageUrl() throws ReportException {
    try {
      return getUrlReport("cumValueChart");
    } catch (NumberFormatException e) {
      throw new ReportException("getEvImageUrl", e);
    }
  }


  public URI getInProgressTaskUrl() throws ReportException {
    try {
      return getUrlReport("tasksInProgressDiscChart");
    } catch (NumberFormatException e) {
      throw new ReportException("getInProgressTaskUrl", e);
    }
  }

  public URI getDirectHoursUrl() throws ReportException {
    try {
      return getUrlReport("cumDirectTimeChart");
    } catch (NumberFormatException e) {
      throw new ReportException("getDirectHoursUrl", e);
    }
  }

  public URI getEarnedValueTrendUrl() throws ReportException {
    try {
      return getUrlReport("valueTrendChart");
    } catch (NumberFormatException e) {
      throw new ReportException("getEarnedValueTrendUrl", e);
    }
  }

  public URI getDirectTimeTrendUrl() throws ReportException {
    try {
      return getUrlReport("timeTrendChart");
    } catch (NumberFormatException e) {
      throw new ReportException("getDirectTimeTrendUrl", e);
    }
  }

  public URI getExternalCommitmentsUrl() throws ReportException {
    try {
      return getUrlReport("commitChart");
    } catch (NumberFormatException e) {
      throw new ReportException("getExternalCommitmentsUrl", e);
    }
  }

  public URI getMilestonesUrl() throws ReportException {
    try {
      return getUrlReport("milestonesChart");
    } catch (NumberFormatException e) {
      throw new ReportException("getMilestonesUrl", e);
    }
  }

  public URI getDefectsUrl() throws ReportException {
    try {
      return URIUtils.createURI(scheme, host, Integer.parseInt(port),
          projectName + "//cms/TSP/indiv_plan_summary", "frame=content&section=200", null);
    } catch (NumberFormatException | URISyntaxException e) {
      throw new ReportException("getDirectTimeTrendUrl", e);
    }
  }

  public URI getImageFromCacheUrl(String reportId) throws ReportException {
    try {
      return URIUtils.createURI(scheme, host, Integer.parseInt(port), projectName + "/" + reportId,
          null, null);
    } catch (NumberFormatException | URISyntaxException e) {
      throw new ReportException("getImageFromCacheUrl", e);
    }
  }

  private URI getUrlReport(String reportName) throws ReportException {
    try {
      return URIUtils.createURI(scheme, host, Integer.parseInt(port),
          projectName + Constants.REPORT_EV_URL, Constants.REPORT_EV_QUERY + reportName, null);
    } catch (NumberFormatException | URISyntaxException e) {
      throw new ReportException("getUrlReport", e);
    }
  }

  public String getPort() {
    return port;
  }

  public void setPort(String port) {
    this.port = port;
  }

  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }
}
