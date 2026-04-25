package com.Decoy.event_managment.Controllers;

import com.Decoy.event_managment.Models.Duyuru;
import com.Decoy.event_managment.Models.DuyuruDurumEnum;
import com.Decoy.event_managment.Repositorys.DuyuruRepository;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DuyuruController
{
    @Autowired
    private DuyuruRepository duyuruRepository;

    @GetMapping("/")
    public String anaSayfa(@RequestParam(required = false) Integer filtre, Model model)
    {
        List<Duyuru> duyurular;
        LocalDateTime simdi = LocalDateTime.now();

        if (filtre != null)
        {
            LocalDateTime start;
            LocalDateTime end;

            switch (filtre) {
                case 0 -> { start = simdi.toLocalDate().atStartOfDay(); end = start.plusDays(1); duyurular = duyuruRepository.findByEtkinlikTarihiBetween(start, end); }
                case 1 -> { start = simdi.toLocalDate().plusDays(1).atStartOfDay(); end = start.plusDays(1); duyurular = duyuruRepository.findByEtkinlikTarihiBetween(start, end); }
                case 2 -> { start = simdi.toLocalDate().atStartOfDay(); end = start.plusWeeks(1); duyurular = duyuruRepository.findByEtkinlikTarihiBetween(start, end); }
                case 3 -> { start = simdi.toLocalDate().atStartOfDay(); end = start.plusMonths(1); duyurular = duyuruRepository.findByEtkinlikTarihiBetween(start, end); }
                case 4 -> { start = simdi.toLocalDate().atStartOfDay(); end = start.plusMonths(3); duyurular = duyuruRepository.findByEtkinlikTarihiBetween(start, end); }
                case 5 -> { start = simdi.toLocalDate().atStartOfDay(); end = start.plusYears(1); duyurular = duyuruRepository.findByEtkinlikTarihiBetween(start, end); }
                case 6 -> { start = simdi.plusYears(1); end = simdi.plusYears(100); duyurular = duyuruRepository.findByEtkinlikTarihiBetween(start, end); }
                case 7 -> { start = simdi.minusYears(100); end = simdi; duyurular = duyuruRepository.findByEtkinlikTarihiBetween(start, end); }
                case 8 -> duyurular = duyuruRepository.findByEtkinlikTarihiIsNull();
                default -> duyurular = duyuruRepository.findAll();
            }
        }
        else
        {
            duyurular = duyuruRepository.findAll();
        }

        duyurular.sort((a, b) -> {
            if (a.getEtkinlikTarihi() == null) return 1;
            if (b.getEtkinlikTarihi() == null) return -1;
            return a.getEtkinlikTarihi().compareTo(b.getEtkinlikTarihi());
        });

        model.addAttribute("duyurular", duyurular);
        model.addAttribute("filtre", filtre);
        return "Home";
    }

    @GetMapping("/duyuru/{id}")
    public String detay(@PathVariable Long id, Model model)
    {
        Duyuru duyuru = duyuruRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Duyuru bulunamadı"));
        model.addAttribute("duyuru", duyuru);
        return "detay";
    }

    @GetMapping("/info")
    @ResponseBody
    public Map<String, String> info() {
        Map<String, String> info = new HashMap<>();
        info.put("hostname", System.getenv("HOSTNAME"));
        info.put("appName", "event-management");
        info.put("javaVersion", System.getProperty("java.version"));
        return info;
    }

    @PostMapping("/add")
    public String add(@ModelAttribute Duyuru duyuru) {
        duyuruRepository.save(duyuru);
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        duyuruRepository.deleteById(id);
        return "redirect:/";
    }
}