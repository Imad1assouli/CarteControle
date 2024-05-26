package com.cartecontrole.controllers;

import com.cartecontrole.entities.ControlChart;
import com.cartecontrole.services.ControlChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/controlcharts")
@CrossOrigin(origins = "http://localhost:4200")
public class ControlChartController {
    @Autowired
    private ControlChartService service;

    @GetMapping
    public List<ControlChart> getAllControlCharts() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ControlChart getControlChartById(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping
    public ControlChart createControlChart(@RequestBody ControlChart controlChart) {
        return service.save(controlChart);
    }

    @PutMapping("/{id}")
    public ControlChart updateControlChart(@PathVariable String id, @RequestBody ControlChart controlChart) {
        controlChart.setId(id);
        return service.update(controlChart);
    }

    @DeleteMapping("/{id}")
    public void deleteControlChart(@PathVariable String id) {
        service.deleteById(id);
    }
}
