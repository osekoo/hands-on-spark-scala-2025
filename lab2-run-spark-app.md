## **Lab 2: Run Spark App using Docker**


### **1. Install Docker**
#### **Windows**
1. Download and install Docker Desktop from the [official website](https://www.docker.com/products/docker-desktop).
2. Follow the installation steps and ensure "Enable WSL 2 features" is selected.
3. Start Docker Desktop.
4. Verify installation:
   ```bash
   docker --version
   ```

#### **Linux**
1. Install Docker:
   - **Ubuntu**:
     ```bash
     sudo apt update
     sudo apt install docker.io
     ```
   - Add your user to the Docker group to avoid using `sudo`:
     ```bash
     sudo usermod -aG docker $USER
     ```
   - Logout and log back in.
2. Verify installation:
   ```bash
   docker --version
   ```

#### **MacOS**
1. Download Docker Desktop from the [official website](https://www.docker.com/products/docker-desktop).
2. Install and start Docker Desktop.
3. Verify installation:
   ```bash
   docker --version
   ```



### **2. Install Docker Compose**
Docker Compose is included with Docker Desktop for Windows and MacOS. For Linux:
```bash
sudo apt update
sudo apt install docker-compose
```

Verify installation:
```bash
docker-compose --version
```

**Note:**  
You can download all the files mentioned in these labs from the [GitHub repository](https://github.com/osekoo/hands-on-spark-scala-2025), or you can continue following the tutorial to create them step by step on your own. Both approaches will help you complete the labs successfully, but creating the files yourself will provide a deeper understanding of the concepts and practices involved.


### **3. Create `docker-compose.yml`**
1. In your working directory, create a file named `docker-compose.yml`.
2. Add the following content to define the Spark cluster:
   ```yaml
      version: '3.8'
      services:
        spark-master:
          image: bitnami/spark:3.5
          container_name: spark-master
          working_dir: /app
          environment:
            - SPARK_MODE=master
            - SPARK_MASTER_HOST=spark-master
          ports:
            - "8080:8080"
            - "7077:7077"
          volumes:
            - ./:/app
          restart: unless-stopped
      
        spark-worker:
          image: bitnami/spark:3.5
          environment:
            - SPARK_MODE=worker
            - SPARK_MASTER_URL=spark://spark-master:7077
          depends_on:
            - spark-master
          restart: unless-stopped
          deploy:
            replicas: 2
   ```

   **Explanation**:
   - `spark-master`: Runs the Spark master node.
   - `spark-worker`: Runs a worker node connected to the master.
   - `spark-submit`: Used to submit Spark applications with an attached volume.



### **4. Create the `spark-submit.sh` Script**
1. In the `app` directory, create a file named `spark-submit.sh`.
2. Add the following content to define the `spark-submit` command:
   ```bash
      #!/bin/bash
      /opt/bitnami/spark/bin/spark-submit \
          --deploy-mode "client" \
          --master "spark://spark-master:7077" \
          --executor-cores 4 \
          --executor-memory 2G \
          --num-executors 1 \
          --class "MainApp" \
          "target/scala-2.12/wordcount_2.12-0.1.jar" \

   ```

3. Make the script executable:
   ```bash
   chmod +x ./app/spark-submit.sh
   ```



### **5. Create the `run-app` Script**
#### **For Linux/MacOS**
1. In your project directory, create a file named `run-app`.
2. Add the following content:
   ```bash
   #!/bin/bash
   docker exec -it spark-master /app/spark-submit.sh
   ```

3. Make the script executable:
   ```bash
   chmod +x run-app
   ```

#### **For Windows**
1. Create a file named `run-app.bat` in your project directory.
2. Add the following content:
   ```cmd
   @echo off
   docker exec -it spark-master /app/spark-submit.sh
   ```


### **6. Create `spark-start` Script**
This script wraps the `docker-compose up` command to start the Spark cluster.

#### **For Linux/MacOS**
1. Create a file named `spark-start` in your project directory.
2. Add the following:
   ```bash
   #!/bin/bash
   echo "Starting Spark cluster..."
   docker-compose up -d
   ```

3. Make it executable:
   ```bash
   chmod +x spark-start
   ```

#### **For Windows**
1. Create a file named `spark-start.bat` in your project directory.
2. Add the following:
   ```cmd
   @echo off
   echo Starting Spark cluster...
   docker-compose up -d
   ```


### **7. Create `spark-stop` Script**
This script wraps the `docker-compose down` command to stop the Spark cluster.

#### **For Linux/MacOS**
1. Create a file named `spark-stop` in your project directory.
2. Add the following:
   ```bash
   #!/bin/bash
   echo "Stopping Spark cluster..."
   docker-compose down
   ```

3. Make it executable:
   ```bash
   chmod +x spark-stop
   ```

#### **For Windows**
1. Create a file named `spark-stop.bat` in your project directory.
2. Add the following:
   ```cmd
   @echo off
   echo Stopping Spark cluster...
   docker-compose down
   docker system prune -f
   ```

### **8. Workflow to Execute the Spark Application**

1. **Start the Spark Cluster:**
   - **Linux/MacOS**:
     ```bash
     ./spark-start
     ```
   - **Windows**:
     ```cmd
     spark-start.bat
     ```
   - Access the Spark Web UI:
       - Open a browser and go to `http://localhost:8080`.


2. **Run the Spark Application:**
   - **Linux/MacOS**:
     ```bash
     ./run-app
     ```
   - **Windows**:
     ```cmd
     run-app.bat
     ```

   - **Expected Output**  
      The console should display the word count results:
      ```
      (pineapple,940)
      (melon,934)
      (orange,934)
      (banana,929)
      (kiwi,927)
      (apple,927)
      (carrot,919)
      ```

3. **Stop the Spark Cluster:**
   - **Linux/MacOS**:
     ```bash
     ./spark-stop
     ```
   - **Windows**:
     ```cmd
     spark-stop.bat
     ```


### **Outcome**
By completing this lab, you will:
- Set up a local Spark cluster using Docker and Docker Compose.
- Automate the execution of `spark-submit` with reusable scripts.
- Manage a Spark cluster lifecycle effectively.
