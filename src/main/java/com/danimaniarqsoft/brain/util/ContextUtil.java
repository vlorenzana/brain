package com.danimaniarqsoft.brain.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.danimaniarqsoft.brain.pdes.exceptions.ReportException;
import com.danimaniarqsoft.brain.pdes.service.context.ReportContext;
import com.google.gson.Gson;

public class ContextUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(ContextUtil.class);

  private ContextUtil() {

  }

  public static void saveToDisk(StringBuilder sb, String dataFile, File outputFile) {
    if (!outputFile.exists()) {
      outputFile.mkdirs();
    }
    FileOutputStream fop = null;
    File file;
    String content = sb.toString();

    try {

      file = new File(outputFile, dataFile);
      fop = new FileOutputStream(file);
      if (!file.exists()) {
        file.createNewFile();
      }

      byte[] contentInBytes = content.getBytes();

      fop.write(contentInBytes);
      fop.flush();
      fop.close();

    } catch (IOException e) {
      LOGGER.error("saveToDisk", e);
    } finally {
      try {
        if (fop != null) {
          fop.close();
        }
      } catch (IOException e) {
        LOGGER.error("saveToDisk", e);
      }
    }
  }

  public static void saveImageToDisk(BufferedImage image, File outputFile, String fileName)
      throws IOException {
    File imageFolder = new File(outputFile, Constants.REPORT_IMG_FOLDER);
    if (!imageFolder.exists()) {
      imageFolder.mkdirs();
    }
    File imageFile = new File(imageFolder, fileName + "." + Constants.EXTENSION_PNG);
    ImageIO.write(image, Constants.EXTENSION_PNG, imageFile);
  }

  public static void saveExceptionToDisk(Throwable e, String fileName, File outputFile) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    e.printStackTrace(pw);
    saveToDisk(new StringBuilder("Please send this log to: daniel.cortes@infotec.mx").append("\n\n")
        .append(sw.toString()), fileName, outputFile);
  }

  /**
   * Export a resource embedded into a Jar file to the local file path.
   *
   * @param resourceName ie.: "/SmartLibrary.dll"
   * @return The path to the exported resource
   * @throws Exception
   */
  static public String exportResource(String resourceName) throws ReportException {
    InputStream stream = null;
    OutputStream resStreamOut = null;
    String jarFolder;
    try {
      stream = ContextUtil.class.getResourceAsStream(resourceName);// note that each / is a
                                                                   // directory down in the "jar
                                                                   // tree" been the jar the root of
                                                                   // the tree
      if (stream == null) {
        throw new Exception("Cannot get resource \"" + resourceName + "\" from Jar file.");
      }
      int readBytes;
      byte[] buffer = new byte[4096];
      jarFolder = new File(
          ContextUtil.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath())
              .getParentFile().getPath().replace('\\', '/');
      resStreamOut = new FileOutputStream(jarFolder + resourceName);
      while ((readBytes = stream.read(buffer)) > 0) {
        resStreamOut.write(buffer, 0, readBytes);
      }
    } catch (Exception ex) {
      throw new ReportException("exportResource", ex);
    } finally {
      try {

        stream.close();
        resStreamOut.close();

      } catch (Exception e) {
        // TODO: handle exception
      }
    }

    return jarFolder + resourceName;
  }

  public static String computeStatus(String vgDiffs) {
    int vgDiff = new Double(vgDiffs).intValue();
    if (vgDiff == 0) {
      return "En Tiempo";
    } else if (vgDiff < 0) {
      return "Atrasado";
    } else {
      return "Adelantado";
    }
  }

  public static void saveJsonFormat(ReportContext context) {
    try {
      Gson gson = new Gson();
      String json = gson.toJson(context);
      saveToDisk(new StringBuilder(json), "data.brain", context.getOutputFile());
    } catch (Exception e) {
      LOGGER.error("JSON - ERROR: ", e);
    }
  }
}
