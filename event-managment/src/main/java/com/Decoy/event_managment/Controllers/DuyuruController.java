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
            duyurular = duyuruRepository.findByDurumLessThanEqual(filtre);
        }
        else
        {
            duyurular = duyuruRepository.findAll();
        }

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
}
