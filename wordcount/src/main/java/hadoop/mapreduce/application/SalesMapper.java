package hadoop.mapreduce.application;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class SalesMapper extends Mapper<Object, Text, Text, DoubleWritable> {

    private Text store = new Text();
    private DoubleWritable cost = new DoubleWritable();

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String[] fields = value.toString().split("\\s+");
        
        if (fields.length == 6) {  
            store.set(fields[2]);  
            cost.set(Double.parseDouble(fields[4]));  
            
            context.write(store, cost);
        }
    }
}
