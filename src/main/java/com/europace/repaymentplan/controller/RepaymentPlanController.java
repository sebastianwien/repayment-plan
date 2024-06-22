package com.europace.repaymentplan.controller;

import com.europace.repaymentplan.model.RepaymentPlanEntry;
import com.europace.repaymentplan.util.Constants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1")
public class RepaymentPlanController {

    /**
     * REST endpoint for creating a repayment plan for given loan amount, annual interest rate, repayment rate and fixed duration of initial interest rate.
     * @param loanAmount total amount of requested loan, e.g. '100000'
     * @param annualInterestRate annual rate of interest in percentage, e.g. '2.55'
     * @param repaymentRate initial desired repayment rate in percentage, e.g. '2.0'
     * @param fixedInterestDuration the amount of years of annualInterestRate, e.g. '10'
     * @return Returns a repayment plan on a monthly basis with initial values and final summary of repayments and interest.
     */
    @PostMapping(path = "/repayment-plan", produces = "application/json")
    public ResponseEntity<Map> calculateMonthlyPayment(
        @RequestParam double loanAmount,
        @RequestParam double annualInterestRate,
        @RequestParam double repaymentRate,
        @RequestParam int fixedInterestDuration) {

        // preparation
        LocalDate startingDate = LocalDate.of(2015, 11, 30);
        annualInterestRate = annualInterestRate / 100;
        repaymentRate = repaymentRate / 100;
        Map<LocalDate, RepaymentPlanEntry> result = new LinkedHashMap<>();
        double annuity = bankersRound(loanAmount * (annualInterestRate / 12 + repaymentRate / 12));

        // calculation
        for (int i = 0; i < fixedInterestDuration; i++) {
            for (int month = 0; month < Month.values().length; month++) {

                double currentLoan;
                if (result.isEmpty()) {
                    currentLoan = loanAmount;
                } else {
                    currentLoan = bankersRound(result.get(startingDate).getResidualDebt()) * -1;
                }

                double interest = bankersRound(currentLoan * (annualInterestRate / 12)); // Zinsen
                double repayment = annuity - interest;                                              // Tilgung
                double residualDebt = currentLoan - repayment;                                      // Restschuld
                double loanRate = interest + repayment;                                             // Rate

                startingDate = startingDate.plusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
                result.put(startingDate, new RepaymentPlanEntry(residualDebt * -1, interest, repayment, loanRate));
            }
        }
        double totalInterest = 0;
        double totalRepayment = 0;

        // printf() output for pretty printing on the console :)
        System.out.printf("\n\n%16s | %13s | %11s | %10s | %10s\n", "Datum", "Restschuld", "Zinsen", "Tilgung(+)/Ausz.(-)", "Rate");
        System.out.println("----------------------------------------------------------------------------------");
        System.out.printf("%16s | %13s | %11s | %19s | %10s\n",
            startingDate,
            Constants.NUMBER_FORMAT.format(loanAmount * -1),
            Constants.NUMBER_FORMAT.format(0.00),
            Constants.NUMBER_FORMAT.format(loanAmount * -1),
            Constants.NUMBER_FORMAT.format(loanAmount * -1)
        );
        for (Map.Entry<LocalDate, RepaymentPlanEntry> entry : result.entrySet()) {
            RepaymentPlanEntry value = entry.getValue();
            if (entry.getKey().getMonthValue() == 12) {
                System.out.println("----------------------------------------------------------------------------------");
            }
            System.out.printf("%16s | %13s | %11s | %19s | %10s\n",
                entry.getKey(), value.getResidualDebtFormatted(), value.getInterestFormatted(), value.getRepaymentFormatted(), value.getLoanRateFormatted());
            totalInterest += value.getInterest();
            totalRepayment += value.getRepayment();
        }
        RepaymentPlanEntry lastEntry = result.get(startingDate);
        System.out.println("----------------------------------------------------------------------------------");
        System.out.printf("%10s | %13s | %11s | %19s | %10s\n",
            "Zinsbindungsende",
            lastEntry.getResidualDebtFormatted(),
            Constants.NUMBER_FORMAT.format(bankersRound(totalInterest)),
            Constants.NUMBER_FORMAT.format(bankersRound(totalRepayment)),
            Constants.NUMBER_FORMAT.format(bankersRound(totalInterest + totalRepayment))
        );
        // printf() finished

        // should return "201 CREATED" as it is POST
        return ResponseEntity.ok(Map.of("repayment-plan", Map.of("payments", result.entrySet().stream().toList())));
    }

    /**
     * Round given double value to {@link RoundingMode}.HALF_EVEN, also called "bankers round".
     * @param value
     * @return
     */
    private double bankersRound(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }

}