package com.cartecontrole.services;

import com.cartecontrole.repositories.MeasurementRepository;
import com.cartecontrole.entities.Measurement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeasurementService {
    @Autowired
    private MeasurementRepository repository;

    public List<Measurement> findAll() {
        return repository.findAll();
    }

    public Measurement save(Measurement measurement) {
        return repository.save(measurement);
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }

    public Measurement update(Measurement measurement) {
        return repository.save(measurement);
    }

    public List<Measurement> findByControlChartId(String controlChartId) {
        return repository.findByControlChartId(controlChartId);
    }
}
