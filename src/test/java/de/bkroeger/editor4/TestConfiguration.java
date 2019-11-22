package de.bkroeger.editor4;

import org.springframework.context.annotation.Bean;

import de.bkroeger.editor4.calculation.CalculatorService;

public class TestConfiguration {

    @Bean
    public CalculatorService<String> stringCalculator() {
        return new CalculatorService<String>();
    }

    @Bean
    public CalculatorService<Double> doubleCalculator() {
        return new CalculatorService<Double>();
    }

}
