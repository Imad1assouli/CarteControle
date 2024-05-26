package com.cartecontrole.services;

import com.cartecontrole.entities.Echantillon;
import com.cartecontrole.entities.Measurement;
import com.cartecontrole.repositories.EchantillonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EchantillonService {
    @Autowired
    private EchantillonRepository repository;

    public List<Echantillon> findAll() {
        return repository.findAll();
    }

    public Echantillon findById(String id) {
        return repository.findById(id).orElse(null);
    }

    public Echantillon save(Echantillon echantillon) {
        calculateMean(echantillon);
        calculateRange(echantillon);
        return repository.save(echantillon);
    }

    public Echantillon update(Echantillon echantillon) {
        return repository.save(echantillon);
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void calculateMean(Echantillon echantillon) {
        List<Measurement> measurements = echantillon.getMeasurements();
        double sum = 0;
        for (Measurement measurement : measurements) { // Change the loop variable type to Measurement
            sum += measurement.getValue(); // Assuming there is a getValue() method in the Measurement class to get the measurement value
        }
        double mean = sum / measurements.size();
        echantillon.setMean(mean);
    }

    public void calculateRange(Echantillon echantillon) {
        List<Measurement> measurements = echantillon.getMeasurements();
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        for (Measurement measurement : measurements) {
            double value = measurement.getValue();
            if (value > max) {
            max = value;
            }
            if (value < min) {
            min = value;
            }
        }
        double range = max - min;
        echantillon.setRange(range);
    }
}
