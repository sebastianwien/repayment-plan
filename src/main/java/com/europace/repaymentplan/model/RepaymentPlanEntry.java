package com.europace.repaymentplan.model;

import com.europace.repaymentplan.util.Constants;
import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Data
public class RepaymentPlanEntry {

    private double residualDebt, interest, repayment, loanRate;

    @JsonGetter("residualDebt")
    public String getResidualDebtFormatted() {
        return Constants.NUMBER_FORMAT.format(residualDebt);
    }

    @JsonGetter("interest")
    public String getInterestFormatted() {
        return Constants.NUMBER_FORMAT.format(interest);
    }

    @JsonGetter("repayment")
    public String getRepaymentFormatted() {
        return Constants.NUMBER_FORMAT.format(repayment);
    }

    @JsonGetter("loanRate")
    public String getLoanRateFormatted() {
        return Constants.NUMBER_FORMAT.format(loanRate);
    }
}
