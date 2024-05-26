package com.cartecontrole.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.List;

@Data
@Document(collection = "controlcharts")
public class ControlChart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String title;
    private String type;  // Control chart type: X-bar, Range, etc.
    private List<Measurement> measurements;
    private List<Echantillon> echantillons;
    private double xBar; // Moyenne des moyennes
    private double rBar; // Moyenne des étendues
    private double upperControlLimit; // UCL
    private double lowerControlLimit; // LCL
    private double centralLine; // Center line
    private double upperSecurityLimit; // USL
    private double lowerSecurityLimit; // LSL
    private double lowerToleranceLimit; // LTL
    private double upperToleranceLimit; // UTL
    private double normalityTestPValue; 
    private double cp; // Process capability
    private double cpk; // Process capability index
    private double cm; // Process capability ratio
    private double cmk; // Process capability ratio
    private List<String> interpretations;
}
