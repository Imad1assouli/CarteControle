package com.cartecontrole.controllers;

import com.cartecontrole.entities.Echantillon;
import com.cartecontrole.services.EchantillonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/echantillons")
public class EchantillonController {
    @Autowired
    private EchantillonService service;

    @GetMapping
    public List<Echantillon> getAllEchantillons() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Echantillon getEchantillonById(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping
    public Echantillon createEchantillon(@RequestBody Echantillon echantillon) {
        return service.save(echantillon);
    }

    @PutMapping("/{id}")
    public Echantillon updateEchantillon(@PathVariable String id, @RequestBody Echantillon echantillon) {
        echantillon.setId(id);
        return service.update(echantillon);
    }

    @DeleteMapping("/{id}")
    public void deleteEchantillon(@PathVariable String id) {
        service.deleteById(id);
    }
}
