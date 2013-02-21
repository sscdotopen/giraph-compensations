package org.apache.giraph.examples.compensations;

import org.apache.giraph.examples.LongSumAggregator;
import org.apache.giraph.examples.SumAggregator;
import org.apache.giraph.graph.WorkerContext;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.log4j.Logger;

public class CompensablePageRankWorkerContext extends WorkerContext {

  static double DANGLING_RANK = 0;

  static double AGGREGATED_L1_DIFF = 0;
  static long AGGREGATED_NON_FAILED_VERTICES = 0l;
  static double AGGREGATED_NON_FAILED_RANK = 0;

  private static final Logger LOG = Logger.getLogger(CompensablePageRankWorkerContext.class);

  @Override
  public void preApplication() throws InstantiationException, IllegalAccessException {
    registerAggregator(CompensablePageRankVertex.DANGLING_RANK, SumAggregator.class);
    registerAggregator(CompensablePageRankVertex.L1_DIFF, SumAggregator.class);
    registerAggregator(CompensablePageRankVertex.NON_FAILED_VERTICES, LongSumAggregator.class);
    registerAggregator(CompensablePageRankVertex.NON_FAILED_RANK, SumAggregator.class);
  }

  @Override
  public void preSuperstep() {

    SumAggregator danglingRank = (SumAggregator) getAggregator(CompensablePageRankVertex.DANGLING_RANK);
    SumAggregator l1Diff = (SumAggregator) getAggregator(CompensablePageRankVertex.L1_DIFF);
    LongSumAggregator nonFailedVertices =
        (LongSumAggregator) getAggregator(CompensablePageRankVertex.NON_FAILED_VERTICES);
    SumAggregator nonFailedRank = (SumAggregator) getAggregator(CompensablePageRankVertex.NON_FAILED_RANK);

    DANGLING_RANK = danglingRank.getAggregatedValue().get();
    AGGREGATED_L1_DIFF = l1Diff.getAggregatedValue().get();
    AGGREGATED_NON_FAILED_VERTICES = nonFailedVertices.getAggregatedValue().get();
    AGGREGATED_NON_FAILED_RANK = nonFailedRank.getAggregatedValue().get();

    LOG.info("Superstep [" + getSuperstep() + "] L1-diff [" + AGGREGATED_L1_DIFF + "], danglingRank [" +
        DANGLING_RANK + "], nonFailedVertices [" + AGGREGATED_NON_FAILED_VERTICES + "], numVertices [" +
        getNumVertices() + "], numEdges + [" + getNumEdges() + "], nonfailedRank [" +
        AGGREGATED_NON_FAILED_RANK + "]");

    useAggregator(CompensablePageRankVertex.DANGLING_RANK);
    useAggregator(CompensablePageRankVertex.L1_DIFF);
    useAggregator(CompensablePageRankVertex.NON_FAILED_VERTICES);
    useAggregator(CompensablePageRankVertex.NON_FAILED_RANK);

    danglingRank.setAggregatedValue(new DoubleWritable(0d));
    l1Diff.setAggregatedValue(new DoubleWritable(0d));
    nonFailedVertices.setAggregatedValue(new LongWritable(0l));
    nonFailedRank.setAggregatedValue(new DoubleWritable(0d));
  }

  @Override
  public void postSuperstep() {}

  @Override
  public void postApplication() {}


}
