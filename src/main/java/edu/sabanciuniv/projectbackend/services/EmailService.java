package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.Product;
import edu.sabanciuniv.projectbackend.models.Refund;
import edu.sabanciuniv.projectbackend.utils.EncryptionUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final EncryptionUtil encryptionUtil;

    @Autowired
    public EmailService(JavaMailSender mailSender, EncryptionUtil encryptionUtil) {
        this.mailSender = mailSender;
        this.encryptionUtil = encryptionUtil;
    }

    public void sendInvoiceEmail(String toEmail, String pdfUrl) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            String decryptedPdfUrl;
            if (!pdfUrl.startsWith("http")) {
                decryptedPdfUrl = encryptionUtil.decryptString(pdfUrl);
            }
            else{
                decryptedPdfUrl = pdfUrl;
            }
            helper.setTo(toEmail);
            helper.setSubject("Your Neptune Invoice");
            helper.setText(
                    "<p>Dear Customer,</p>" +
                            "<p>Thank you for your purchase. Please find your invoice at the link below:</p>" +
                            "<a href='" + decryptedPdfUrl + "'>Download Invoice (PDF)</a>" +
                            "<p>Best regards,<br>Neptune Tech</p>",
                    true
            );

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    @Async           // ➜ farklı thread'te çalışır, performans kaybı yok
    public void sendDiscountMail(String to, Product p) {
        try {
            MimeMessage msg  = mailSender.createMimeMessage();
            MimeMessageHelper h = new MimeMessageHelper(msg, true, "UTF-8");

            h.setTo(to);
            h.setSubject("SALE! " + p.getName() + " is now %.2f $".formatted(p.getDiscountedPrice()));
            h.setText("""
                    <p>Hi,</p>
                    <p>The <strong>%s</strong> product in your favorite list is on sale.</p>
                    <p>New Price: <strong>%.2f $</strong></p>
                    <p>Thank you for your interest!<br>Neptune Tech</p>
                    """.formatted(p.getName(), p.getDiscountedPrice()), true);

            mailSender.send(msg);

        } catch (MessagingException e) {
            // sadece logla – işletmeyi durdurmasın
            System.err.println("Discount mail failed: " + e.getMessage());
        }
    }

    @Async
    public void sendRefundConfirmationEmail(Refund refund) {
        try {
            System.out.println("\n=== Starting Email Send Process ===");
            System.out.println("1. Creating MimeMessage...");
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String customerEmail = refund.getOrder().getCustomer().getEmail();
            System.out.println("2. Setting recipient email: " + customerEmail);
            helper.setTo(customerEmail);
            
            String subject = "Refund Confirmation";
            System.out.println("3. Setting subject: " + subject);
            helper.setSubject(subject);

            System.out.println("4. Checking refund details:");
            System.out.println("   - Process Date: " + refund.getProcessDate());
            System.out.println("   - Customer Name: " + refund.getOrder().getCustomer().getFirstName());
            System.out.println("   - Order ID: " + refund.getOrder().getOrderId());
            System.out.println("   - Refund Amount: " + refund.getRefundAmount());
            System.out.println("   - Refund Reason: " + refund.getReason());

            String emailContent = """
                    <p>Dear %s,</p>
                    
                    <p>We are writing to confirm that your refund request for Order #%s has been processed successfully.</p>
                    
                    <p><strong>Refund Details:</strong></p>
                    <ul>
                        <li>Order ID: %s</li>
                        <li>Refund Amount: $%.2f</li>
                        <li>Refund Date: %s</li>
                        <li>Refund Reason: %s</li>
                    </ul>
                    
                    <p>The refunded amount will be credited back to your original payment method within 5-7 business days.</p>
                    
                    <p>If you have any questions about this refund, please don't hesitate to contact our customer service team.</p>
                    
                    <p>Thank you for your patience and understanding.</p>
                    
                    <p>Best regards,<br>Neptune Tech Customer Service</p>
                    """.formatted(
                            refund.getOrder().getCustomer().getFirstName(),
                            refund.getOrder().getOrderId(),
                            refund.getOrder().getOrderId(),
                            refund.getRefundAmount(),
                            refund.getProcessDate() != null ? refund.getProcessDate().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "N/A",
                            refund.getReason()
                    );
            
            System.out.println("5. Setting email content...");
            helper.setText(emailContent, true);

            System.out.println("6. Sending email...");
            mailSender.send(message);
            System.out.println("7. Email sent successfully!");
            System.out.println("=== Email Send Process Completed ===\n");

        } catch (MessagingException e) {
            System.err.println("\n=== Email Send Failed ===");
            System.err.println("Error Type: " + e.getClass().getName());
            System.err.println("Error Message: " + e.getMessage());
            System.err.println("Stack trace:");
            e.printStackTrace();
            System.err.println("=======================\n");
        } catch (Exception e) {
            System.err.println("\n=== Unexpected Error ===");
            System.err.println("Error Type: " + e.getClass().getName());
            System.err.println("Error Message: " + e.getMessage());
            System.err.println("Stack trace:");
            e.printStackTrace();
            System.err.println("=======================\n");
        }
    }
}