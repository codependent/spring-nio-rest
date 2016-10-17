# spring-nio-rest

[![Build Status](https://travis-ci.org/codependent/spring-nio-rest.svg?branch=master)](https://travis-ci.org/codependent/spring-nio-rest)

Spring Boot Project showing how efficient nio REST services (Callable & RxJava's Observable) can be, instead of blocking REST services.

Swagger documentation available at path `/v2/api-docs`.

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

### Asynchronous controller with Callable

It is able to process up to the rps setup limit (700), with no errors and having the response time only limited by the business service's processing time: 

    >>loadtest -c 15 -t 60 --rps 700 http://localhost:8080/callable/data    
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

Pushing it to its limits it manages to cope with up to an impressive 1700 rps (99996 requests in a minute) without errors:

    >>loadtest -c 15 --rps 1700 -t 60 http://localhost:8080/async/data
    Requests: 0, requests per second: 0, mean latency: 0 ms
    Requests: 6673, requests per second: 1329, mean latency: 590 ms
    Requests: 15197, requests per second: 1706, mean latency: 650 ms
    Requests: 23692, requests per second: 1702, mean latency: 610 ms
    Requests: 31950, requests per second: 1653, mean latency: 640 ms
    Requests: 40727, requests per second: 1757, mean latency: 620 ms
    Requests: 49139, requests per second: 1684, mean latency: 600 ms
    Requests: 57655, requests per second: 1701, mean latency: 660 ms
    Requests: 66197, requests per second: 1710, mean latency: 610 ms
    Requests: 74748, requests per second: 1707, mean latency: 610 ms
    Requests: 83111, requests per second: 1677, mean latency: 630 ms
    Requests: 91410, requests per second: 1658, mean latency: 690 ms

    Target URL:          http://localhost:8080/async/data
    Max time (s):        60
    Concurrency level:   15
    Agent:               none
    Requests per second: 1700

    Completed requests:  99996
    Total errors:        0
    Total time:          60.000301544 s
    Requests per second: 1667
    Total time:          60.000301544 s

    Percentage of the requests served within a certain time
      50%      625 ms
      90%      728 ms
      95%      761 ms
      99%      821 ms
     100%      1286 ms (longest request)
    Requests: 99996, requests per second: 1713, mean latency: 750 ms
    
### Asynchronous controller with RxJava's Observable

It's performance is slightly better than the Callable version

    >>loadtest -c 15 -t 60 --rps 1700 http://localhost:8080/observable/data
    Requests: 0, requests per second: 0, mean latency: 0 ms
    Requests: 6707, requests per second: 1341, mean latency: 560 ms
    Requests: 15070, requests per second: 1675, mean latency: 560 ms
    Requests: 23658, requests per second: 1716, mean latency: 630 ms
    Requests: 31620, requests per second: 1594, mean latency: 570 ms
    Requests: 40698, requests per second: 1816, mean latency: 730 ms
    Requests: 49173, requests per second: 1695, mean latency: 560 ms
    ...
    Completed requests:  100088
    Total errors:        0
    Total time:          60.000421678 s
    Requests per second: 1668
    Total time:          60.000421678 s

