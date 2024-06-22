POST http://localhost:8080/api/v1/repayment-plan?loanAmount=100000&annualInterestRate=2.12&repaymentRate=2.0&fixedInterestDuration=1
HTTP 200
[Asserts]
jsonpath "$.repayment-plan" exists
jsonpath "$.repayment-plan.payments" exists
jsonpath "$.repayment-plan.payments" count == 12
jsonpath "$.repayment-plan.payments.[0].2015-12-31" exists
jsonpath "$.repayment-plan.payments.[0].2015-12-31.residualDebt" startsWith "-99.833,34"
jsonpath "$.repayment-plan.payments.[0].2015-12-31.interest"     startsWith "176,67"
jsonpath "$.repayment-plan.payments.[0].2015-12-31.repayment"    startsWith "166,66"
jsonpath "$.repayment-plan.payments.[0].2015-12-31.loanRate"     startsWith "343,33"

jsonpath "$.repayment-plan.payments.[11].2016-11-30" exists
jsonpath "$.repayment-plan.payments.[11].2016-11-30.residualDebt" startsWith "-97.980,50"
jsonpath "$.repayment-plan.payments.[11].2016-11-30.interest"     startsWith "173,40"
jsonpath "$.repayment-plan.payments.[11].2016-11-30.repayment"    startsWith "169,93"
jsonpath "$.repayment-plan.payments.[11].2016-11-30.loanRate"     startsWith "343,33"

## test cases with invalid input for loan data missing