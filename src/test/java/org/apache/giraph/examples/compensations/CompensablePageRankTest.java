package org.apache.giraph.examples.compensations;

import com.google.common.collect.Maps;
import junit.framework.TestCase;
import org.apache.giraph.utils.InternalVertexRunner;

public class CompensablePageRankTest extends TestCase {

  public void testToyExample() throws Exception {

    String[] graph = new String[] {
        "2 1",
        "5 2 4",
        "3",
        "4 3 2",
        "1 4 2 3"};

    InternalVertexRunner.run(CompensablePageRankVertex.class, null, CompensablePageRankWorkerContext.class,
        PageRankInputFormat.class, PageRankOutputFormat.class, Maps.<String,String>newHashMap(), graph);


  }


}
