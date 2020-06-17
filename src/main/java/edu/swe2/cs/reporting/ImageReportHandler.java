package edu.swe2.cs.reporting;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import edu.swe2.cs.model.Iptc;
import edu.swe2.cs.model.Photographer;
import edu.swe2.cs.util.URLBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ImageReportHandler extends AbstractReportHandler {

    public ImageReportHandler(String targetPath) {
        super(targetPath);
    }

    @Override
    protected void generateReport() throws DocumentException, IOException {
        Document document = getDocument();
        addTitle(document);
        addImage(document);
        addTableWithTags(document);
        closeDocument(document);
    }

    private void addTitle(Document document) throws DocumentException {
        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("PictureDB Image Report - " +
                getReport().getFileName(), TITLE_FONT));
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Report generated on: " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")), NORMAL_FONT));
        addEmptyLine(preface, 2);
        document.add(preface);
    }

    private void addImage(Document document) throws IOException, DocumentException {
        Image image = Image.getInstance(URLBuilder.getPreparedImgPath(getReport().getFileName()));
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
        document.add(new Paragraph("IPTC / EXIF", SUBTITLE_BOLD));
        addEmptyLine(preface, 2);
        // create table
        PdfPTable table = new PdfPTable(2);
        PdfPCell c1 = new PdfPCell(new Phrase("Property Name"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        c1 = new PdfPCell(new Phrase("Property Value"));
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
        Photographer photographer = ((ImageReport) getReport()).getPicture().getPhotographer();
        if (photographer != null) {
            String photographerName = photographer.getFirstName() + " " + photographer.getLastName();
            table.addCell(photographerName);
        } else {
            table.addCell("");
        }
    }

    private void addIptcData(PdfPTable table) {
        Iptc iptc = ((ImageReport) getReport()).getPicture().getIptc();
        boolean hasIptc = iptc != null;
        table.addCell("Title");
        table.addCell(hasIptc ? iptc.getTitle() : "");
        table.addCell("Caption");
        table.addCell(hasIptc ? iptc.getCaption() : "");
        table.addCell("City");
        table.addCell(hasIptc ? iptc.getCity() : "");
        table.addCell("Tags");
        table.addCell(hasIptc ? String.join(",", iptc.getAllTags()) : "");
    }

    private void addExifData(PdfPTable table) {
        // we store always exif data
        table.addCell("Camera");
        table.addCell(((ImageReport) getReport()).getPicture().getExif().getCamera());
        table.addCell("Lens");
        table.addCell(((ImageReport) getReport()).getPicture().getExif().getLens());
        table.addCell("Date");
        table.addCell(((ImageReport) getReport()).getPicture().getExif().getCaptureDate().toString());
    }
}
