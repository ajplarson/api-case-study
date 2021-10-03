# API-case-study
This application is a Spring Boot API that has 3 main endpoints:
* /api/product (GET)
* /api/product/{productId} (PUT)
* /api/price (POST)

#Running the API
Currently, the application is configured to run locally, using a local instance of MongoDB.

Some useful Gradle tasks are the following:
* ./gradlew test (automatically runs jacocoReport) - runs unit tests and generates a code coverage report in the build directory
* ./gradlew bootJar - assembles a "fat jar" (contains all necessary dependencies) to run the application
* ./gradlew bootRun - runs the application with the local profile specified in the app.yml 

#Application Information
Product objects are populated by gathering specific product information from an external API. This API does not include information about pricing; prices are created and read from an instance of MongoDB. The application combines these two data sources in order to provide a relevant product via the GET endpoint above. In addition, the price of a specific product can be 

##Areas for Improvement
* the endpoints do not have any authentication
* the Spring Web Client methods that hit the external API lack Unit Tests
* the application has not run through any sort of CI workflow/pipeline, so some inconsistencies in environment and static analysis issues may exist
* the IDs for the price object match the ID of the product. a better scheme could be present here
* the application can only run locally
