### **Building a Spark Project**

The purpose of this lab is to create and build a Spark project using **Scala** and **SBT**. By following these steps, you’ll learn how to set up the project, understand its structure, and use SBT commands to compile and package your application.



### **Step 1: Set Up the Project**
1. **Create a New Project**:
   - Follow the tutorial at [https://github.com/osekoo/spark-scala.g8](https://github.com/osekoo/spark-scala.g8) to generate a new Spark project using the Giter8 template engine.
   - Run the following command:
     ```bash
     sbt new osekoo/spark-scala.g8
     ```
   - Provide the required details, such as the project name (wordcount), Scala version (2.12.18), and Spark version (3.5.2).

2. **Open the Project in an IDE**:
   - Use your favorite IDE (e.g., **Visual Studio Code** or **IntelliJ IDEA**) to open the project as described in the tutorial. This will make editing and managing your code easier.



### **Step 2: Understand the Project Structure**
The project template generates the following files and folders:

#### **1. `build.sbt`**
- **Purpose**: The main configuration file for the SBT build tool.
- **Content**:
  - Project settings (name, version, Scala version).
  - Dependencies for Spark and other libraries.
- **Example**:
  ```scala
    name := "wordcount"
    
    version := "0.1"
    
    scalaVersion := "2.12.18"
    
    val sparkVersion = "3.5.2"
    
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % sparkVersion,
      "org.apache.spark" %% "spark-sql" % sparkVersion,
      "org.apache.spark" %% "spark-mllib" % sparkVersion % "provided"
    )

  ```

#### **2. `src/main/scala/MainApp.scala`**
- **Purpose**: The main entry point of your Spark application.
- **Content**:
  - A basic Spark job that processes data.
- **Example**:
  ```scala
      import org.apache.spark.sql.SparkSession
      
      object MainApp {
        def main(args: Array[String]): Unit = {
          val spark = SparkSession.builder
            .appName("Word Count")
            .getOrCreate() // create a Spark session
      
          spark.sparkContext.setLogLevel("ERROR")
      
          wordCount(spark) // call the wordCount function
      
          spark.stop() // stop the Spark session
        }
      
        private def wordCount(spark: SparkSession): Unit = {
          val fruits = Seq("apple", "banana", "carrot", "orange", "kiwi", "melon", "pineapple") // list of fruits
          val colors = Seq("red", "yellow", "orange", "green", "brown", "blue", "purple") // list of colors
          // pick between 5 and 15 colored fruits randomly as one item of a seq and repeat them 1000 times to create a dataset
          val data = (1 to 1000).map(_ => (1 to scala.util.Random.nextInt(10) + 5).map(_ => s"${colors(scala.util.Random.nextInt(colors.length))} ${fruits(scala.util.Random.nextInt(fruits.length))}").mkString(", "))
      
          // print the first 10 items of the dataset
          println("\n============================ Dataset ============================")
          data.take(10).foreach(println)
          println("=================================================================\n")
      
          // create an RDD from the dataset
          val rdd = spark.sparkContext.parallelize(data)
          val wordCounts = rdd
            .flatMap(line => line.split("[ ,]")) // split each line into words
            .filter(word => word.nonEmpty) // filter out empty words
            .map(word => (word, 1)) // create a tuple of (word, 1)
            .reduceByKey((a, b) => a + b) // sum the counts
            .sortBy(a => a._2, ascending = false) // sort by count in descending order
      
          println("\n============================ Word count result ============================")
          wordCounts.collect().foreach(println) // print the result
          println("===========================================================================\n")
      
        }
      }

  ```

#### **3. `run-app`**
- **Purpose**: A script to start the Spark application and the local Spark cluster.
- **Content**: 
  - Starts Spark master and worker processes.
  - Submits the compiled JAR file to Spark.
- **Usage**: Run it to start the Spark application.

#### **4. `spark-stop`**
- **Purpose**: A script to stop the local Spark cluster.
- **Content**:
  - Stops all running Spark processes.



### **Step 3: SBT Commands**
SBT (Scala Build Tool) is essential for managing your project. Below are the key SBT commands you’ll use:

1. **`sbt compile`**:
   - Compiles the Scala source files.
   - Checks for syntax errors and ensures dependencies are correctly resolved.

2. **`sbt package`**:
   - Packages your project into a JAR file.
   - The resulting JAR is located in the `target/scala-<version>/` directory.

3. **`sbt assembly`** (Optional):
   - Creates a "fat JAR" containing all dependencies.
   - Useful for deploying the application to a cluster.

4. **`sbt clean`**:
   - Removes previously compiled files and artifacts.



### **Step 4: Run the Application**

#### **1. Package the Application**
Run the following command to compile and package the project:
```bash
sbt package
```

#### **2. Start the Application**
Run the application using the `run-app` script:
```bash
./run-app
```

#### **3. Monitor Spark Dashboard**
While the application is running, open the Spark UI to monitor job execution:
- URL: [http://localhost:8080](http://localhost:8080)
- The UI shows details about stages, tasks, and executors.

#### **4. Stop the Spark Cluster**
After completing the application run, stop the Spark cluster:
```bash
./spark-stop
```

### **What is `spark-submit`?**

`spark-submit` is a command-line tool provided by Apache Spark to submit and run Spark applications on various Spark-supported cluster managers (e.g., standalone, YARN, Mesos, Kubernetes) or locally on your machine.

It acts as the entry point to execute your compiled Spark application JAR, passing it configuration parameters, application-specific arguments, and resource details.



### **How Does `spark-submit` Work?**
1. **Reads the Application JAR**: The tool loads the JAR file containing your application code.
2. **Submits the Job to Spark Cluster**: Sends the application to the Spark cluster (or local Spark environment) based on the specified `--master` configuration.
3. **Allocates Resources**: Determines how many cores, memory, and executors are needed.
4. **Runs the Application**: Executes your Spark code and provides logs for debugging or monitoring.



### **Common Syntax**
The basic syntax for `spark-submit` is:
```bash
spark-submit [options] <application-jar> [application-arguments]
```



### **Key Options in `spark-submit`**

1. **Cluster Manager**
   - **`--master`**: Specifies where to run the application (e.g., local, standalone cluster, or YARN).
   - Examples:
     - `--master local[*]`: Run locally with all available cores.
     - `--master spark://<master-host>:7077`: Submit to a standalone cluster.

2. **Deploy Mode**
   - **`--deploy-mode`**: Defines how the driver runs.
     - `client`: Driver runs on the submitting machine.
     - `cluster`: Driver runs on the cluster (for non-local clusters).

3. **Application Resources**
   - **`--num-executors`**: Number of executors to use.
   - **`--executor-cores`**: Number of cores per executor.
   - **`--executor-memory`**: Memory allocated per executor (e.g., `2G`).

4. **Application Main Class**
   - **`--class`**: Specifies the main class of your application.

5. **Files and Dependencies**
   - **`--jars`**: Additional JAR files required for the application.
   - **`--files`**: Files to distribute to executors.
   - **`--packages`**: Maven coordinates for additional dependencies.

6. **Logging and Debugging**
   - **`--verbose`**: Displays detailed output.
   - **`--conf`**: Sets custom Spark configurations (e.g., `spark.executor.extraJavaOptions`).



### **Example Usage**

#### **1. Run a Spark Application**
```bash
spark-submit \
    --deploy-mode client \
    --master "$SPARK_MASTER_URL" \
    --executor-cores 4 \
    --executor-memory 2G \
    --num-executors 1 \
    --class "MainApp" \
    "target/scala-2.12/wordcount_2.12-0.1.jar" \
```
- Runs the application locally using all available cores.
- The main class is `MainApp`.
- The JAR file is located in `target/scala-2.12/wordcount_2.12-0.1.jar`.



#### **3. Submit with Additional Files and Dependencies**
```bash
spark-submit \
  --master spark://spark-master:7077 \
  --class MainApp \
  --files data/input.csv \
  --jars external-lib.jar \
  target/scala-2.12/mysparkapp_2.12-0.1.jar
```
- Distributes `data/input.csv` to executors.
- Includes `external-lib.jar` as an additional dependency.



### **How `spark-submit` Fits Into Your Workflow**
- **Development**: Use `sbt` or IDE tools to run locally during development.
- **Testing**: Use `spark-submit` with `--master local` for testing.
- **Production**: Use `spark-submit` to deploy applications to standalone clusters or resource managers like YARN or Kubernetes.



### **Integration with the Lab**
In the lab, the `run-app` script internally uses `spark-submit` to start the Spark application. You can view or customize the `spark-submit` command in the script:

This demonstrates how `spark-submit` is essential for running Spark applications in various environments, offering flexibility and control over resource allocation and execution.


### **Questions to Explore**
1. **Project Structure**:
   - What is the purpose of the `build.sbt` file?
   - How does `MainApp.scala` interact with Spark?

2. **SBT Commands**:
   - What is the difference between `sbt package` and `sbt assembly`?

3. **Spark Monitoring**:
   - What information does the Spark UI provide?
   - How can you optimize your Spark job based on Spark UI metrics?

4. **Scripts**:
   - What does the `run-app` script automate?
   - Why is `spark-stop` important?



### **Outcome**
By completing this lab, you will:
- Set up and build a Spark project using Scala and SBT.
- Understand the project structure and key files.
- Use SBT commands to compile and package your Spark application.
- Run and monitor your application using Spark's built-in tools.

Feel free to ask questions or explore additional details to enhance your understanding!
