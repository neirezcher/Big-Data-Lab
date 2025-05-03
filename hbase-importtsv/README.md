# HBase Import TSV Example

This repository provides an example Java application to import TSV (Tab-Separated Values) data into HBase using Hadoop's MapReduce framework.

## Table of Contents
- [Prerequisites](#prerequisites)
- [Building the Project](#building-the-project)
- [Running the Import](#running-the-import)
  
## Prerequisites

Before you begin, ensure you have the following installed:

- **Java** (JDK 8 or later)
- **Apache Maven** (to build the project)
- **Apache Hadoop** (for running MapReduce jobs)
- **Apache HBase** (for HBase setup)
- **SLF4J** (for logging)

## Building the Project

1. Clone the repository:
    ```bash
    git clone https://github.com/neirezcher/Big-Data-Lab.git
    cd repo
    ```

2. Build the project using **Maven**:
    ```bash
    mvn clean package
    ```
   This will generate a JAR file named `HBase-importTSV.jar` in the `target/` directory.

## Running the Import

1. Ensure that your **HBase** and **Hadoop** cluster is up and running, and the necessary configurations are set.
   
2. You need to pass the following arguments to the Java program when running it:

   - **`-Dmapreduce.framework.name`**: Set to `yarn` to run the job on YARN.
   - **`-Dyarn.resourcemanager.address`**: Address of your YARN ResourceManager.
   - **`-Dhbase.zookeeper.quorum`**: Address of your HBase Zookeeper quorum.
   - **`-Dimporttsv.columns`**: The columns configuration for the import (e.g., columns from `data:rank`, `data:target_sequence`, etc.).
   - **`-Dimporttsv.separator`**: Set to the correct separator (e.g., `\x09` for tab-separated values).
   - The main class to execute: `tn.insat.importtsv.HBaseImportTsv`.
   - The first argument is the **HBase table name** where the data will be imported.
   - The second argument is the **path to the TSV file**.

   Example command:

   ```bash
   java -cp "HBase-importTSV.jar:lib/*:$(/usr/local/hadoop/bin/hadoop classpath):/usr/local/hbase/lib/*" \
   -Dmapreduce.framework.name=yarn \
   -Dyarn.resourcemanager.address=<RESOURCE_MANAGER_ADDRESS> \
   -Dhbase.zookeeper.quorum=<ZOOKEEPER_QUORUM_ADDRESS> \
   -Dimporttsv.columns="<COLUMN_CONFIGURATION>" \
   -Dimporttsv.separator=<SEPARATOR> \
   tn.insat.importtsv.HBaseImportTsv \
   <HBASE_TABLE_NAME> \
   <TSV_FILE_PATH>

## Titanic Import Example

In this example, we will import a Titanic dataset in TSV (Tab-Separated Values) format into an HBase table. The dataset contains information about passengers aboard the Titanic, such as survival status, age, class, name, ticket details, and travel information.

### Creating the HBase Table

First, we need to create the HBase table `titanic`, which will have three column families: `passenger`, `ticket`, and `travel`.

Run the following command in the HBase shell:

```bash
hbase shell <<EOF
create 'titanic', 
  {NAME => 'passenger', VERSIONS => 1},
  {NAME => 'ticket', VERSIONS => 1},
  {NAME => 'travel', VERSIONS => 1}
EOF
```
### Importing the TSV Data

After creating the table, you can import the data from the `titanic.tsv` file using the ImportTsv MapReduce job:

```bash
hbase org.apache.hadoop.hbase.mapreduce.ImportTsv \
  -Dmapreduce.framework.name=local \
  -Dimporttsv.separator=$'\t' \
  -Dimporttsv.columns="HBASE_ROW_KEY,passenger:survived,passenger:pclass,passenger:name,passenger:sex,passenger:age,ticket:sibsp,ticket:parch,ticket:number,ticket:fare,ticket:cabin,travel:embarked" \
  titanic file:///Hbase-TSV/titanic.tsv
```
### Verifying the Data

After importing the data, you can verify the import by running a scan on the `titanic` table in HBase:

```bash
hbase scan 'titanic' {LIMIT => 5}
```
## Titanic TSV File

The `titanic.tsv` file contains passenger data with the following fields:

### Passenger Details
- **Survival status**: `survived` (0 = No, 1 = Yes)
- **Class**: `pclass` (1 = 1st, 2 = 2nd, 3 = 3rd)
- **Name**: `name` (Full passenger name)
- **Sex**: `sex` (Male/Female)
- **Age**: `age` (Numeric value)

### Ticket Information
- **Siblings/Spouse count**: `sibsp` (Number of siblings/spouses aboard)
- **Parents/Children count**: `parch` (Number of parents/children aboard)
- **Ticket number**: `number` (Ticket identifier)
- **Fare**: `fare` (Ticket price)
- **Cabin**: `cabin` (Cabin number)

### Travel Details
- **Embarked port**: `embarked` (C = Cherbourg, Q = Queenstown, S = Southampton)

### File Location
```
hbase-importtsv/src/main/resources/titanic.tsv
```

Make sure to place the `titanic.tsv` file at the correct location before running the import command. If you're using a different directory or path for your TSV file, make sure to update the `file://` path in the import command accordingly.

## Troubleshooting

- Ensure your **HBase** and **Hadoop** configurations are set correctly in your environment.
- Double-check the format of the TSV file and make sure it follows the specified separator.
- If you encounter memory-related issues, try adjusting the heap size or other Hadoop configurations for resource management.
