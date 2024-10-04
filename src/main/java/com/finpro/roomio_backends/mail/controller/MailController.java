package com.finpro.roomio_backends.mail.controller;

import com.finpro.roomio_backends.mail.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class MailController {

    @Autowired
    private MailService mailService;

    @GetMapping("/send-test-email")
    public String sendTestEmail(@RequestParam String to) {
        mailService.sendTestEmail(to, "Test Email", "This is a test email.");
        return "Email sent successfully to " + to;
    }
}
