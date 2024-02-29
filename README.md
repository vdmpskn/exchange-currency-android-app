# Currency Converter

Currency Converter is an Android app that allows users to exchange currencies using the latest rates from a public API.

## Features

- Multi-currency account with a starting balance of 1000 Euros (EUR)
- Ability to convert any currency to any other currency if the rate is provided by the API
- Display of the current balance in each currency after each conversion
- Commission fee of 0.7% for each conversion after the first five free ones
- Simple and user-friendly interface with input, pickers, and submit button

## Installation

To install the app, you need to have Android Studio and an Android emulator or device. You can clone this repository and open it in Android Studio, or download the APK file from the releases page and install it on your device.

## Usage

To use the app, follow these steps:

- Launch the app and see your current balance in Euros and other currencies
- Enter the amount you want to convert in the input field
- Select the currency you want to sell and the currency you want to buy from the pickers
- Click the submit button and see the result of the conversion and the updated balance
- Repeat the process as many times as you want

## API

The app uses the Exchange Rates API to get the latest currency rates. The API is public and does not require authentication. The response example is:

```json
{
"base":"EUR",
"date":"2022-10-06",
    "rates":{
        "AED":4.147043,
        "AFN":118.466773
    }
}
```
## Code quality

The app follows the Google Java Style Guide and uses Checkstyle to enforce it.

## License

The app is licensed under the @author[Vadym Piskun] license.
