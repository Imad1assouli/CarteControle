package com.cartecontrole.repositories;

import com.cartecontrole.entities.Measurement;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MeasurementRepository extends MongoRepository<Measurement, String> {
    List<Measurement> findByControlChartId(String controlChartId);
}