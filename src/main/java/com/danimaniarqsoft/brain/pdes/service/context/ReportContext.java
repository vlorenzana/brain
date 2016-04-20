package com.danimaniarqsoft.brain.pdes.service.context;

import java.io.File;
import java.util.Date;

import com.danimaniarqsoft.brain.pdes.model.Report;
import com.danimaniarqsoft.brain.util.Constants;
import com.danimaniarqsoft.brain.util.DateUtils;
import com.danimaniarqsoft.brain.util.UrlPd;

/**
 * The ReportContext Class, is used throught the application to share the same context.
 * 
 * @author Daniel Cortes Pichardo
 *
 */
public class ReportContext {
  private Report report;
  private UrlPd  urlPd;
  private File   outputFile;

  public ReportContext() {
    File outputFile = new File(Constants.REPORT_FOLDER + DateUtils.getDateFolderForma(new Date()));
    outputFile.mkdirs();
    this.outputFile = outputFile;
  }

  public Report getReport() {
    return report;
  }

  public void setReport(Report report) {
    this.report = report;
  }

  public UrlPd getUrlPd() {
    return urlPd;
  }

  public void setUrlPd(UrlPd urlPd) {
    this.urlPd = urlPd;
  }

  public File getOutputFile() {
    return outputFile;
  }
}
