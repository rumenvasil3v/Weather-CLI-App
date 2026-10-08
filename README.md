# Weather-CLI-App

Ask for a city, get its weather in your terminal, and choose the units yourself, one property at a time.

Here's roughly what a session asks you:

```
Enter city name: Sofia
Enter for how many days to display weather ahead (1 - 14): 3
Making http get request...

Choose unit for property -> Temperature
You can choose between 'Fahrenheit' or 'Celsius': Celsius

Choose unit for property -> Wind Speed
You can choose between 'Miles Per Hour' or 'Kilometers Per Hour': Kilometers Per Hour
```

The same question comes up for gusts, wind chill, "feels like" and visibility, and then the report is printed: today's conditions with the units you picked, followed by the extra forecast days.

## Things that trip people up

The city name has to be one of the shortcuts the app knows, spelled and capitalised exactly: `NY`, `Guildford`, `BrightonUk`, `BrightonUs`, `Paris`, `Sofia` or `Sydney`. Anything else throws "City not found". Adding a city means adding a line to `mapCitiesToISO()` in `WeatherService`.

Unit answers are case-sensitive too. If you type something it doesn't recognise, that property just isn't shown.

The data comes from the forecast endpoint of [WeatherAPI.com](https://www.weatherapi.com). The key is a constant at the top of `WeatherService.java`, so you'll want your own (they have a free tier) rather than relying on the one in the code. I'd also move it out into an environment variable before the next commit.

## Building

Maven project on Gson 2.8.8 and OkHttp 4.12.0. Import it as a Maven project and run `cli.Main`. The code is split into `cli` (the prompts), `service` (the HTTP call and JSON parsing), `model` (the weather data and unit conversions) and `utils` (the unit chooser).

One known wrinkle: the shortcut-to-full-name map (`NY` to "New York, US") gets built but the request currently sends the shortcut itself, so the place WeatherAPI picks for short names like `NY` is up to its own matching.

MIT licensed.
