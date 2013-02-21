package org.apache.giraph.examples.compensations;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.giraph.graph.BasicVertex;
import org.apache.giraph.graph.BspUtils;
import org.apache.giraph.graph.VertexReader;
import org.apache.giraph.lib.TextVertexInputFormat;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

public class PageRankInputFormat extends
    TextVertexInputFormat<LongWritable, DoubleWritable, FloatWritable, DoubleWritable> {

  @Override
  public VertexReader<LongWritable, DoubleWritable, FloatWritable, DoubleWritable> createVertexReader(InputSplit split,
      TaskAttemptContext context) throws IOException {
    return new PageRankVertexReader(textInputFormat.createRecordReader(split, context));
  }

  public static class PageRankVertexReader extends
      TextVertexInputFormat.TextVertexReader<LongWritable, DoubleWritable, FloatWritable, DoubleWritable> {

    private static final Pattern SEPARATOR = Pattern.compile("[\t ]");

    public PageRankVertexReader(RecordReader<LongWritable, Text> lineReader) {
      super(lineReader);
    }

    @Override
    public BasicVertex<LongWritable, DoubleWritable, FloatWritable, DoubleWritable> getCurrentVertex()
        throws IOException, InterruptedException {
      BasicVertex<LongWritable, DoubleWritable, FloatWritable, DoubleWritable>
          vertex = BspUtils.<LongWritable, DoubleWritable, FloatWritable, DoubleWritable>createVertex(
          getContext().getConfiguration());

      String[] tokens = SEPARATOR.split(getRecordReader().getCurrentValue().toString());
      Map<LongWritable, FloatWritable> edges = Maps.newHashMapWithExpectedSize(tokens.length - 1);
      for (int n = 1; n < tokens.length; n++) {
        edges.put(new LongWritable(Integer.parseInt(tokens[n])), new FloatWritable(0));
      }

      LongWritable vertexId = new LongWritable(Integer.parseInt(tokens[0]));
      vertex.initialize(vertexId, new DoubleWritable(0), edges, Lists.<DoubleWritable>newArrayList());

      return vertex;
    }

    @Override
    public boolean nextVertex() throws IOException, InterruptedException {
      return getRecordReader().nextKeyValue();
    }
  }

}
