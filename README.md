# SimpleMovies
A Simple movies application that is built with MVVM , Dagger2 , LiveData and Room.
## Preview 

<img src="https://github.com/MohNage7/SimpleMovies/blob/master/images/device-2019-10-06-195420.png"  width="241" height="500" /> <img src="https://github.com/MohNage7/SimpleMovies/blob/master/images/device-2019-10-06-195444.png"   width="241" height="500" />
<img src="https://github.com/MohNage7/SimpleMovies/blob/master/images/device-2019-10-06-195336.png"  width="241" height="500" /><img src="https://github.com/MohNage7/SimpleMovies/blob/master/images/device-2019-10-06-221226.png"  width="241" height="500" />
<img src="https://github.com/MohNage7/SimpleMovies/blob/master/images/device-2019-10-06-195524.png"  width="241" height="500" /><img src="https://github.com/MohNage7/SimpleMovies/blob/master/images/device-2019-10-06-195544.png"  width="241" height="500" />
<img src="https://github.com/MohNage7/SimpleMovies/blob/master/images/device-2019-10-06-221246.png"  width="500" height="241" />


### Project Overview
In this project we will consume themoviedb APIs to build a simple app that have the following features

* Fetch Movies by categories (Popular, Top rated and upcoming ). 
* Show every movie details in separate screen
* Cache data offline. 
* Search for movies.

### App architecture 
The following diagram shows how all the modules will interact with one another.

<img src="https://github.com/MohNage7/SimpleMovies/blob/master/images/simple_movies_diagram.png"  width=600 height=524  />

Each component depends only on the component one level below it. For example, activities and fragments depend only on a view model. 
The repository is the only class that depends on multiple other classes; on a persistent data model and a remote backend data source.

## Model
* Model Represents the data and business logic of the app.
* Our bussiness is represented with the following classes (Movie, Category and Video)
* Room db is used to cache the data locally and is our "single source of truth" in providing the data for the view.

## View
* MoviesActivity: Displays list of movies depending on categories ( POPULAR , Top rated , upcoming ) 
* MovieDetailsActivity: Displays movie details and it's vidoes 
* CategoryBottomSheet: Switch between different categories
* CategoryAdapter: inflates and displays filterList that is provided from CategoryBottomSheet
* MoviesAdapter: inflates and displays moviesList that is provided from MoviesActivity's fetch movies by filter result.
* SearchAdapter: inflates and displays moviesList that is provided from MoviesActivity's search result.
* VideoAdapter: inflates and displays videoList that is provided from MovieDetailsActivity.
* OnCategoryClickListener: responsible for interaction between CategoryAdapter and CategoryBottomSheet
* OnCategorySelectedListener: responsible for interaction between CategoryBottomSheet and MoviesActivity
* OnMovieClickListener: responsible for interaction between MoviesAdapter,SearchAdapter and MoviesActivity


## ViewModel
* MoviesViewModel: MoviesActivity listens to movies changes that will happen when MoviesRepository is invoked to fetch the data from Remote service / Local DB
* MovieDetailViewModel: MovieDetailsActivity listens to videos changes that will happen when MovieDetailsRepository is invoked to fetch the data from Remote service / Local DB

## Repository 
* We are using the repository pattern to interact with the remote service and our local db 
* The repository saves results into the database.
* The repository doesn't make unnecessary requests if the data is cached and up to date.
* MoviesRepository: is being used by MoviesViewModel to fetch the list of movies. and also it provides the search logic
* MovieDetailsRepository: is being used by MovieDetailViewModel to provide list of videos for specific movie.

## NetworkBoundResource
A helper class that is used to take the decision of loading the data from the local db or from remote service.

The following diagram shows the decision tree for NetworkBoundResource:


<img src="https://github.com/MohNage7/SimpleMovies/blob/master/images/network-bound-resource.png"  width=400 height=324  />

NetworkBoundResource is using the local db as it's single source of truth.For example It starts by observing the database for list of movies resource.
When the entry is loaded from the database for the first time, NetworkBoundResource checks whether the result is good enough to be dispatched or that it should be re-fetched from the network.

## Database 
* We're using room database which is built over SQLITE as our local db.
* MovieDao: is used to insert/fetch our list of movies
* VideosDao: is used to insert/fetch list of videos for specific movie.
* VideoConverter: converts List<Video> to string to be able to save it in the db. and converts it to List<Video> in fetch process.
* MovieDatabase: is responsible to provide us one and only object to access our db.
* AppExecutors: is used to write and read from db.

## Api and base classes
* ApiResponse: a generic class for handling responses from retrofit 
* RestApiService: provides and end point for our remote service
* BaseActivity: is extended by any activity in the app. it declares unified structure and contains common methods for our activities.
* BaseFragment: the same as BaseActivity but for fragments
* BaseViewHolder: the same as BaseActivity but for ViewHolder.
* DataWrapper: Wrappes our data call back with status and message to be able to handle different status inside views.

## Utils
* Constants: Contains all our final strings in the app. 
* LiveDataCallAdapter and LiveDataCallAdapterFactory: is used to get LiveData as call back from retrofit.
* RefreshRateLimiter: is a factor in refreshing data decision.

## Dependency Injection
 Dependency injection allows classes to define their dependencies without constructing them. 
 At runtime, another class is responsible for providing these dependencies

* For DI we are using dagger2 
* DataModule: define classes and methods which provide dependencies
* DataComponent: is used by Dagger 2 to generate code which uses the modules to fulfill the requested dependencies.

## Questions
* What is the principle of sole responsibility? What's its purpose?
  It states that every object have single responsiblity and that responsibility should be entirly encapsulated by the class. 
  There should never be more than one reason for a class to change. to achive high cohesion and low coupling between modules that make testing , maintainance and readability easier.

* What characteristics does, in your opinion, have a "good" code or clean code?
  - Clean code is simple, direct and well-written code. it should be providing one way rather than many ways of doing one thing.             it makes the code more robust and easy to read, maintain and scale 
characteristics that indicates that we are writting clean code are : 
  - Efficiency  
  - Maintainability 
  - Well Structured 
  - Readable 
  - Reliability 
  - Follows Standards 
  - Extensibility 
   


