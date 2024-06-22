# Repayment Plan Service

This is a simple webapp to create an annuity based repayment plan.

OpenAPI: http://localhost:8080/v3/api-docs

Repayment plan is returned as JSON from REST endpoint and pretty printed to console, e.g.:
```
           Datum |    Restschuld |      Zinsen | Tilgung(+)/Ausz.(-) |       Rate
----------------------------------------------------------------------------------
      2016-11-30 | -100.000,00 € |      0,00 € |       -100.000,00 € | -100.000,00 €
----------------------------------------------------------------------------------
      2015-12-31 |  -99.833,34 € |    176,67 € |            166,66 € |   343,33 €
      2016-01-31 |  -99.666,38 € |    176,37 € |            166,96 € |   343,33 €
      2016-02-29 |  -99.499,13 € |    176,08 € |            167,25 € |   343,33 €
      2016-03-31 |  -99.331,58 € |    175,78 € |            167,55 € |   343,33 €
      2016-04-30 |  -99.163,74 € |    175,49 € |            167,84 € |   343,33 €
      2016-05-31 |  -98.995,60 € |    175,19 € |            168,14 € |   343,33 €
      2016-06-30 |  -98.827,16 € |    174,89 € |            168,44 € |   343,33 €
      2016-07-31 |  -98.658,42 € |    174,59 € |            168,74 € |   343,33 €
      2016-08-31 |  -98.489,39 € |    174,30 € |            169,03 € |   343,33 €
      2016-09-30 |  -98.320,06 € |    174,00 € |            169,33 € |   343,33 €
      2016-10-31 |  -98.150,43 € |    173,70 € |            169,63 € |   343,33 €
      2016-11-30 |  -97.980,50 € |    173,40 € |            169,93 € |   343,33 €
----------------------------------------------------------------------------------
Zinsbindungsende |  -97.980,50 € |  2.100,46 € |          2.019,50 € | 4.119,96 €
```

No database layer; tested with hurl (https://hurl.dev/) - a file based API endpoint testing tool.

Debian: ``apt-get install hurl``

Mac: ``brew install hurl``

Usage:
``hurl --test -v repayment-plan.hurl``

Output:
```
> hurl --test src/main/resources/repayment-plan.hurl -v
src/main/resources/repayment-plan.hurl: Running [1/1]
* ------------------------------------------------------------------------------
* Executing entry 1
*
* Cookie store:
*
* Request:
* POST http://localhost:8080/api/v1/repayment-plan?loanAmount=100000&annualInterestRate=2.12&repaymentRate=2.0&fixedInterestDuration=1
*
* Request can be run with the following curl command:
* curl --request POST 'http://localhost:8080/api/v1/repayment-plan?loanAmount=100000&annualInterestRate=2.12&repaymentRate=2.0&fixedInterestDuration=1'
*
> POST /api/v1/repayment-plan?loanAmount=100000&annualInterestRate=2.12&repaymentRate=2.0&fixedInterestDuration=1 HTTP/1.1
> Host: localhost:8080
> Accept: */*
> User-Agent: hurl/4.3.0
>
* Response: (received 1545 bytes in 16 ms)
*
< HTTP/1.1 200
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Sat, 22 Jun 2024 11:05:58 GMT
<
*
src/main/resources/repayment-plan.hurl: Success (1 request(s) in 19 ms)
--------------------------------------------------------------------------------
Executed files:  1
Succeeded files: 1 (100.0%)
Failed files:    0 (0.0%)
Duration:        21 ms
```