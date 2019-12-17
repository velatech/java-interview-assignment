## Java Interview Exercise
We want to give our customer the best experience possible. So we should inform
 them about the details of his/her card like:
* valid/not valid
* the scheme (i.e VISA, MASTERCARD or AMEX)
* the bank when it is available

In order to do so, you should create a simple Spring REST api service that will
understand the following call and response structure:   
    
     
     GET /card-scheme/verify/234564562....
     {
       "success": true
       "payload" : {
            "scheme": "visa",
            "type" : "debit",
            "bank" : "UBS"
     }    
    }
    # Number of hits
    GET /card-scheme/stats?start=1&limit=3
    {
      "success": true
      "start": 1,
      "limit": 3,
      "size": 133,
      "payload": {
        "545423": 5,
        "679234": 4,
        "329802": 1
      }
    }

To gather this data, you should use a 3rd party API, such as https://binlist.net/
#### Topics to take in account
- Try to show your OOP skills
- Database modelization, using JPA or ORM
- Code standards/clean code
- Software patterns
- Performance and number of requests to the 3rd Party API
- Unit tests.   
- Create a simple UI for the endpoints and display the output properly.

Create a repository on github for code and make frequent commit as you develop your API.  
##### Goodluck  

#### Deployment Link
The link to the user interface where these functionalities
are to be test can be found on this [here](https://pedantic-archimedes-cb42ab.netlify.com).

The github repository for the React App can be found [here](https://github.com/phayo/card-scheme-client).

NB: The client is a simple react app deployed on netlify that consumes
the Spring API and provides live demo of the REST API.

The app can be cloned ran locally and tested with postman too.

Example request.
- localhost:8080/api/v1/card-scheme/verify/5399831685516243

response: 
```json
{
    "success": true,
    "payload": {
        "scheme": "mastercard",
        "type": "debit",
        "bank": "GTBANK"
    }
}
```

- localhost:8080/api/v1/card-scheme/verify/5399
```json
{
    "success": false,
    "payload": null
}

```

- localhost:8080/api/v1/card-scheme/stats?start=1&limit=3
```json
{
    "success": true,
    "start": Number,
    "limit": Number,
    "size": Number,
    "payload": Object (or null)
}
```


Notes:

Thank you for reviewing my application.

###### Chukwuebuka Anazodo