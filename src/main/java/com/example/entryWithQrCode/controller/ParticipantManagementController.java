
package com.example.entryWithQrCode.controller;

import com.example.entryWithQrCode.dto.ParticipantScanResult;
import com.example.entryWithQrCode.service.ParticipantManagementService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ParticipantManagementController {

    private final ParticipantManagementService service;

    public ParticipantManagementController(
            ParticipantManagementService service) {
        this.service = service;
    }

    @GetMapping("/participants")
    public String showPage() {
        return "participants";
    }

    @PostMapping("/participants/add")
    public String addParticipant(
            @RequestParam String fullName,
            RedirectAttributes redirectAttributes) {

        System.out.println("Вызван контроллер добавления участника");

        try {
            ParticipantScanResult participant =
                    service.addParticipant(fullName);

            redirectAttributes.addFlashAttribute(
                    "message",
                    "Участник успешно добавлен"
            );
            redirectAttributes.addFlashAttribute(
                    "createdFullName",
                    participant.fullName()
            );
            redirectAttributes.addFlashAttribute(
                    "newUuid",
                    participant.newUuid()
            );

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(
                    "message",
                    e.getMessage()
            );
        }

        return "redirect:/participants";
    }

    @PostMapping("/participants/delete")
    public String deleteParticipant(
            @RequestParam String fullName,
            RedirectAttributes redirectAttributes) {

        try {
            int deleted = service.deleteByFullName(fullName);

            if (deleted > 0) {
                redirectAttributes.addFlashAttribute(
                        "message",
                        "Удалено участников: " + deleted
                );
            } else {
                redirectAttributes.addFlashAttribute(
                        "message",
                        "Участник с таким ФИО не найден"
                );
            }

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(
                    "message",
                    e.getMessage()
            );
        }

        return "redirect:/participants";
    }
}
