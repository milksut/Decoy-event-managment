package com.Decoy.event_managment.Controllers;

import com.Decoy.event_managment.Models.Duyuru;
import com.Decoy.event_managment.Models.DuyuruDurumEnum;
import com.Decoy.event_managment.Repositorys.DuyuruRepository;
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
            if(filtre == DuyuruDurumEnum.TarihiGecmis.value
            || filtre == DuyuruDurumEnum.TarihiBelirsiz.value)
            {
                duyurular = duyuruRepository.findByDurum(filtre);
            }
            else
            {
                duyurular = duyuruRepository.findByDurumLessThanEqual(filtre);
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

    private int calculateDurum(LocalDateTime tarih)
    {
        if (tarih == null)
            return DuyuruDurumEnum.TarihiBelirsiz.value;

        LocalDateTime simdi = LocalDateTime.now();

        if (tarih.isBefore(simdi))
            return DuyuruDurumEnum.TarihiGecmis.value;

        if (tarih.isBefore(simdi.plusDays(1)))
            return DuyuruDurumEnum.Bugun.value;

        if (tarih.isBefore(simdi.plusDays(2)))
            return DuyuruDurumEnum.Yarin.value;

        if (tarih.isBefore(simdi.plusWeeks(1)))
            return DuyuruDurumEnum.BuHafta.value;

        if (tarih.isBefore(simdi.plusMonths(1)))
            return DuyuruDurumEnum.BuAy.value;

        if (tarih.isBefore(simdi.plusMonths(3)))
            return DuyuruDurumEnum.UcAy.value;

        if (tarih.isBefore(simdi.plusYears(1)))
            return DuyuruDurumEnum.BuYil.value;

        return DuyuruDurumEnum.BirYildanFazla.value;
    }

    @PostMapping("/add")
    public String add(@ModelAttribute Duyuru duyuru)
    {
        duyuru.setDurum(calculateDurum(duyuru.getEtkinlikTarihi()));
        duyuruRepository.save(duyuru);
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        duyuruRepository.deleteById(id);
        return "redirect:/";
    }

    @PostMapping("/update_dates")
    public String update_dates()
    {
        List<Duyuru> duyurular =
                duyuruRepository.findByDurumLessThanEqual(DuyuruDurumEnum.BirYildanFazla.value);

        for(Duyuru d : duyurular)
        {
            d.setDurum(calculateDurum(d.getEtkinlikTarihi()));
        }
        duyuruRepository.saveAll(duyurular);
        return "redirect:/";
    }
}