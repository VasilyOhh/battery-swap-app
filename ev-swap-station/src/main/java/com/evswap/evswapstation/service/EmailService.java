package com.evswap.evswapstation.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.frontend.url:http://localhost:3000}")
    private String frontendUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Async
    public void sendPasswordResetEmail(String recipientEmail, String token, String userName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(recipientEmail);
            helper.setSubject("Yêu cầu đặt lại mật khẩu - EVSwap Station");

            String resetUrl = frontendUrl + "/reset-password?token=" + token;

            String content = buildEmailContent(userName, resetUrl);

            helper.setText(content, true);
            mailSender.send(message);

            log.info("Đã gửi email đặt lại mật khẩu đến: {}", recipientEmail);

        } catch (MessagingException e) {
            log.error("Lỗi khi gửi email đến: {}", recipientEmail, e);
            throw new RuntimeException("Không thể gửi email: " + e.getMessage());
        }
    }

    private String buildEmailContent(String userName, String resetUrl) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }" +
                ".content { background: #f9f9f9; padding: 30px; }" +
                ".button { display: inline-block; padding: 15px 30px; background: #667eea; color: white; text-decoration: none; border-radius: 5px; margin: 20px 0; }" +
                ".footer { background: #333; color: #fff; padding: 20px; text-align: center; font-size: 12px; border-radius: 0 0 10px 10px; }" +
                ".warning { background: #fff3cd; border-left: 4px solid #ffc107; padding: 15px; margin: 20px 0; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1>🔐 Đặt lại mật khẩu</h1>" +
                "</div>" +
                "<div class='content'>" +
                "<p>Xin chào <strong>" + userName + "</strong>,</p>" +
                "<p>Chúng tôi đã nhận được yêu cầu đặt lại mật khẩu cho tài khoản EVSwap Station của bạn.</p>" +
                "<p>Vui lòng click vào nút bên dưới để đặt lại mật khẩu:</p>" +
                "<div style='text-align: center;'>" +
                "<a href='" + resetUrl + "' class='button'>Đặt lại mật khẩu</a>" +
                "</div>" +
                "<p>Hoặc copy link sau vào trình duyệt:</p>" +
                "<p style='background: #e9ecef; padding: 10px; word-break: break-all; font-size: 12px;'>" + resetUrl + "</p>" +
                "<div class='warning'>" +
                "<strong>⚠️ Lưu ý quan trọng:</strong>" +
                "<ul>" +
                "<li>Link này sẽ hết hạn sau <strong>15 phút</strong></li>" +
                "<li>Link chỉ có thể sử dụng <strong>một lần</strong></li>" +
                "<li>Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này</li>" +
                "</ul>" +
                "</div>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>&copy; 2024 EVSwap Station. All rights reserved.</p>" +
                "<p>Email này được gửi tự động, vui lòng không trả lời.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}
