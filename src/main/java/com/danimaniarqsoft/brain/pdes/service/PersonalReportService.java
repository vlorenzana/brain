package com.danimaniarqsoft.brain.pdes.service;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.danimaniarqsoft.brain.pdes.exceptions.ReportException;
import com.danimaniarqsoft.brain.pdes.model.Report;
import com.danimaniarqsoft.brain.pdes.service.context.ReportContext;
import com.danimaniarqsoft.brain.util.Constants;
import com.danimaniarqsoft.brain.util.ContextUtil;

/**
 * 
 * @author Daniel Cortes Pichardo
 *
 */
public class PersonalReportService extends AbstractReportTemplate {

	private static PersonalReportService instance = null;

	private PersonalReportService() {

	}

	public static synchronized PersonalReportService getInstance() {
		if (instance == null) 
                    instance = new PersonalReportService();				
		return instance;
	}

	@Override
	protected void createWeekReport(ReportContext context) throws ReportException {
		try {
			Report report = WeekReportService.createReport(context.getUrlPd());
			context.setReport(report);
			HtmlTemplateService.getInstance().saveHtmlReport(context);
			ContextUtil.saveJsonFormat(context);
		} catch (NumberFormatException | ReportException e) {
			throw new ReportException("createWeekReport", e);
		}
	}

	@Override
	protected void locateEvImage(ReportContext context) throws ReportException {
		locateResource(context.getUrlPd().getEvImageUrl().toString(), "body div div div img", "ev", context);
	}

	@Override
	protected void locateInProgressTaskImage(ReportContext context) throws ReportException {
		locateResource(context.getUrlPd().getInProgressTaskUrl().toString(), Constants.BODY_EVCHARITEM_IMG,
				"inPogressTasks", context);
	}

	@Override
	protected void locateCumDirectTimeImage(ReportContext context) throws ReportException {
		locateResource(context.getUrlPd().getDirectHoursUrl().toString(), Constants.BODY_EVCHARITEM_IMG, "directHours",
				context);
	}

	@Override
	protected void locateEvTrendImage(ReportContext context) throws ReportException {
		locateResource(context.getUrlPd().getEarnedValueTrendUrl().toString(), Constants.BODY_EVCHARITEM_IMG, "evTrend",
				context);
	}

	@Override
	protected void locateDirectTimeTrendImage(ReportContext context) throws ReportException {
		locateResource(context.getUrlPd().getDirectTimeTrendUrl().toString(), Constants.BODY_EVCHARITEM_IMG,
				"directTimeTrend", context);
	}

	@Override
	protected void locateDefectImages(ReportContext context) throws ReportException {
		locateResource(context.getUrlPd().getDefectsUrl().toString(), "body p img", "defects", context);
	}

	@Override
	protected void locateExternalCommitmentsImage(ReportContext context) throws ReportException {
		locateResource(context.getUrlPd().getExternalCommitmentsUrl().toString(), Constants.BODY_EVCHARITEM_IMG,
				"externalCommitments", context);
	}

	@Override
	protected void locateMilestonesImage(ReportContext context) throws ReportException {
		locateResource(context.getUrlPd().getMilestonesUrl().toString(), Constants.BODY_EVCHARITEM_IMG, "milestones",
				context);
	}

	private void locateResource(final String contextUrl, final String xPathQuery, final String fileName,
			ReportContext context) throws ReportException {
		try {
			Document doc = Jsoup.connect(contextUrl).get();
			Elements elements = doc.select(xPathQuery);
			BufferedImage image;
			int count = 0;
			for (Element element : elements) {
				image = ImageIO.read(context.getUrlPd().getImageFromCacheUrl(element.attr(Constants.ATTR_SRC)).toURL());
				ContextUtil.saveImageToDisk(image, context.getOutputFile(), fileName + blankIfZero(count++));
			}
		} catch (IOException | NumberFormatException | ReportException e) {
			throw new ReportException("locateResource", e);
		}
	}

	private static String blankIfZero(int val) {
		if (val == 0) {
			return "";
		} else {
			return Integer.toString(val);
		}
	}
}
