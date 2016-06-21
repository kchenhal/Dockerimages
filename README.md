# Run pyspark with mesos
  see link http://spark.apache.org/docs/latest/running-on-mesos.html#connecting-spark-to-mesos
  >**Notes:**
  
  >- Need install mesos on the spark driver machine when running in client mode, so that pyspark can find native mesos library
  -      https://mesosphere.com/downloads/
  >- Need set, so that it can upload  the spark to mesos slave
  -     export SPARK_EXECUTOR_URI=http://apache.cs.utah.edu/spark/spark-1.6.1/spark-1.6.1-bin-hadoop2.6.tgz  
  
# Mesos slave, set port ranges
**Command Line:**
  >- --resources=ports:[80-32000]
  
  in the slave machine, got to /etc/mesos-slave directory, create a file that corresponding to the mesos-slave command line argument. in this case, create a file called 
  
    'resources' 
    
  the content of the file will be: 
  
    'ports:[80-32000]' 
>
  - [ec2-user@ip-172-16-22-29 mesos-slave]$ pwd
    /etc/mesos-slave
  - [ec2-user@ip-172-16-22-29 mesos-slave]$ ls
    containerizers  docker_socket  executor_environment_variables  executor_registration_timeout  hostname  resources

- https://open.mesosphere.com/reference/mesos-slave/
  
**Json format**
  >- resources {
  name: "ports"
  type: RANGES
  ranges {
    range {
      begin: 80
      end: 32000
    }
  }
  role: "*"
}

  
