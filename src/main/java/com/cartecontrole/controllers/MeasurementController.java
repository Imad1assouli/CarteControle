package com.cartecontrole.controllers;

import com.cartecontrole.entities.Measurement;
import com.cartecontrole.services.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/measurements")
public class MeasurementController {
    @Autowired
    private MeasurementService service;

    @GetMapping
    public List<Measurement> getAllMeasurements() {
        return service.findAll();
    }

    @GetMapping("/{controlChartId}")
    public List<Measurement> getMeasurementsByControlChartId(@PathVariable String controlChartId) {
        return service.findByControlChartId(controlChartId);
    }

    @PostMapping
    public Measurement createMeasurement(@RequestBody Measurement measurement) {
        return service.save(measurement);
    }

    @PutMapping("/{id}")
    public Measurement updateMeasurement(@PathVariable String id, @RequestBody Measurement measurement) {
        measurement.setId(id);
        return service.update(measurement);
    }

    @DeleteMapping("/{id}")
    public void deleteMeasurement(@PathVariable String id) {
        service.deleteById(id);
    }
}
