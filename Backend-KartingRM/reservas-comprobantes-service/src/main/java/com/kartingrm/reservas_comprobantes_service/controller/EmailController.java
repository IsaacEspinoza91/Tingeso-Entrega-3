package com.kartingrm.reservas_comprobantes_service.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservas-comprobantes-service/email")
public class EmailController {

    private final JavaMailSender mailSender;
    private static final String FROM_EMAIL = "tingeso.karting.rm@gmail.com";

    public EmailController(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @PostMapping("/send-pdf")
    public ResponseEntity<String> sendPdfEmail(@RequestParam("pdf") MultipartFile pdfFile,
                                               @RequestParam("email") String toEmail,
                                               @RequestParam(value = "subject", defaultValue = "Comprobante de Pago") String subject) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(FROM_EMAIL);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText("Muchas gracias por su reserva en Karting RM.\n\nAdjunto encontrará su comprobante de pago.");

            // validación, nombre de archivo
            String filename = Optional.ofNullable(pdfFile.getOriginalFilename())
                    .filter(name -> !name.isBlank())
                    .orElse("comprobante.pdf");

            // Adjuntar el PDF
            helper.addAttachment(filename, pdfFile::getInputStream);

            mailSender.send(message); // Enviar correo
            return ResponseEntity.ok("Correo enviado exitosamente");

        } catch (MessagingException e) {
            return ResponseEntity.badRequest()
                    .body("Error al enviar el correo: " + e.getMessage());
        }
    }
}