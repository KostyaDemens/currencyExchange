# currency-exchange-api 💸

## Description
`currency-exchange-api` is a Java Servlet application that implements a REST API for currency exchange. Its main functions include currency management, exchange rate establishment, and calculation of exchange amounts using direct, reverse, and cross rates.

## Technologies ⚙
The project is developed using Jakarta Servlets and is supported on Tomcat 10 server.

## API features 🧰
### Currencies
**GET** `/currencies`

Get list of currencies
```
[
    {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },   
    {
        "id": 0,
        "name": "Euro",
        "code": "EUR",
        "sign": "€"
    }
]
```

**GET** `/currency/EUR`

Get currency by code
```
{
    "id": 0,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
}
```

**POST** `/currencies`

Add new currency
```
{
    "id": 0,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
}
```


### Exchange rates
**GET** `/exchangeRates`

Get list of exchange rates
```
[
  {
    "id": 0,
    "name": "United States dollar",
    "code": "USD",
    "sign": "$"
  },
  {
    "id": 1,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
  },
  "..."
]
```

**GET** `/exchangeRate/USDRUB`

Get exchange rate by code
```
{
    "id": 0,
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 1,
        "name": "Euro",
        "code": "EUR",
        "sign": "€"
    },
    "rate": 0.99
}
```

**POST** `/exchangeRates`

Add new exchange rate
```
{
    "id": 0,
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 1,
        "name": "Euro",
        "code": "EUR",
        "sign": "€"
    },
    "rate": 0.99
}
```
**PATCH** `/exchangeRate/USDRUB`

Change exchange rate
```
{
    "id": 0,
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 1,
        "name": "Euro",
        "code": "EUR",
        "sign": "€"
    },
    "rate": 0.99
}
```

### Currency exchange
**GET** `/exchange?from=BASE_CURRENCY_CODE&to=TARGET_CURRENCY_CODE&amount=$AMOUNT`

Calculation of the transfer of a certain amount of funds from one currency to another. Example request - **GET** `/exchange?from=USD&to=AUD&amount=10`

```{
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 1,
        "name": "Australian dollar",
        "code": "AUD",
        "sign": "A€"
    },
    "rate": 1.45,
    "amount": 10.00,
    "convertedAmount": 14.50
}
```

To obtain the exchange rate, there are three possible scenarios when performing a currency conversion from currency **A** to currency **B**:

1. If the ExchangeRates table contains the currency pair **AB**, we take its exchange rate.
2. If the ExchangeRates table contains the currency pair **BA**, we take its exchange rate and calculate the reciprocal to obtain **AB**.
3. If the ExchangeRates table contains the currency pairs **USD-A** and **USD-B**, we compute the exchange rate **AB** based on these rates.

