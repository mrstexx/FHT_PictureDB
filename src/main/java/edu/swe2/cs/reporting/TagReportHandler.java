package edu.swe2.cs.reporting;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import java.io.IOException;
import java.util.Map;

public class TagReportHandler extends AbstractReportHandler {

    public TagReportHandler(String targetPath) {
        super(targetPath);
    }

    @Override
    protected void generateReport() throws DocumentException, IOException {
        Document document = getDocument();
        addTitle(document);
        addTagsTable(document);
        closeDocument(document);
    }

    private void addTitle(Document document) throws DocumentException {
        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Tags Report", TITLE_FONT));
        addEmptyLine(preface, 2);
        document.add(preface);
    }

    private void addTagsTable(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        PdfPCell c1 = new PdfPCell(new Phrase("Tag Name"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        c1 = new PdfPCell(new Phrase("Number of images using tag"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);
        addTagsData(table);
        document.add(table);
    }

    private void addTagsData(PdfPTable table) {
        Map<String, Integer> tagsData = ((TagReport) getReport()).getTagsData();
        tagsData.forEach((tagName, number) -> {
            table.addCell(tagName);
            table.addCell(number.toString());
        });
    }
}
