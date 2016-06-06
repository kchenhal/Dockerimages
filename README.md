# Run pyspark with mesos
  see link http://spark.apache.org/docs/latest/running-on-mesos.html#connecting-spark-to-mesos
  >**Notes:**
  
  >- Need install mesos on the spark driver machine when running in client mode, so that pyspark can find native mesos library
  -      https://mesosphere.com/downloads/
  >- Need set, so that it can upload  the spark to mesos slave
  -     export SPARK_EXECUTOR_URI=http://apache.cs.utah.edu/spark/spark-1.6.1/spark-1.6.1-bin-hadoop2.6.tgz  
  
