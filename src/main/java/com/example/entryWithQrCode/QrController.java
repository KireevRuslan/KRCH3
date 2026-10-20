package com.example.entryWithQrCode;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
public class QrController {
    @GetMapping("/")
    public String index(){
        return "index";
    }

    @PostMapping("/check-qr")
    public String checkQr(
            @RequestParam("uuid") String uuid,
            Model model
    ) {
        UUID parsebUuid = null;
        try {
            parsebUuid = UUID.fromString(uuid);

            model.addAttribute(
                    "message",
                    "UUID имеет корректный формат: " + parsebUuid
            );
        } catch (IllegalArgumentException e) {
            model.addAttribute(
                    "message",
                    "UUID некорректный формат: " + parsebUuid
            );
        }
        return "index";
    }
}
