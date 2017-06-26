package kr.ac.kookmin.cs.bigdata;

import java.io.IOException;
import java.util.Arrays;

import net.iharder.base64.Base64.InputStream;

import org.apache.commons.io.IOUtils;
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
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.gson.JsonParser;
import com.sun.xml.bind.CycleRecoverable.Context;




public class WordCount extends Configured implements Tool {
    public static void main(String[] args) throws Exception {
        System.out.println(Arrays.toString(args));
        int res = ToolRunner.run(new Configuration(), new WordCount(), args);
      
        System.exit(res);
    }

    @Override
    public int run(String[] args) throws Exception {
        System.out.println(Arrays.toString(args));

        Job job = Job.getInstance(getConf());
        job.setJarByClass(WordCount.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        job.setMapperClass(Map1.class);
        job.setMapperClass(Map1.class);
        job.setReducerClass(Reduce.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        
      //  FileInputFormat.addInputPath(job, new Path(args[0]));
        MultipleInputs.addInputPath(job,new Path(args[0]),TextInputFormat.class,Map1.class );
        MultipleInputs.addInputPath(job,new Path(args[1]),TextInputFormat.class,Map2.class );
        FileOutputFormat.setOutputPath(job, new Path(args[2]));

        job.waitForCompletion(true);
      
        return 0;
    }
   
    public static class Map1 extends Mapper<LongWritable, Text, Text, IntWritable> {
        private static IntWritable ONE = new IntWritable(0);
        private Text word = new Text();

        public void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
  
            String asin;
            String asin2;
            String reviewText;
            String help;
            String text = value.toString();
            String tmp;
            int num=0;
            try {

            JSONObject json = new JSONObject(text);
               asin= json.getString("asin");
               help=json.getString("helpful");
           //    help.toString().split(",");
               for (String token: help.toString().split(",")) {
                  if(token.matches(".*]"))
                  {
                     for(String number:token.toString().split("]")){
                        System.out.println(asin+":"+number);
                        num=Integer.parseInt(number);
                        ONE=new IntWritable(num);
                        context.write(new Text(asin),ONE);
                     }
                  }
               }
               
               JSONObject json2 = new JSONObject(text);
               asin2= json2.getString("asin");
               reviewText=json2.getString("reviewText");
            
         } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }

        }

    }
    
    
    
    
    
    
    
    

    public static class Map2 extends Mapper<LongWritable, Text, Text, IntWritable> {
        private final static IntWritable ONE = new IntWritable(1);
        private Text word = new Text();
        

        public void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
        	
        	String asin;
            String overall;
            String text = value.toString();
            try {

				JSONObject json = new JSONObject(text);
            	asin = json.getString("asin");
            
				System.out.println("Map2="+asin);
					
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        }

    }

    public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {
        @Override
        public void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
       
            int mostNumOfReivewer = 0;
        	int secondNumOfReivewer = 0;
            int thirdNumOfReivewer=0;
            int temp = 0;
            String initAsin;
            
            
            for (IntWritable val : values) {
            	temp = val.get();
                if(thirdNumOfReivewer < temp)
                {
                	thirdNumOfReivewer = temp;
                }
                
                if(secondNumOfReivewer < thirdNumOfReivewer)
                {
                	temp = secondNumOfReivewer;
                	secondNumOfReivewer = thirdNumOfReivewer;
                	thirdNumOfReivewer = temp;
                }
                if(mostNumOfReivewer < secondNumOfReivewer)
                {
                	temp = mostNumOfReivewer;
                	mostNumOfReivewer = secondNumOfReivewer;     	
                	secondNumOfReivewer = temp;
                }

            }
           // System.out.println("key = " + key + "mostNumOfReivewer = " + mostNumOfReivewer + " secondNumOfReivewer = " + secondNumOfReivewer + " thirdNumOfReivewer = " + thirdNumOfReivewer);
            context.write(key, new IntWritable(mostNumOfReivewer));
            context.write(key, new IntWritable(secondNumOfReivewer));
            context.write(key, new IntWritable(thirdNumOfReivewer));
        }
    }
    
}


