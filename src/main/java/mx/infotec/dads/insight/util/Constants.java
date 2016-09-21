package mx.infotec.dads.insight.util;

/**
 * Clase de constantes utilizadas en el proyecto
 * 
 * @author Daniel Cortes Pichardo
 *
 */
public class Constants {
    
    public static final String PDES__REPORTER = "PDES Reporter";    
    public static final String WIZARD = "/fxml/wizard.fxml";
    public static final String ICON = "images/monkey.png";
    public static final int TIMEOUT = 10*1000;
    public static final String FILTER_FINISHED = "terminado";
    public static final String FILE_PDES_PROPERTIES = "./pdes.properties";
    public static final String PDES_CLIENT_HOST_NAME = "localhost";
    public static final String PDES_SCHEME = "http";

    public static final String FILE_WEEKDATA_TXT = "./data.txt";
    // Properties name's
    public static final String PROPERTY_PORT = "port";
    public static final String PROPERTY_PROJECT = "project";
    public static final String PROPERTY_MEMBER_NAME = "memberName";

    public static final String FILE_ERROR_TXT = "./error.txt";
    public static final String ATTR_SRC = "src";
    public static final String EXTENSION_PNG = "png";
    public static final String REPORT_FOLDER = "weekReport";
    public static final String REPORT_IMG_FOLDER = "reportImages";
    public static final String REPORT_EV_URL = "//reports/ev.class";
    public static final String REPORT_FILTER_URL = "//team/setup/selectLabelFilter";
    public static final String REPORT_FILTERBYWBS_URL = "//team/setup/selectWBSIndiv";
    
    public static final String REPORT_INDIV_PLAN_SUMMARY = "//cms/TSP/indiv_plan_summary";
    
    public static final String REPORT_TABLE = "//reports/table.class";
    public static final String REPORT_EV_QUERY = "tl=auto&labelFilterAuto=t&pathFilterAuto=t&charts&showChart=pdash.ev.";
    public static final String OUTPUT_FORMAT = "%-40s : %-20s ";
    public static final String BODY_EVCHARITEM_IMG = "body .evChartItem img";

    public static final String PDES_EN_DATE_FORMAT_PATTERN = "MM/dd/yy";
    public static final String PDES_ES_DATE_FORMAT_PATTERN = "dd/MM/yy";
    public static final String PDES_FOLDER_DATE_FORMAT = "yyyyMMdd";

    // Type of report
    public static final int FIST_SELECTION = 0;
    public static final int SECOND_SELECTION = 1;

    
    public static final String PAGE_PLANNING = "index.html";
    public static final String PAGE_TASK_PRODUCTS = "task_products.html";
    public static final String PAGE_QUALITY = "quality.html";
    public static final String PAGE_ROLES = "roles.html";
    
    public static final String PLAN_DE_ACCION = "Plan de acci�n";
    
    private Constants() {

    }
}
