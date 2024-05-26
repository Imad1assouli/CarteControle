package com.cartecontrole.repositories;

import com.cartecontrole.entities.ControlChart;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ControlChartRepository extends MongoRepository<ControlChart, String> {
}
