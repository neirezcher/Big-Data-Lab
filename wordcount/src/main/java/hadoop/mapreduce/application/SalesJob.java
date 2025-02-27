package hadoop.mapreduce.application;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class SalesJob {
    public static void main(String[] args) throws Exception {
        // Vérification du nombre d'arguments
        if (args.length != 2) {
            System.err.println("Usage: SalesJob <input path> <output path>");
            System.exit(-1);
        }

        // Configuration du job MapReduce
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Sales Total by Store");
        job.setJarByClass(SalesJob.class);
        
        // Définition des classes Mapper et Reducer
        job.setMapperClass(SalesMapper.class);
        job.setReducerClass(SalesReducer.class);
        
        // Spécification des types de sortie du Mapper et du Reducer
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(DoubleWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);

        // Définition des chemins d'entrée et de sortie
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // Lancement du job MapReduce
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}