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
