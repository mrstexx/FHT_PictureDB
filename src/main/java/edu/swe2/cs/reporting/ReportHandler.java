package edu.swe2.cs.reporting;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import edu.swe2.cs.model.Iptc;
import edu.swe2.cs.model.Photographer;
import edu.swe2.cs.model.Picture;
import edu.swe2.cs.util.SystemProperties;
import edu.swe2.cs.util.URLBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportHandler {

    private static final String PDF_EXTENSION = ".pdf";
    private static final Font TITLE_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static final Font SUBTITLE_BOLD = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
    private static final Logger LOG = LogManager.getLogger(ReportHandler.class);

    private final String targetPath;
    private Picture picture;

    public ReportHandler(String targetPath) {
        this.targetPath = targetPath;
    }

    public boolean createPdfReport(Picture picture) {
        this.picture = picture;
        try {
            generateReport();
        } catch (DocumentException | IOException e) {
            LOG.error("An error occurred while generating report, {0}", e);
            return false;
        }
        return true;
    }

    private void generateReport() throws IOException, DocumentException {
        Document pdfDoc = new Document();
        PdfWriter.getInstance(pdfDoc, new FileOutputStream(getFileName()));
        // open document
        pdfDoc.open();
        // add all required content
        addMetaData(pdfDoc);
        addTitle(pdfDoc);
        addImage(pdfDoc);
        addTableWithTags(pdfDoc);
        // close document
        pdfDoc.close();
    }

    private void addMetaData(Document document) {
        document.setPageSize(PageSize.A4);
        document.addTitle("PicDB - " + picture.getFileName());
        document.addSubject("PictDB - " + picture.getFileName());
        document.addKeywords("PictureDB, Pdf, Report");
        document.addAuthor("picture-db.io");
        document.addCreator("picture-db.io");
    }

    private void addTitle(Document document) throws DocumentException {
        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("PictureDB Image Report - " +
                picture.getFileName(), TITLE_FONT));
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Report generated on: " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")), NORMAL_FONT));
        addEmptyLine(preface, 2);
        document.add(preface);
    }

    private void addImage(Document document) throws IOException, DocumentException {
        Image image = Image.getInstance(URLBuilder.getPreparedImgPath(picture.getFileName()));
        float documentWidth = document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin();
        float documentHeight = document.getPageSize().getHeight() - document.topMargin() - document.bottomMargin();
        image.scaleToFit(documentWidth, documentHeight);
        document.add(image);
    }

    private void addTableWithTags(Document document) throws DocumentException {
        // add title and some space
        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 2);
        document.add(preface);
        document.add(new Paragraph("Tags", SUBTITLE_BOLD));
        // create table
        PdfPTable table = new PdfPTable(2);
        PdfPCell c1 = new PdfPCell(new Phrase("Tag Name"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        c1 = new PdfPCell(new Phrase("Tag Label"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);
        addPhotographerData(table);
        addIptcData(table);
        addExifData(table);
        document.add(table);
    }

    private void addPhotographerData(PdfPTable table) {
        table.addCell("Photographer");
        Photographer photographer = this.picture.getPhotographer();
        if (photographer != null) {
            String photographerName = photographer.getFirstName() + " " + photographer.getLastName();
            table.addCell(photographerName);
        } else {
            table.addCell("");
        }
    }

    private void addIptcData(PdfPTable table) {
        Iptc iptc = this.picture.getIptc();
        boolean hasIptc = iptc != null;
        table.addCell("Title");
        table.addCell(hasIptc ? iptc.getTitle() : "");
        table.addCell("Caption");
        table.addCell(hasIptc ? iptc.getCaption() : "");
        table.addCell("City");
        table.addCell(hasIptc ? iptc.getCity() : "");
    }

    private void addExifData(PdfPTable table) {
        // we store always exif data
        table.addCell("Camera");
        table.addCell(this.picture.getExif().getCamera());
        table.addCell("Lens");
        table.addCell(this.picture.getExif().getLens());
        table.addCell("Date");
        table.addCell(this.picture.getExif().getCaptureDate().toString());
    }

    private String getFileName() {
        return this.targetPath + SystemProperties.FILE_SEPARATOR + this.picture.getFileName() + PDF_EXTENSION;
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

}
