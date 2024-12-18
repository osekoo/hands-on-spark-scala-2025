## **Lab 1: Build Spark App using Scala and SBT**

### **Prerequisites**
Before starting, ensure that:
- You have administrative privileges on your machine.
- Basic understanding of terminal/command-line usage.

### **1. Install Java 8**
#### **Windows**
1. Download the Java 8 JDK from the [Oracle website](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html).
2. Run the installer and follow the installation instructions.
3. Set `JAVA_HOME`:
   - Right-click **This PC** > **Properties** > **Advanced system settings** > **Environment Variables**.
   - Add `JAVA_HOME` with the path to the JDK folder (e.g., `C:\Program Files\Java\jdk1.8.0_xx`).
   - Append `%JAVA_HOME%\bin` to the `Path` variable.

4. Verify installation:
   ```bash
   java -version
   ```

#### **Linux/MacOS**
1. Use a package manager to install Java 8:
   - **Ubuntu**:
     ```bash
     sudo apt update
     sudo apt install openjdk-8-jdk
     ```
   - **MacOS** (using Homebrew):
     ```bash
     brew install openjdk@8
     ```

2. Set `JAVA_HOME` in your shell configuration file (`~/.bashrc`, `~/.zshrc`, etc.):
   ```bash
   export JAVA_HOME=$(dirname $(dirname $(readlink -f $(which java))))
   export PATH=$JAVA_HOME/bin:$PATH
   ```

3. Verify installation:
   ```bash
   java -version
   ```



### **2. Install SBT**
#### **Windows**
1. Download the SBT installer from the [official website](https://www.scala-sbt.org/download.html).
2. Run the installer and follow the instructions.
3. Verify installation:
   ```bash
   sbt sbtVersion
   ```

#### **Linux/MacOS**
1. Install via package manager:
   - **Ubuntu**:
     ```bash
     echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | sudo tee /etc/apt/sources.list.d/sbt.list
     curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x99E82A75642AC823" | sudo apt-key add
     sudo apt-get update
     sudo apt-get install sbt
     ```

   - **MacOS**:
     ```bash
     brew install sbt
     ```

2. Verify installation:
   ```bash
   sbt sbtVersion
   ```



### **3. Create New Scala Project**
1. Create a new SBT project named `wordcount`:
   ```bash
   sbt new scala/scala-seed.g8
   ```
   - When prompted, name the project `wordcount`.

2. Navigate to the project directory:
   ```bash
   cd wordcount
   ```



### **4. Edit Project with IDE**
#### **Setup IDE**
- **IntelliJ IDEA**:
  - https://www.jetbrains.com/idea/
  - Open the `wordcount` directory.
  - Install the Scala plugin from **File > Settings > Plugins**.

- **Visual Studio Code**:
  - https://code.visualstudio.com/
  - Open the `wordcount` folder.
  - Install the **Metals** extension for Scala.

#### **Edit `build.sbt`**
Modify `build.sbt` to include Spark dependencies:
```scala
name := "wordcount"

version := "0.1"

scalaVersion := "2.12.18"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.3.2",
  "org.apache.spark" %% "spark-sql" % "3.3.2"
)
```

#### **Create `MainApp.scala`**
1. Create a file named `MainApp.scala` in the `src/main/scala` directory.
2. Implement the word count logic:
   ```scala
   import org.apache.spark.sql.SparkSession

   object MainApp {
     def main(args: Array[String]): Unit = {
       val spark = SparkSession.builder
         .appName("Word Count")
         .master("local[*]")
         .getOrCreate()

       val data = Seq("hello world", "hello spark", "hello scala", "word count example")
       val rdd = spark.sparkContext.parallelize(data)

       val wordCounts = rdd
         .flatMap(line => line.split(" "))
         .map(word => (word, 1))
         .reduceByKey(_ + _)

       wordCounts.collect().foreach(println)

       spark.stop()
     }
   }
   ```



### **5. Compile and Package Project**
1. Compile the project:
   ```bash
   sbt compile
   ```

2. Package the project:
   ```bash
   sbt package
   ```

3. Verify the JAR file exists:
   - Navigate to `target/scala-2.12`.
   - Ensure the file `wordcount_2.12-0.1.jar` is present.



### **6. Run the Spark Application**
Run the application to verify functionality:
```bash
spark-submit --class MainApp target/scala-2.12/wordcount_2.12-0.1.jar
```



### **Expected Output**
The console should display the word count results:
```
(hello, 3)
(world, 1)
(spark, 1)
(scala, 1)
(word, 1)
(count, 1)
(example, 1)
```



### **Outcome**
By completing this lab, you will:
- Set up the environment for Scala and Spark development.
- Create a basic Spark application to process data.
- Compile and package their Scala project into a runnable JAR.

### **Annex**

To assist you in setting up and familiarizing yourself with IntelliJ IDEA and Visual Studio Code, here are some beginner-friendly tutorials:

**IntelliJ IDEA:**

- **Official Getting Started Guide:** https://www.jetbrains.com/help/idea/getting-started.html JetBrains provides a comprehensive guide to help you get started with IntelliJ IDEA, covering installation, configuration, and basic features. 

- **Creating Your First Java Application:** https://www.jetbrains.com/help/idea/creating-and-running-your-first-java-application.html This tutorial walks you through creating, running, and packaging a simple Java application, introducing you to IntelliJ IDEA's coding assistance and tools. 

- **IntelliJ IDEA Tutorial for Beginners:** https://examples.javacodegeeks.com/java-development/desktop-java/ide/intellij-idea-tutorial-beginners/ An introductory tutorial that demonstrates how to create your first project using IntelliJ IDEA, suitable for those new to the IDE. 

**Visual Studio Code:**

- **Official Getting Started Tutorial:** https://code.visualstudio.com/docs/getstarted/getting-started Microsoft offers a step-by-step tutorial to help you understand the key features of Visual Studio Code, enabling you to start coding quickly. 

- **Learn Visual Studio Code in 7 Minutes:** https://learn.microsoft.com/en-us/shows/visual-studio-code/learn-visual-studio-code-in-7min-official-beginner-tutorial A concise video tutorial that provides an overview of Visual Studio Code, demonstrating how to write and execute code in various languages. 

- **Getting Started with Visual Studio Code:** https://dev.to/umeshtharukaofficial/getting-started-with-vscode-a-beginners-guide-2mic A beginner's guide that covers installation, basic features, customization, and productivity tips for Visual Studio Code. 

These resources should provide a solid foundation for you to begin working with IntelliJ IDEA and Visual Studio Code effectively.

For a quick visual introduction to Visual Studio Code, you might find the following video helpful:

 

