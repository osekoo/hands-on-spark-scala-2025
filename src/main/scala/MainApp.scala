import org.apache.spark.sql.SparkSession

object MainApp {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder
      .appName("Word Count")
      .getOrCreate()  // create a Spark session

    spark.sparkContext.setLogLevel("ERROR")

    val fruits = Seq("apple", "banana", "carrot", "orange", "kiwi", "melon", "pineapple")  // list of fruits
    // pick between 5 and 15 fruits randomly as one item of a seq and repeat them 1000 times to create a dataset
    val data = (1 to 1000).map(_ =>
      scala.util.Random.shuffle(fruits)
        .take(5 + scala.util.Random.nextInt(11))
        .mkString(" "))

    // create an RDD from the dataset
    val rdd = spark.sparkContext.parallelize(data)
    val wordCounts = rdd
      .flatMap(line => line.split(" "))  // split each line into words
      .map(word => (word, 1))  // create a tuple of (word, 1)
      .reduceByKey((a, b) => a + b)  // sum the counts
      .sortBy(a => a._2, ascending = false)  // sort by count in descending order

    println("\n============================ Word count result ============================")
    wordCounts.collect().foreach(println)  // print the result
    println("===========================================================================\n")

    spark.stop()  // stop the Spark session
  }
}