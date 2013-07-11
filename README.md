HadoopTestCluster
=================

An easy to way to launch a single JVM cluster.

To use (fill in the appropriate hadoop-2.x version in the pom.xml)

`mvn package`

`export HADOOP_CLASSPATH=target/lib\/\*`
`../bin/hadoop jar target/hadoop-test-cluster-1.0-test.jar`

This can use a freshly downloaded hadoop binary from the corresponding release.

Without the ./bin/hadoop this will be missing MRAppMaster from the classpath & no jobs will complete.
