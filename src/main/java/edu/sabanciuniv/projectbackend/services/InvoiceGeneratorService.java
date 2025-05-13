
package edu.sabanciuniv.projectbackend.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import edu.sabanciuniv.projectbackend.models.Order;
import edu.sabanciuniv.projectbackend.dto.PaymentRequest;
import edu.sabanciuniv.projectbackend.models.OrderItem;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class InvoiceGeneratorService {

    public String generateInvoicePdf(Order order, PaymentRequest request) {
        String fileName = "invoice_" + order.getOrderId() + ".pdf";
        String filePath = new File("build/resources/main/static/invoices", fileName).getAbsolutePath();
        String publicPdfUrl = "http://localhost:8080/invoices/" + fileName;

        Document document = new Document(PageSize.A4, 50, 50, 50, 50);

        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();


            PdfContentByte canvas = writer.getDirectContentUnder();
            Font watermarkFont = new Font(Font.FontFamily.HELVETICA, 100, Font.BOLD, new BaseColor(200, 200, 255)); // Açık mavi ton

            Phrase watermark = new Phrase("NEPTUNE", watermarkFont);


            // Transparanlık
            PdfGState gState = new PdfGState();
            gState.setFillOpacity(0.25f);  // 10% opaklık
            canvas.setGState(gState);

            // Konumlandırma ve yazdırma
            ColumnText.showTextAligned(
                    canvas,
                    Element.ALIGN_CENTER,
                    watermark,
                    297,  // sayfa ortası yatayda (A4: 595x842)
                    421,  // sayfa ortası dikeyde
                    45    // açı: çapraz
            );


            BaseColor neptuneColor = new BaseColor(31, 28, 102);

            // Fontlar
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD);
            Font infoLabelFont = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, BaseColor.DARK_GRAY);
            Font infoValueFont = new Font(Font.FontFamily.HELVETICA, 13);
            Font tableHeaderFont = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, BaseColor.WHITE);
            Font tableRowFont = new Font(Font.FontFamily.HELVETICA, 12);
            Font totalFont = new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD);
            Font contactFont = new Font(Font.FontFamily.HELVETICA, 11, Font.ITALIC, BaseColor.DARK_GRAY);

            ColumnText column = new ColumnText(canvas);

            // NEPTUNE sağ üst köşe
            Paragraph logo = new Paragraph("NEPTUNE", new Font(Font.FontFamily.HELVETICA, 28, Font.BOLD, neptuneColor));
            logo.setAlignment(Element.ALIGN_RIGHT);
            document.add(logo);

            // Sayfanın ortasını aşağı yukarı dengelemek için boşluk
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            // Başlık
            Paragraph title = new Paragraph("INVOICE", titleFont);
            title.setAlignment(Element.ALIGN_LEFT);
            title.setSpacingAfter(20f);
            document.add(title);

            // Order bilgileri hizalı
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidths(new float[]{1f, 3f});
            infoTable.setSpacingBefore(30f);
            infoTable.setSpacingAfter(30f);
            infoTable.setWidthPercentage(70);
            infoTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            infoTable.addCell(getNoBorderCell("Order ID:", infoLabelFont));
            infoTable.addCell(getNoBorderCell(order.getOrderId(), infoValueFont));

            infoTable.addCell(getNoBorderCell("Order Date:", infoLabelFont));
            infoTable.addCell(getNoBorderCell(order.getOrderDate().toLocalDate().toString(), infoValueFont));

            infoTable.addCell(getNoBorderCell("Customer:", infoLabelFont));
            infoTable.addCell(getNoBorderCell(order.getCustomer().getFirstName() + order.getCustomer().getLastName(), infoValueFont));

            infoTable.addCell(getNoBorderCell("Email:", infoLabelFont));
            infoTable.addCell(getNoBorderCell(order.getCustomer().getEmail(), infoValueFont));

            infoTable.addCell(getNoBorderCell("Address:", infoLabelFont));
            infoTable.addCell(getNoBorderCell(
                    request.getAddress() + ", " + request.getCity() + ", " + request.getCountry() + ", " + request.getZipCode(),
                    infoValueFont));

            infoTable.addCell(getNoBorderCell("Phone:", infoLabelFont));
            infoTable.addCell(getNoBorderCell(request.getPhoneNumber(), infoValueFont));


            document.add(infoTable);

            // Tablo
            PdfPTable table = new PdfPTable(new float[]{4, 2, 2});
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(20f);

            String[] headers = {"Product", "Quantity", "Price"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, tableHeaderFont));
                cell.setBackgroundColor(BaseColor.GRAY);
                cell.setPadding(10f);
                cell.setBorder(Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);
            }

            for (OrderItem item : order.getOrderItems()) {
                table.addCell(getStyledRowCell(item.getProduct().getName(), tableRowFont));
                table.addCell(getStyledRowCell(String.valueOf(item.getQuantity()), tableRowFont));
                table.addCell(getStyledRowCell(String.format("$%.2f", item.getProduct().getPrice()), tableRowFont));
            }

            document.add(table);

            // Total
            Paragraph total = new Paragraph("Total Amount: $" + String.format("%.2f", order.getTotalPrice()), totalFont);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            // Alt iletişim mesajı
            Paragraph contact = new Paragraph("If you have any questions, please contact: support@neptune.com", contactFont);
            contact.setSpacingBefore(40f);
            contact.setAlignment(Element.ALIGN_CENTER);
            document.add(contact);

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return publicPdfUrl;
    }

    private PdfPCell getNoBorderCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingBottom(12f);
        return cell;
    }

    private PdfPCell getStyledRowCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.BOTTOM);
        cell.setBorderColor(BaseColor.LIGHT_GRAY);
        cell.setPaddingTop(10f);
        cell.setPaddingBottom(10f);
        return cell;
    }
}

