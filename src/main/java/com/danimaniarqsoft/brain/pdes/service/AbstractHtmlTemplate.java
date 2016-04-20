package com.danimaniarqsoft.brain.pdes.service;

import com.danimaniarqsoft.brain.pdes.exceptions.ReportException;
import com.danimaniarqsoft.brain.pdes.service.context.ReportContext;

public abstract class AbstractHtmlTemplate {

  protected abstract void createIndexFile(ReportContext context) throws ReportException;

  protected abstract void createDefectFile(ReportContext context) throws ReportException;

  protected abstract void createExternalCommitmentsFile(ReportContext context)
      throws ReportException;

  protected abstract void createSizeFile(ReportContext context) throws ReportException;

  protected abstract void createHoursFile(ReportContext context) throws ReportException;

  protected abstract void createTaskProgressFile(ReportContext context) throws ReportException;

  protected abstract void createVgFile(ReportContext context) throws ReportException;

  protected abstract void createWeekResumeFile(ReportContext context) throws ReportException;

  protected abstract void createSupportFile(ReportContext context) throws ReportException;

  protected abstract void createPerformanceFile(ReportContext context) throws ReportException;

  protected abstract void createMilestonesFile(ReportContext context) throws ReportException;

  protected abstract void createWebSite(ReportContext context) throws ReportException;



  public void saveHtmlReport(ReportContext context) throws ReportException {
    createIndexFile(context);
    createWeekResumeFile(context);
    createPerformanceFile(context);
    createSizeFile(context);
    createVgFile(context);
    createHoursFile(context);
    createExternalCommitmentsFile(context);
    createMilestonesFile(context);
    createTaskProgressFile(context);
    createTaskProgressFile(context);
    createDefectFile(context);
    createSupportFile(context);
    createWebSite(context);
  }

}
