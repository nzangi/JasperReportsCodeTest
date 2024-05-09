package com.jasperreportscodetest.utils;

import net.sf.jasperreports.engine.JRDefaultScriptlet;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ReportUtils  {
    public static Double calculateBonus(Double salary) {
        // Calculate 10% bonus
        Double bonus = salary * 0.10;
        // Round the bonus to two decimal places
        BigDecimal roundedBonus = BigDecimal.valueOf(bonus).setScale(2, RoundingMode.HALF_UP);
        return roundedBonus.doubleValue();
        // return salary * 0.10; // Calculate 10% bonus
    }
}
