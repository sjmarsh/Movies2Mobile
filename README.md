# Movies2Mobile
Android app for Movies2 movie collection.  Initially this is just a proof of concept exploring how to build an app with Kotlin.  Currently the data source for movies is a json file that has been exported from a separate movie collection desktop application.

## TODO
- ~~Replace simple search text box and button with a propper Android app bar, eg. https://developer.android.com/training/appbar~~
- ~~Combine Movies and Concerts into single view.~~
  - ~~Add filtering functionality (filter would include movie categories as well as concerts)~~
  - ~~Some changes would be required to the export json file format of the Movies desktop (web) application~~
- ~~Add Actors view~~
- ~~Fix dark mode / power-save mode theme for search result card~~
- ~~Improve UX around applying filter.  Filter icon should indicate if filter is applied (or  not)~~
- ~~Data loading/searching should be asynchronous~~
- ~~Add ability to hyperlink between movies and actors~~
- Add ability to get movie data from a web service (instead of or as well as json file in file system).  
- Add unit tests - explore how to do this with Kotlin. Project template includes some samples.
- Improve architecture to make better use of View Models for view logic.  This should help with unit tests.
- Add a demo mode to be used when you don't have a json file exported from the movies desktop application
- ~~Update icon to suit Android 11~~
 
