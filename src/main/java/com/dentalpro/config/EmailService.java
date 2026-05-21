package com.dentalpro.config;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${app.mail.dev-mode:true}")
    private boolean devMode;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public boolean isDevMode() {
        return devMode;
    }

    public void sendWelcomeEmail(String to, String nombre) {
        String subject = "¡Bienvenido a DentalPro, " + nombre + "!";
        String html = """
            <div style="font-family:Arial,sans-serif;max-width:600px;margin:0 auto;background:#0f172a;color:#e2e8f0;border-radius:12px;overflow:hidden">
              <div style="background:#1e3a5f;padding:32px 40px;text-align:center">
                <h1 style="color:#60a5fa;margin:0;font-size:28px;letter-spacing:-1px">🦷 DentalPro</h1>
              </div>
              <div style="padding:40px">
                <h2 style="color:#f1f5f9;font-size:22px">¡Hola, %s!</h2>
                <p style="color:#94a3b8;line-height:1.7">
                  Tu registro en <strong style="color:#60a5fa">DentalPro</strong> fue exitoso.
                  Ya formas parte de nuestra comunidad de pacientes.
                </p>
                <p style="color:#94a3b8;line-height:1.7">
                  Nuestro equipo se pondrá en contacto contigo pronto para confirmar tu primera cita.
                  También puedes comunicarte con nosotros en cualquier momento.
                </p>
                <div style="background:#1e293b;border-radius:8px;padding:20px;margin:24px 0;border-left:4px solid #60a5fa">
                  <p style="margin:0;color:#cbd5e1;font-size:14px">
                    ✅ Registro completado<br>
                    📅 Atención de lunes a sábado<br>
                    📞 Contáctanos para agendar tu cita
                  </p>
                </div>
                <p style="color:#64748b;font-size:13px;margin-top:32px">
                  DentalPro — Cuidamos tu sonrisa con tecnología de primer nivel.
                </p>
              </div>
            </div>
            """.formatted(nombre);

        send(to, subject, html);
    }

    public void sendResetCode(String to, String nombre, String code) {
        String subject = "Código de verificación DentalPro";
        String html = """
            <div style="font-family:Arial,sans-serif;max-width:600px;margin:0 auto;background:#0f172a;color:#e2e8f0;border-radius:12px;overflow:hidden">
              <div style="background:#1e3a5f;padding:32px 40px;text-align:center">
                <h1 style="color:#60a5fa;margin:0;font-size:28px;letter-spacing:-1px">🦷 DentalPro</h1>
              </div>
              <div style="padding:40px">
                <h2 style="color:#f1f5f9;font-size:22px">Hola, %s</h2>
                <p style="color:#94a3b8;line-height:1.7">
                  Recibiste este correo porque solicitaste restablecer tu contraseña.
                  Usa el siguiente código de verificación:
                </p>
                <div style="text-align:center;margin:32px 0">
                  <div style="display:inline-block;background:#1e293b;border:2px solid #60a5fa;border-radius:12px;padding:20px 40px">
                    <span style="font-size:42px;font-weight:bold;letter-spacing:12px;color:#60a5fa">%s</span>
                  </div>
                </div>
                <p style="color:#94a3b8;line-height:1.7;font-size:14px">
                  ⏱️ Este código expira en <strong>10 minutos</strong>.<br>
                  Si no solicitaste este cambio, ignora este correo.
                </p>
                <p style="color:#64748b;font-size:13px;margin-top:32px">
                  DentalPro — Cuidamos tu sonrisa con tecnología de primer nivel.
                </p>
              </div>
            </div>
            """.formatted(nombre, code);

        send(to, subject, html);
    }

    private void send(String to, String subject, String html) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            mailSender.send(message);
        } catch (Exception e) {
            // En dev sin SMTP configurado, solo logueamos el error
            System.err.println("[DentalPro Email] No se pudo enviar a " + to + ": " + e.getMessage());
        }
    }
}
