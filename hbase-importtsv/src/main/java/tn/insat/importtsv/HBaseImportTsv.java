package tn.insat.importtsv;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.mapreduce.ImportTsv;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HBaseImportTsv {
    private static final Logger LOG = LoggerFactory.getLogger(HBaseImportTsv.class);

    public static void main(String[] args) throws Exception {
        // Create configuration and load defaults
        Configuration conf = HBaseConfiguration.create();
        
        // Manually copy system properties to configuration
        System.getProperties().forEach((k, v) -> {
            if (k.toString().startsWith("importtsv.")) {
                conf.set(k.toString(), v.toString());
            }
        });

        if (conf.get("importtsv.columns") == null) {
            LOG.error("ERROR: Columns configuration missing. You must specify:");
            LOG.error(" -Dimporttsv.columns=\"HBASE_ROW_KEY,colfam:qual,...\"");
            System.exit(1);
        }

        LOG.info("Starting import with columns: {}", conf.get("importtsv.columns"));
        int result = ToolRunner.run(conf, new ImportTsv(), args);
        System.exit(result);
    }
}