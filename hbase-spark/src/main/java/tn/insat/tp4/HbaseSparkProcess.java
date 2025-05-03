package tn.insat.tp4;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class HbaseSparkProcess {

    public void createHbaseTable() {
        Configuration config = HBaseConfiguration.create();
        SparkConf sparkConf = new SparkConf().setAppName("SparkHBaseTest").setMaster("local[4]");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);

        //Définir le nom de la table HBase à lire
        config.set(TableInputFormat.INPUT_TABLE, "sales_ledger");

        // Création d’un RDD basé sur les données HBase (clé, résultat)
        JavaPairRDD<ImmutableBytesWritable, Result> hBaseRDD =
                jsc.newAPIHadoopRDD(
                        config, // Configuration HBase
                        TableInputFormat.class, // Format d’entrée HBase
                        ImmutableBytesWritable.class, // Type de la clé (clé HBase)
                        Result.class // Type de la valeur (résultat HBase)
                );

        System.out.println("nombre d'enregistrements: "+hBaseRDD.count());

    }

    public static void main(String[] args){
        HbaseSparkProcess admin = new HbaseSparkProcess();
        admin.createHbaseTable();
    }

}
