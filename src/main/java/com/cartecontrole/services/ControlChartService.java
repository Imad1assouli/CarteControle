package com.cartecontrole.services;

import com.cartecontrole.entities.ControlChart;
import com.cartecontrole.entities.Echantillon;
import com.cartecontrole.entities.Measurement;
import com.cartecontrole.repositories.ControlChartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.inference.KolmogorovSmirnovTest;
import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.ArrayList;
import java.util.List;

@Service
public class ControlChartService {
    @Autowired
    private ControlChartRepository repository;

    public List<ControlChart> findAll() {
        return repository.findAll();
    }

    public ControlChart findById(String id) {
        return repository.findById(id).orElse(null);
    }

    public ControlChart save(ControlChart controlChart) {
        performCalculations(controlChart);
        return repository.save(controlChart);
    }

    public ControlChart update(ControlChart controlChart) {
        performCalculations(controlChart);
        return repository.save(controlChart);
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }

    private void performCalculations(ControlChart controlChart) {
        if ("X-bar".equals(controlChart.getType())) {
            calculateXBarChart(controlChart);
        }
        // Add other chart type calculations as needed
    }


    // calculate mean of means
    public double calculateXBar(ControlChart controlChart) {
        List<Echantillon> echantillons = controlChart.getEchantillons();
        double sum = 0;
        for (Echantillon echantillon : echantillons) {
            sum += echantillon.getMean();
        }
        double xBar = sum / echantillons.size();
        controlChart.setXBar(xBar);
        return xBar;
    }

    // calculate mean of ranges
    public double calculateRBar(ControlChart controlChart) {
        List<Echantillon> echantillons = controlChart.getEchantillons();
        double sum = 0;
        for (Echantillon echantillon : echantillons) {
            sum += echantillon.getRange();
        }
        double rBar = sum / echantillons.size();
        controlChart.setRBar(rBar);
        return rBar;
    }

    // calculate UCL and ICL 
    public void calculateControlLimits(ControlChart controlChart) {
        double xBar = controlChart.getXBar();
        double rBar = controlChart.getRBar();
        double A2 = 0.483;
        double upperControlLimit = xBar + A2 * rBar;
        double lowerControlLimit = xBar - A2 * rBar;
        double upperSecurityLimit = xBar + 3 * rBar;
        double lowerSecurityLimit = xBar - 3 * rBar;
        controlChart.setUpperControlLimit(upperControlLimit);
        controlChart.setLowerControlLimit(lowerControlLimit);
        controlChart.setUpperSecurityLimit(upperSecurityLimit);
        controlChart.setLowerSecurityLimit(lowerSecurityLimit);
    }

    // X-bar chart calculations
    private void calculateXBarChart(ControlChart controlChart) {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (Echantillon echantillon : controlChart.getEchantillons()) {
            for (Measurement measurement : echantillon.getMeasurements()) {
                stats.addValue(measurement.getValue());
            }
        }
        double mean = stats.getMean();
        double stdDev = stats.getStandardDeviation();
        controlChart.setCp((controlChart.getUpperSecurityLimit() - controlChart.getLowerSecurityLimit()) / (6 * stdDev));
        controlChart.setCpk(Math.min((controlChart.getUpperSecurityLimit() - mean) / (3 * stdDev), (mean - controlChart.getLowerSecurityLimit()) / (3 * stdDev)));
        controlChart.setCm(controlChart.getCp());
        controlChart.setCmk(controlChart.getCpk());

        KolmogorovSmirnovTest test = new KolmogorovSmirnovTest();
        NormalDistribution normalDistribution = new NormalDistribution(mean, stdDev);
        double pValue = test.kolmogorovSmirnovTest(normalDistribution, stats.getValues());
        controlChart.setNormalityTestPValue(pValue);

        double lowerToleranceLimit = mean - 1.96 * stdDev;
        double upperToleranceLimit = mean + 1.96 * stdDev;
        controlChart.setLowerToleranceLimit(lowerToleranceLimit);
        controlChart.setUpperToleranceLimit(upperToleranceLimit);

        controlChart.setUpperControlLimit(mean + 3 * stdDev);
        controlChart.setLowerControlLimit(mean - 3 * stdDev);
        controlChart.setCentralLine(mean);

        List<String> interpretations = interpretXBarChart(controlChart);
        controlChart.setInterpretations(interpretations);

        System.out.println("Interpretations: " + interpretations);
    }

    private List<String> interpretXBarChart(ControlChart controlChart) {
        List<String> interpretations = new ArrayList<>();
        double[] values = controlChart.getEchantillons().stream()
                .flatMap(e -> e.getMeasurements().stream())
                .mapToDouble(Measurement::getValue)
                .toArray();
        double upperControlLimit = controlChart.getUpperControlLimit();
        double lowerControlLimit = controlChart.getLowerControlLimit();
        double centralLine = controlChart.getCentralLine();

        // Check for points outside control limits
        for (double value : values) {
            if (value > upperControlLimit || value < lowerControlLimit) {
                interpretations.add("One or more points are outside the control limits.");
                break;
            }
        }

        // Check for runs of seven or more points all above or all below the center line
        if (checkRun(values, centralLine, 7)) {
            interpretations.add("There is a run of seven or more points all above or all below the center line.");
        }

        System.out.println("Interpretations: " + interpretations);

        return interpretations;
    }

    private boolean checkRun(double[] values, double centralLine, int runLength) {
        int countAbove = 0;
        int countBelow = 0;

        for (double value : values) {
            if (value > centralLine) {
                countAbove++;
                countBelow = 0;
            } else {
                countBelow++;
                countAbove = 0;
            }

            if (countAbove >= runLength || countBelow >= runLength) {
                return true;
            }
        }
        return false;
    }
}
