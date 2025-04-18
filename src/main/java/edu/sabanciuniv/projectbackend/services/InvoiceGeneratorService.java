package edu.sabanciuniv.projectbackend.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import edu.sabanciuniv.projectbackend.models.Order;
import edu.sabanciuniv.projectbackend.models.OrderItem;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;


@Service
public class InvoiceGeneratorService {

    public String generateInvoicePdf(Order order) {
        String fileName = "invoice_" + order.getOrderId() + ".pdf";
        String filePath = new File("build/resources/main/static/invoices", fileName).getAbsolutePath();
        String publicPdfUrl = "http://localhost:8080/invoices/" + fileName;

        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Başlık
            Font boldFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Paragraph title = new Paragraph("Invoice - Order #" + order.getOrderId(), boldFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            // Müşteri bilgisi
            document.add(new Paragraph("Customer: " + order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName()));
            document.add(new Paragraph("Email: " + order.getCustomer().getEmail()));
            document.add(new Paragraph("Date: " + order.getOrderDate().toString()));
            document.add(new Paragraph(" "));

            // Ürün tablosu
            PdfPTable table = new PdfPTable(3); // 3 sütun
            table.addCell("Product");
            table.addCell("Quantity");
            table.addCell("Price");

            for (OrderItem item : order.getOrderItems()) {
                table.addCell(item.getProduct().getName());
                table.addCell(String.valueOf(item.getQuantity()));
                table.addCell(String.format("%.2f", item.getProduct().getPrice()));
            }

            document.add(table);

            // Toplam
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Total: $" + order.getTotalPrice(), boldFont));

            document.close();

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

        return "http://localhost:8080/invoices/" + fileName;
    }
}
