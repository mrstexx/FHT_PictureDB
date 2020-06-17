package edu.swe2.cs.reporting;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import edu.swe2.cs.util.SystemProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileOutputStream;
import java.io.IOException;

public abstract class AbstractReportHandler {

    protected static final String PDF_EXTENSION = ".pdf";
    protected static final Font TITLE_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    protected static final Font SUBTITLE_BOLD = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
    protected static final Font NORMAL_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
    protected static final Logger LOG = LogManager.getLogger(AbstractReportHandler.class);

    private final String targetPath;
    private IReport report;

    public AbstractReportHandler(String targetPath) {
        this.targetPath = targetPath;
    }

    public boolean createPdfReport(IReport report) {
        this.report = report;
        LOG.info("Creating report for {}", report.getFileName());
        try {
            generateReport();
        } catch (DocumentException | IOException e) {
            LOG.error("An error occurred while generating report, {0}", e);
            return false;
        }
        return true;
    }

    private void addMetaData(Document document) {
        document.setPageSize(PageSize.A4);
        document.addTitle("PicDB - " + report.getFileName());
        document.addSubject("PictDB - " + report.getFileName());
        document.addKeywords("PictureDB, Pdf, Report");
        document.addAuthor("picture-db.io");
        document.addCreator("picture-db.io");
    }

    protected abstract void generateReport() throws DocumentException, IOException;

    protected Document getDocument() throws IOException, DocumentException {
        Document pdfDoc = new Document();
        PdfWriter.getInstance(pdfDoc, new FileOutputStream(getFileName()));
        // open document
        pdfDoc.open();
        // add all required content
        addMetaData(pdfDoc);
        // return prepared document
        return pdfDoc;
    }

    protected void closeDocument(Document pdfDoc) {
        pdfDoc.close();
    }

    private String getFileName() {
        return this.targetPath + SystemProperties.FILE_SEPARATOR + this.report.getFileName() + PDF_EXTENSION;
    }

    protected IReport getReport() {
        return report;
    }

    protected static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
