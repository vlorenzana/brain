package com.danimaniarqsoft.brain.pdes.model;

import java.util.Date;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.danimaniarqsoft.brain.util.DateUtils;

public class InfoReportTable {

  private String dateReport;
  private String reportedPeriod;
  private String dateEndPlanned;
  private String status;
  private String dateForecast;

  public InfoReportTable(Document doc) {
    Elements elements = doc.select("body table[name=STATS]");
    extractInfo(elements);
  }

  private void extractInfo(Elements elements) {
    this.dateReport = DateUtils.convertDateToString(new Date());
    Element dateEndPlanned = elements.get(0).getAllElements().get(5);
    this.setDateEndPlanned(dateEndPlanned.text());
    this.setDateForecast(readForeCast(elements));
  }

  private static String readForeCast(Elements elements) {
    try {
      Element dateForecast = elements.get(0).getAllElements().get(278);
      return dateForecast.text();
    } catch (Exception e) {
      return "No hay valores para estimar";
    }
  }

  public String getDateReport() {
    return dateReport;
  }

  public void setDateReport(String dateReport) {
    this.dateReport = dateReport;
  }

  public String getReportedPeriod() {
    return reportedPeriod;
  }

  public void setReportedPeriod(String reportedPeriod) {
    this.reportedPeriod = reportedPeriod;
  }

  public String getDateEndPlanned() {
    return dateEndPlanned;
  }

  public void setDateEndPlanned(String dateEndPlanned) {
    this.dateEndPlanned = dateEndPlanned;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getDateForecast() {
    return dateForecast;
  }

  public void setDateForecast(String dateForecast) {
    this.dateForecast = dateForecast;
  }



}
