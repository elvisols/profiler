### What else we would love to know
> What are the assumptions that you made during the implementation?
- Application will be deployed on a single instance
- The following ports are available
  - 2181
  - 9092
  - 9000
  - 9200
  - 9300
  - 5601
  - 8999 
 - Service caller has access to the user's unique id to query.

> What are the performance characteristics of your implementation?
- This application will really slow down (worse till run into an OutOfMemoryException :( - system crash!) when barrage of data are dropped in the `inputsource` folder - 
because the application is deployed on a single instance - that will inhibit scalability.

> If you could load test it, what do you expect to see in the result?
- It will pass for few hundred thousands of data records.
- It will fail when more data is loaded into the system.

> If you had more time, how would you improve your solution?
- Real-time chunk: I will window the insights request i.e I will break it down and expose the requested insights via a windowed frame, say, for the last  hour(s))|day(s) what are the top amenities clicked or hotels visited, without querying the (ElasticSearch) sink, but using Kafka stores.
- Threads: I will try to distribute by data loading (on my camel route) to use all available processor on my machine.

### Bonus

> What other user insights could we possibly generate from this data?
- Top-M Hotel regions clicked|visited. This was actually captured|exposed in the service. Please see documentation `./target/generated-docs/index.html` for details.

> If you had to update the data source in real time, how would your solution change?
- Nothing changes! The `inputsource` folder is a real-time file listener for data inputs. Once a file is drop Apache Camel bot swings into action and pull these files for processing and delete|move them when done.

> What comments would you expect when this goes to a code review?
- Why did I have my data source `SearchResponse` in my service? - This should be coming from my Repo but I favoured configuration here over convention.
- And more as deemed fit. I love guys that quiz :)
