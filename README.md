Check [the blog](https://abhirockzz.wordpress.com/2017/05/30/kafeene-2-kafka-concurrency-utilities) for more details

## Start with Docker Compose

- `git clone https://github.com/abhirockzz/kafka-javaee-concurrency-utilities.git`
- `mvn clean install` - creates `kafka-concurrency-utils` in `target` directory
- `docker-compose up --build` - starts Kafka, Zookeeper, Payara and Producer containers (you can switch to any other [Java EE runtime](https://github.com/abhirockzz/kafka-javaee-concurrency-utilities/blob/master/Dockerfile#L1))

## Results

![](https://abhirockzz.files.wordpress.com/2017/06/kafka-javaee-conc-utils-snapshot.jpg)

The results you'll see might be similar to this. Here is the gist. The above snapshot shows data from three (consumer) poll loops

- Producer pushed one record
- Each loop fetched 1 record (that's just co-incidence, not by design) and its completely independent of the producer
- After each loop (task), the offset commit process got triggered - in this example we have 3 partitions and the offset for each partition is printed to the console
- If you notice carefully, the committed offset (for a particular loop) will be in line with the data which was consumed (note: committed offset points to the next offset i.e. one more than the offset that offset which was last consumed)

## bye-bye

`docker-compose down -v` once you're done....


