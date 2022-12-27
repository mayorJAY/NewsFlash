<p align="center">
  <img src="app_icon.png" title="App Logo">
</p>

# NewsFlash

NewsFlash is a News reading Android Application that fetches and displays a list of news headlines published by a single source. The App fetches the data from [News API](https://newsapi.org/). It supports multiple flavors, each flavor represents a news source, building each flavor displays the news from the configured source, this means more flavors can be added easily. It also performs Fingerprint Login when run on devices that support that feature. It is implemented using Multi-flavor, Clean Architecture, Model-View-ViewModel (MVVM) pattern and uses Modern Android Development pattern and libraries. Adequate Unit Tests were also implemented in the codebase.

Get the [APK here](https://drive.google.com/file/d/1SIrvEX7bfnJOqHvqFMT5NNYssqN-8iRq/view?usp=share_link)

## Project Characteristics

This application has the following characteristics:
* 100% Kotlin
* Modern Architecture (Clean Architecture, Model-View-ViewModel)
* [Android Jetpack Components](https://developer.android.com/jetpack)
* [Material Design](https://material.io/develop/android/docs/getting-started)

## Libraries Used

Minimum API level is set to 21, this means NewsFlash can run on approximately 98% of Android devices
* [Biometric Library](https://developer.android.com/jetpack/androidx/releases/biometric) which is a library for authenticating users with biometrics or device credentials
* [Retrofit](https://square.github.io/retrofit/) which is a type-safe REST client for Android which makes it easier to consume RESTful web services
* [Moshi](https://github.com/square/moshi), a modern JSON library for Android, Java and Kotlin
* [OkHttp Logging Interceptor](https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor), an OkHttp interceptor which logs HTTP request and response data
* [Kotlin Coroutines](https://developer.android.com/kotlin/coroutines) used to perform asynchronous network calls to the remote server
* [Paging Library](https://developer.android.com/topic/libraries/architecture/paging) which helps to load and display small chunks of data at a time
* [Glide](https://github.com/bumptech/glide) which is an image loading and caching library for Android
* [Kotlin flow](https://developer.android.com/kotlin/flow) for emitting live updates from a network call sequentially
* [Hilt](https://dagger.dev/hilt/), a DI library for Android that reduces the boilerplate of using manual DI
* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) used to store and manage UI-related data in a lifecycle conscious way
* [View Binding](https://developer.android.com/topic/libraries/view-binding) used to easily write code that interacts with views by referencing them directly
* [Material Design](https://material.io/develop/android/docs/getting-started/) which is an adaptable system that guides in maintaining principles and best practices of contemporary UI
* [Browser Library](https://developer.android.com/jetpack/androidx/releases/browser), a library that helps display webpages in the user's default browser (Chrome Custom Tabs)
* [Timber](https://github.com/JakeWharton/timber), a utility library for logging and easy debugging
* [SDP/SSP](https://github.com/intuit/sdp) which is a scalable size unit that scales with the screen size. It helps to easily design for multiple screens
* [JUnit4](https://junit.org/junit4), a testing framework used for writing unit tests
* [MockWebServer](https://javadoc.io/doc/com.squareup.okhttp3/mockwebserver/3.14.9/overview-summary.html), a library that makes it easy to test how Apps behave when making HTTP/HTTPS calls
* [Mockito](https://site.mockito.org/), a mocking framework for writing unit tests

## Installation

You will need an API key from [News API](https://newsapi.org/). In your project's root directory, inside the `local.properties` file include the following line:

````
api.key="YOUR_API_KEY"
````