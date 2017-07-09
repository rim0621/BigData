
package kr.ac.kookmin.cs.bigdata;

import java.io.IOException;
import java.util.Arrays;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.json.JSONException;
import org.json.JSONObject;

public class json extends Configured implements Tool {  
    public static void main(String[] args) throws Exception {
        System.out.println(Arrays.toString(args));
        int res = ToolRunner.run(new Configuration(), new json(), args);
      
        System.exit(res);
    }

    @Override
    public int run(String[] args) throws Exception {
    	System.out.println(Arrays.toString(args));

        Job job = Job.getInstance(getConf());
        job.setJarByClass(json.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);   

        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.waitForCompletion(true);
      
        return 0;
    }
   
    public static class Map extends Mapper<LongWritable, Text, Text, IntWritable>{
        private final static IntWritable ONE = new IntWritable(0);
        private Text word = new Text();

        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{

   // 불필요한 새로운 줄 바꿈        	
            String asin;
            String reviewText;
            String[] text =value.toString().split("$");  // 이건 무슨 의도인지??
             
            try{
                for(int i=0;i<text.length; i++){
                    JSONObject jo = new JSONObject(text[i]);
                  
                    asin = jo.getString("asin");
                    reviewText=jo.getString("summary").toLowerCase();  
                    
                    for(String token:reviewText.toString().split(" "))
                    {
                    	if(token.matches(".*nice*.")||token.matches(".*good*.")||token.matches(".*satisfying*.")||
                    			token.matches(".*great*.")||token.matches(".*cool*.")||token.matches(".*best*.")
                    			||token.matches(".*beautiful*.")||token.matches(".*sweet*.")||token.matches(".*Brilliant*."))
                    	{
                    		ONE.set(1);
                        	context.write(new Text(asin),ONE);
                    	}
                    	else if(token.matches(".*bad*.")||token.matches(".*disappointing*.")||token.matches(".*upset*.")||
                    			token.matches(".*bring*.")||token.matches(".*poisonous*.")||token.matches(".*mad*.")||token.matches(".*poor*.")
                    			||token.matches(".*idiot*.")||token.matches(".* fault*.")||token.matches(".* ignorant*."))
                    	{
                    		ONE.set(-1);
                        	context.write(new Text(asin),ONE);
                        	
                    	}
                    	
                    	
                    }
                  
                    
                    }
                      
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
}
       

    public static class Reduce extends Reducer<Text, IntWritable, Text ,IntWritable>{
   
        public void reduce(Text key,  Iterable<IntWritable> values, Context context) throws IOException, InterruptedException{
         	
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
      
            }
            context.write(key, new IntWritable(sum));
        }
    }
       
}








