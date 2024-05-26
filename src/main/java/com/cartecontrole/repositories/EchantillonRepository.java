package com.cartecontrole.repositories;

import com.cartecontrole.entities.Echantillon;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EchantillonRepository extends MongoRepository<Echantillon, String> {
}
