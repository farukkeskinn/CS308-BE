package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.Product;
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

    @Async           // ➜ farklı thread’te çalışır, performans kaybı yok
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
                    <p>Thank you for your interest!<br>Neptune Tech</p>
                    """.formatted(p.getName(), p.getDiscountedPrice()), true);

            mailSender.send(msg);

        } catch (MessagingException e) {
            // sadece logla – işletmeyi durdurmasın
            System.err.println("Discount mail failed: " + e.getMessage());
        }
    }
}