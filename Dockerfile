FROM airhacks/tomee
ENV WAR kafka-concurrency-utils.war
COPY target/${WAR} ${DEPLOYMENT_DIR}