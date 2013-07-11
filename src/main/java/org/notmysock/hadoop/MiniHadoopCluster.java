package org.notmysock.hadoop;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.hdfs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapreduce.*;

import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.mapred.*;
import java.io.*;

public class MiniHadoopCluster {
  
  private final Configuration conf = new Configuration();

  private MiniDFSCluster miniDFSCluster;
  private MiniMRClientCluster miniMRCluster;

  public MiniHadoopCluster() {
  }

  public void start() throws IOException {
    this.start(1,1);
  }

  public void start(int datanodes, int tasknodes ) throws IOException {

    conf.setInt("dfs.replication", (datanodes > 3) ? 3 : datanodes);
    conf.set("mapred.framework.name", "yarn");
    conf.setBoolean("yarn.minicluster.fixed.ports", true);
    conf.setBoolean("mapreduce.jobhistory.minicluster.fixed.ports", true);
    conf.set("yarn.scheduler.capacity.root.queues", "default");
    conf.setInt("yarn.scheduler.capacity.root.capacity", 100);
    conf.setInt("yarn.scheduler.capacity.root.default.capacity", 100);
    conf.setInt("yarn.nodemanager.resource.memory-mb", 4096);
    conf.setInt("yarn.scheduler.minimum-allocation-mb", 512);
    conf.setInt("yarn.nodemanager.delete.debug-delay-sec", 360);

    miniDFSCluster = new MiniDFSCluster.Builder(conf)
          .nameNodePort(56565)
          .numDataNodes(datanodes)
          .build();

    miniMRCluster = MiniMRClientClusterFactory.create(this.getClass(), 1, conf);
  }

  public void stop() {
    try {
      miniMRCluster.stop();
    } catch(IOException e) {
    } finally {
      miniDFSCluster.shutdown();
    }
  }

  public Configuration getConfiguration() { return conf; }
  public MiniDFSCluster getMiniDFSCluster() { return miniDFSCluster; }
  public MiniMRClientCluster getMiniMRCluster() { return miniMRCluster; }

  public static MiniHadoopCluster cluster = null;

  public static void main(String[] args) throws Exception {
    cluster = new MiniHadoopCluster();
    cluster.start();

    System.out.println("Press ^C to exit");

    Runtime.getRuntime().addShutdownHook(new Thread() {
        public void run() { MiniHadoopCluster.cluster.stop(); }
    });
  }
}
