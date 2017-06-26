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
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;




public class Project extends Configured implements Tool {
    public static void main(String[] args) throws Exception {
        System.out.println(Arrays.toString(args));
        int res = ToolRunner.run(new Configuration(), new Project(), args);
      
        System.exit(res);
    }

    @Override
    public int run(String[] args) throws Exception {
        System.out.println(Arrays.toString(args));

        Job job = Job.getInstance(getConf());
        job.setJarByClass(Project.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));


        job.setMapperClass(Map1.class);
        job.setOutputKeyClass(Text.class);
	job.setMapOutputValueClass(Text.class);	

        job.setReducerClass(Reduce.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        

        job.waitForCompletion(true);
      
        return 0;
    }
   
    public static class Map1 extends Mapper<LongWritable, Text, Text, Text> {

        public void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
  
            String asin;
            String reviewText;
            String help;
            String text = value.toString();
            String tmp;
            int num=0;
            try {

            JSONObject json = new JSONObject(text);
               asin= json.getString("asin");
               help=json.getString("helpful");
               reviewText=json.getString("reviewText");
               for (String token: help.toString().split(",")) {
                  if(token.matches(".*]"))
                  {
                     for(String number:token.toString().split("]")){

                    	 tmp=number+";"+reviewText;
                         context.write(new Text(asin),new Text(tmp));
                     }
                  }
               }
               
 
            
         } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }

        }

    }
    
    
    
    

    public static class Reduce extends Reducer<Text, Text, Text, Text> {
        @Override
        public void reduce(Text key, Iterable<Text> values, Context context)
                throws IOException, InterruptedException {
       

            int max = 0;
            String text = null;
            
            
            for (Text val : values) {
            	String[] a=val.toString().split(";");
            System.out.println(a[0]);	
    			if(Integer.parseInt(a[0])>max)
    			{
    				max=Integer.parseInt(a[0]);
    				text=a[1];
    			}
            
            }            
            context.write(key, new Text(text));

        }
    }
}
