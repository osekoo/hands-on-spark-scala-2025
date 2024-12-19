### **Lab 3: Deployment On Cluster**

This lab focuses on deploying and executing a Spark application on the **LAMSADE Cluster**. You will learn how to transfer files, configure the remote environment, run Spark jobs, and monitor progress using the provided cluster resources. By completing this lab, you will gain practical experience in deploying and managing Spark applications on a distributed cluster environment.



### **Procedure to Run a Spark Application on the LAMSADE Cluster**



#### **1. Request Your Account Key**
Before starting, ensure you have your LAMSADE cluster account key.  
- Contact **Dario** to request your key if you don’t have one.  
- Your key file may look like: `id_123_<user_name>.key`.



#### **2. Connect to the Remote Server**
Use SSH to connect to the LAMSADE cluster:
```bash
ssh -p 5022 -i <your_account_key> <user_name>@ssh.lamsade.dauphine.fr
```
- Replace `<your_account_key>` with the path to your private SSH key file.
- Replace `<user_name>` with your cluster username.
- You will be prompted for your password if the SSH key is correctly configured.



#### **3. Set Up Workspace on the Remote Server**
1. Create a workspace for your Spark project:
   ```bash
   mkdir -p ~/workspace/data
   ```
2. Verify the directory:
   ```bash
   ls ~/workspace
   ```
   You should see the `data` folder.



#### **4. Transfer Your Data to the Remote Server**
Use one of the following methods to upload your data files:

- **From Your Local Machine Using SCP**:
  ```bash
  scp -P 5022 -i <your_account_key> <your_local_file> <user_name>@ssh.lamsade.dauphine.fr:~/workspace/data
  ```
- **Download Directly on the Remote Server**:
  ```bash
  wget -P ~/workspace/data <file_url>
  ```
- **Copy from HDFS (on the Remote Server)**:
  ```bash
  hdfs dfs -get /path/to/your/data ~/workspace/data
  ```



#### **5. Transfer Your JAR File**
Copy your pre-built Spark application JAR file to the `workspace` directory:
```bash
scp -P 5022 -i <your_account_key> <jar_location> <user_name>@ssh.lamsade.dauphine.fr:~/workspace
```



#### **6. Create a Spark Runner Script**
Create a script (`spark-run.sh`) to automate the Spark job submission.

##### **Script Example**:
```bash
#!/bin/bash
spark-submit \
    --deploy-mode "client" \
    --master "spark://vmhadoopmaster.cluster.lamsade.dauphine.fr:7077" \
    --executor-cores "4" \
    --executor-memory "2G" \
    --num-executors "2" \
    --class "YourMainClass" \
    "your-spark-app.jar" \
    arg1 arg2
```

- Modify the arguments based on your deployment environment.



#### **7. Transfer the Runner Script**
Copy the runner script to the `workspace` directory:
```bash
scp -P 5022 -i <your_account_key> <runner_location> <user_name>@ssh.lamsade.dauphine.fr:~/workspace
```



#### **8. Set Execution Permissions**
Make the script executable on the remote server:
```bash
chmod +x ~/workspace/spark-run.sh
```


#### **10. Run the Application**
Execute the Spark job from the `workspace` directory:
```bash
cd ~/workspace
./spark-run.sh
```



#### **11. Monitor Job Progress**
You can monitor the job in two ways:
1. **Terminal Output**: View real-time logs in the terminal.
2. **Spark Web UI**: Access the web UI for detailed job information:
   - Open an SSH tunnel:
     ```bash
     ssh -p 5022 -i <your_account_key> <user_name>@ssh.lamsade.dauphine.fr -L 8080:vmhadoopmaster.cluster.lamsade.dauphine.fr:8080
     ```
   - Access the UI at `http://localhost:8080`.



#### **12. Troubleshooting**
- **Job Failures**: Check Spark UI
- **Data Issues**: Verify file paths and ensure files are uploaded correctly and available in HDFS.
- **Cluster Issues**: Use the YARN Web UI to check node health and resource usage:
  - `http://vmhadoopmaster.cluster.lamsade.dauphine.fr:8088/cluster/nodes`



### **Cluster Configuration Overview**
- **Spark Version**: 3.5.1
- **Scala Version**: 2.12.18
- **Java Version**: 1.8
- **Cluster Hardware**:
  - 9 Nodes: 4x (32G, 16 cores), 1x (40G, 16 cores), 2x (2G, 2 cores), 1x (1G, 2 cores)
  - Spark Master URL: `spark://vmhadoopmaster.cluster.lamsade.dauphine.fr:7077`
  - Spark UI: `http://vmhadoopmaster.cluster.lamsade.dauphine.fr:8080/`

This configuration provides an efficient environment for executing moderate to large-scale Spark jobs.

--- 

By the end of this lab, you will have successfully deployed and monitored a Spark application on the LAMSADE cluster, giving you hands-on experience with distributed data processing in a production-like environment.
