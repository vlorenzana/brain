package com.danimaniarqsoft.brain.dao;

import java.io.IOException;
import java.net.URISyntaxException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.danimaniarqsoft.brain.pdes.model.SizeTable;
import com.danimaniarqsoft.brain.util.UrlPd;

/**
 * ReportDAO, xpath query to the PDES Report
 * 
 * @author Daniel Cortes Pichardo
 *
 */
public class OverallMetricsDAO {
  private Document overallMetrics;

  public OverallMetricsDAO(UrlPd urlPd)
      throws IOException, NumberFormatException, URISyntaxException {
    this.overallMetrics = Jsoup.connect(urlPd.getReportsPlanSummaryUrl().toString())
        .header("Referer", urlPd.getOveralMetricsUrl().toString()).get();
  }

  public SizeTable findSizeTable(String xpathQuery) {
    Elements overallMetricsElements = overallMetrics.select(xpathQuery);
    Element sizeTable = overallMetricsElements.get(1);
    sizeTable.addClass("table table-bordered table-striped table-responsive");
    return new SizeTable(sizeTable.toString());
  }
}
