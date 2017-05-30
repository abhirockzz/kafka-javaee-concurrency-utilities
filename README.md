Check [the blog](https://abhirockzz.wordpress.com/2017/05/30/kafeene-2-kafka-concurrency-utilities) for more details

## Start with Docker Compose

- `git clone https://github.com/abhirockzz/kafka-javaee-concurrency-utilities.git`
- `mvn clean install` - creates `kafka-concurrency-utils` in `target` directory
- `docker-compose up --build` - starts Kafka, Zookeeper, Payara and Producer containers (you can switch to any other [Java EE runtime](https://github.com/abhirockzz/kafka-javaee-concurrency-utilities/blob/master/Dockerfile#L1))

Wait for the containers to start before you move to the testing part...

## Test

- `docker-compose down -v` once you're done....
