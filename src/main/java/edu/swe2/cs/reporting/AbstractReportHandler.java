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

    /**
     * Create report for defined report type
     *
     * @param report Report to be created
     * @return True if report was successfully created, otherwise false.
     */
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

    /**
     * Generate report. After document content has been added to the document it HAS to be closed.
     *
     * <code>
     * Document document = getDocument();
     * addTitle(document);
     * addImage(document);
     * addTableWithTags(document);
     * closeDocument(document);
     * </code>
     *
     * @throws DocumentException An error occurred while generating report
     * @throws IOException       If file name cannot be found for the export
     */
    protected abstract void generateReport() throws DocumentException, IOException;

    /**
     * Create base document structure including meta data.
     *
     * @return Created document which is ready to be modified.
     * @throws IOException       If file name cannot be found for the export
     * @throws DocumentException An error occurred while creating document
     */
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

    /**
     * @param pdfDoc Document to be closed. NOTE: It must be called after document has been generated.
     */
    protected void closeDocument(Document pdfDoc) {
        pdfDoc.close();
    }

    private String getFileName() {
        return this.targetPath + SystemProperties.FILE_SEPARATOR + this.report.getFileName() + PDF_EXTENSION;
    }

    /**
     * @return report to be created
     */
    protected IReport getReport() {
        return report;
    }

    /**
     * Add empty line to document paragraph
     *
     * @param paragraph Paragraph to be added lines
     * @param number    Number of empty lines to add to paragraph
     */
    protected static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
