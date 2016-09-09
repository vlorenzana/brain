package mx.infotec.dads.insight.pdes.service;

import mx.infotec.dads.insight.pdes.exceptions.ReportException;
import mx.infotec.dads.insight.pdes.service.context.ReportContext;

/**
 * Clase abstracta que define los métodos que deberan de ser implementados por
 * un manejador de templates HTML
 * 
 * @author Daniel Cortes Pichardo
 *
 */
public abstract class AbstractHtmlTemplate {

    protected abstract void createIndexFile(ReportContext context) throws ReportException;

    protected abstract void createRoleFile(ReportContext context) throws ReportException;

    protected abstract void createExternalCommitmentsFile(ReportContext context) throws ReportException;

    protected abstract void createQualityFile(ReportContext context) throws ReportException;

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
	createQualityFile(context);
	createVgFile(context);
	createHoursFile(context);
	createExternalCommitmentsFile(context);
	createMilestonesFile(context);
	createTaskProgressFile(context);
	createTaskProgressFile(context);
	createRoleFile(context);
	createSupportFile(context);
	createWebSite(context);
    }

}
