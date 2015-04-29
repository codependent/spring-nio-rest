# spring-nio-rest
Spring Boot Project showing how efficient nio REST services can be, instead of blocking REST services.

## Considerations
* Load tests have been performed using [loadtest](https://github.com/alexfernandez/loadtest). 
* The business operation has been set up so that it takes 500msec to process.

## Results

### Synchronous controller

The response time degrades as the test runs. 99% of the requests end up taking up to 25 seconds after one minute, with increasing latency and server errors (1 out of 4 requests).

    >>loadtest -c 15 -t 60 --rps 700 http://localhost:8080/sync/data    
    ...
    Requests: 7683, requests per second: 400, mean latency: 7420 ms
    Requests: 9683, requests per second: 400, mean latency: 9570 ms
    Requests: 11680, requests per second: 399, mean latency: 11720 ms
    Requests: 13699, requests per second: 404, mean latency: 13760 ms
    ...
    Percentage of the requests served within a certain time
      50%      8868 ms
      90%      22434 ms
      95%      24103 ms
      99%      25351 ms
     100%      26055 ms (longest request)
    
     100%      26055 ms (longest request)
    
       -1:   7559 errors
    Requests: 31193, requests per second: 689, mean latency: 14350 ms
    Errors: 1534, accumulated errors: 7559, 24.2% of total requests

### Asynchronous controller

It is able to process up to the rps setup limit (700), with no errors and having the response time only limited by the business service's processing time: 

    >>loadtest -c 15 -t 60 --rps 700 http://localhost:8080/async/data    
    ...
    Requests: 0, requests per second: 0, mean latency: 0 ms
    Requests: 2839, requests per second: 568, mean latency: 500 ms
    Requests: 6337, requests per second: 700, mean latency: 500 ms
    Requests: 9836, requests per second: 700, mean latency: 500 ms
    Requests: 13334, requests per second: 700, mean latency: 500 ms
    Requests: 16838, requests per second: 701, mean latency: 500 ms
    Requests: 20336, requests per second: 700, mean latency: 500 ms
    Requests: 23834, requests per second: 700, mean latency: 500 ms
    Requests: 27338, requests per second: 701, mean latency: 500 ms
    Requests: 30836, requests per second: 700, mean latency: 500 ms
    Requests: 34334, requests per second: 700, mean latency: 500 ms
    Requests: 37834, requests per second: 700, mean latency: 500 ms
    Requests: 41336, requests per second: 700, mean latency: 500 ms

    Target URL:          http://localhost:8080/async/data
    Max time (s):        60
    Concurrency level:   15
    Agent:               none
    Requests per second: 700

    Completed requests:  41337
    Total errors:        0
    Total time:          60.002348360999996 s
    Requests per second: 689
    Total time:          60.002348360999996 s

    Percentage of the requests served within a certain time
      50%      503 ms
      90%      506 ms
      95%      507 ms
      99%      512 ms
     100%      527 ms (longest request)
