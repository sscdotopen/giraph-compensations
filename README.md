Compensating PageRank in Giraph
===============================

In this repository, we provide code and instructions to repeat the experiments regarding the compensation of failures during the computation of PageRank in the [Apache Giraph](http://giraph.apache.org) system.

We modified Giraph to simulate failures and apply our proposed compensation function. We made some minor changes to the system and disabled checkpointing. The source code for the compensable PageRank can be found in the [CompensablePageRankVertex](https://github.com/sscdotopen/giraph-compensations/blob/compensable/src/main/java/org/apache/giraph/examples/compensations/CompensablePageRankVertex.java) class.

In order to repeat our experiments, you first need to download the *Slashdot-Zoo* dataset from [http://konect.uni-koblenz.de/networks/slashdot-zoo](http://konect.uni-koblenz.de/networks/slashdot-zoo), provided through the Koblenz Network Collection.

The dataset has to be converted to Giraph's input format for using a [python script](https://github.com/sscdotopen/giraph-compensations/blob/compensable/toadjacency.py), which we provide here.

`python toadjacency.py graph.csv > adjacency.csv `

Additionally, a working Hadoop installation is needed (pseudo-distributed Hadoop on a single machine is sufficient for this experiment).

The dataset must be copied to HDFS and the code from this repository must be built via maven.

Finally, the experiment can be run like this:

    bin/hadoop jar giraph-0.1-jar-with-dependencies.jar 
    org.apache.giraph.GiraphRunner org.apache.giraph.examples.compensations.CompensablePageRankVertex 
    --workers 15
    --inputFormat org.apache.giraph.examples.compensations.PageRankInputFormat
    --inputPath hdfs:///path/to/adjacency.csv 
    --outputFormat org.apache.giraph.examples.compensations.PageRankOutputFormat 
    --outputPath hdfs:///some/tmp/dir/ 
    --workerContext org.apache.giraph.examples.compensations.CompensablePageRankWorkerContext

After the job has finished, grep for *CompensablePageRankWorkerContext* in the log files to obtain statistics about the convergence behavior during the execution.
