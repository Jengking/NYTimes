## NY Times Api Demo App
This app is to demonstrate New York Times Most Popular and Search API. The app is built around MVVM design pattern and consist of 3 pages, Landing, Listing and Search Page.

## Setup
After checking out the project, datakey.properties file need to be created at project root directory. The file contents need to be BASE_URL = "https://api.nytimes.com/svc/" 
and API_KEY = "Your Own Api key". The api key can be obtain at https://developer.nytimes.com/ upon signing up.

### Landing Page
- to show heterogeneous layout view
- use NY Times most popular api such as Most Viewed, Most Shared and Most Emailed for a period of 1 day

### Listing Page
- use NY Times most popular api such as Most Viewed, Most Shared and Most Emailed for a period of 30 days

### Search Page
- use NY Times Search Api
