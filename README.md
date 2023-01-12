# AWS and MongoDB Fraud Detection

This repository contains information and resources on building a fraud detection system using Amazon Web Services (AWS) and MongoDB.

## Table of Contents
- [Introduction](#introduction)
- [MongoDB Serverless](#mongodb-serverless)
- [Amazon Data Wrangler](#amazon-data-wrangler)
- [Architecture Diagram](#architecture-diagram)
- [Implementing a Fraud Detection System](#implementing-a-fraud-detection-system)
- [Conclusion](#conclusion)

## Introduction
Fraud detection is the process of identifying fraudulent activities or transactions in a system. It is a crucial aspect of risk management for businesses and organizations, as it helps to prevent financial losses and protect against fraudulent activities. Fraud detection can be done manually or through the use of automated systems and algorithms. 

Machine learning techniques, such as artificial neural networks and decision trees, are commonly used in automated fraud detection systems to identify patterns and anomalies in transactional data. These systems can be trained on historical data to recognize fraudulent behaviour and can be fine-tuned to adapt to changing patterns of fraud. Fraud detection is important for a wide range of industries, including financial services, e-commerce, and healthcare, to ensure the integrity and security of transactions.


## MongoDB Serverless
MongoDB Serverless is a fully managed service that allows you to run MongoDB workloads using a serverless model. With MongoDB Serverless, you only pay for the computing and storage resources that you use, and the service automatically scales up or down based on the workload demands.

MongoDB Serverless is built on top of the MongoDB Atlas platform, which is a global cloud database service that supports the development, deployment, and management of MongoDB workloads. You can use MongoDB Serverless to build applications that require a flexible, scalable, and highly available database without the need to manage the underlying infrastructure.

To use MongoDB Serverless, you will need to sign up for a [MongoDB Atlas](https://cloud.mongodb.com/) account and create a cluster. You can then enable the MongoDB Serverless option for your cluster, and use the MongoDB API or one of the available MongoDB drivers to connect to your database from your application.
 

## Amazon Data Wrangler
Amazon Data Wrangler allows users to analyze, visualize, and manipulate data in Amazon Redshift, Amazon S3, and Amazon Athena. It is designed to make it easier for data analysts and data scientists to work with large datasets and build data pipelines.

With Amazon Data Wrangler, users can use Python to load data into Amazon Redshift, create data transformations, and build data pipelines. It includes a number of pre-built connectors and functions for working with data from a variety of sources, including CSV, JSON, and Excel files.

Some of the key features of Amazon Data Wrangler include

- Data ingestion and preparation: Amazon Data Wrangler includes tools for loading data from a variety of sources into Amazon Redshift, including CSV, JSON, and Excel files. It also includes functions for cleaning and transforming data, such as filtering, pivoting, and aggregating.

- Data visualization: Amazon Data Wrangler includes a visual editor for creating data visualizations using popular libraries like Matplotlib and Seaborn.

- Data pipeline construction: Amazon Data Wrangler allows users to build data pipelines using a drag-and-drop interface, making it easier to connect different data sources and transformations.

- Collaboration: Amazon Data Wrangler includes tools for collaborating with other users, including version control and the ability to share notebooks and pipelines.

Overall, Amazon Data Wrangler is a powerful tool for working with large datasets and building data pipelines on the AWS platform.

## Architecture Diagram
<img width="984" alt="image" src="https://user-images.githubusercontent.com/114057324/211982044-74de4a21-cd08-4d6b-9bd2-f2661eedaf8b.png">

1. Web Application for accessing the end-point
2. MongoDB Serverless 
3. API Services
4. Amazon MSK
5. S3 bucket
6. Amazon SageMaker DataWrangler

## Implementing a Fraud Detection System
To implement a fraud detection system using AWS and MongoDB, follow the below given steps;

#### Atlas Cluster
- [Set up](https://www.mongodb.com/docs/atlas/tutorial/create-serverless-instance/) a serverless cluster in MongoDB Atlas.
- Configure the database for [network security](https://www.mongodb.com/docs/atlas/security/add-ip-address-to-list/) and access.
#### S3 Bucket
- Configure [S3](https://docs.aws.amazon.com/quickstarts/latest/s3backup/step-1-create-bucket.html) for storing data.

#### MSK
Set up [MSK](https://github.com/mongodb-partners/Atlas_to_Redshift#step-by-step-instructions-for-setting-up-amazon-msk-job) for streaming data from MongoDB to S3

#### Data Wrangler
- Create a data wrangler with endpoint deployment as stated in the below steps

1. Import data into Data Wrangler  			
<img width="1728" alt="Screenshot 2022-12-20 at 2 35 54 PM" src="https://user-images.githubusercontent.com/114057324/211983413-3d5156b5-3265-4c16-97af-4cef977e4f55.png">

2. Choose S3 Bucket as Data Source
<img width="1728" alt="Screenshot 2022-12-20 at 2 38 14 PM" src="https://user-images.githubusercontent.com/114057324/211983545-f428f226-571b-4dd0-bb4a-35b64ed40631.png">

3. Select the S3 bucket to import data from and select the data
<img width="1728" alt="Screenshot 2022-12-20 at 2 38 34 PM" src="https://user-images.githubusercontent.com/114057324/211983618-9adb24ca-00bd-4378-a003-4d1b552777a9.png">

<img width="1728" alt="Screenshot 2022-12-20 at 2 38 51 PM" src="https://user-images.githubusercontent.com/114057324/211983697-81dadb55-b169-4eaf-8ba6-2edee7950e5d.png">

4. Apply any transformation as required
<img width="1728" alt="Screenshot 2022-12-20 at 2 40 04 PM" src="https://user-images.githubusercontent.com/114057324/211983766-91e5ebd6-d8a3-48d2-9dd3-a2956381b86f.png">

5. Next, select the location to save the flow
<img width="1728" alt="Screenshot 2022-12-20 at 2 40 28 PM" src="https://user-images.githubusercontent.com/114057324/211983848-9ee0ed86-07c7-47e5-a75f-551647032379.png">

6. Set the experiment name and other parameters
<img width="1728" alt="Screenshot 2022-12-20 at 2 41 14 PM" src="https://user-images.githubusercontent.com/114057324/211983958-b46b04f0-6ca2-4b95-85eb-a621a0f89afc.png">

7. Select the target and feature columns for training
<img width="1728" alt="Screenshot 2022-12-20 at 2 41 26 PM" src="https://user-images.githubusercontent.com/114057324/211984010-af02fafa-57ca-4fc6-bef4-3b80f3957f65.png">

8. Select the training method which best suits your assessment
<img width="1728" alt="Screenshot 2022-12-20 at 2 41 34 PM" src="https://user-images.githubusercontent.com/114057324/211984086-5ee65ab8-d965-444a-a049-4e63aa519275.png">

9. Set the endpoint name and run the experiment.
<img width="1728" alt="Screenshot 2022-12-20 at 2 41 47 PM" src="https://user-images.githubusercontent.com/114057324/211984132-449fe245-20e0-41e5-a54f-f07d267be806.png">

10. Post training, the best model will be auto-deployed to the enpoint name you provided in the earlier step.
<img width="1728" alt="Screenshot 2022-12-20 at 9 22 46 PM" src="https://user-images.githubusercontent.com/114057324/211984212-b42fa1bc-3faa-4e32-82c4-c80436dd3d3b.png">


#### AWS Lambda

- Create a [lambda](https://docs.aws.amazon.com/lambda/latest/dg/gettingstarted-awscli.html) function for calling the model endpoint
- Modify the below function to fit your needs.

````
import boto3

def lambda_handler(event, context):
    # Create an SageMaker client
    sagemaker = boto3.client('sagemaker-runtime')

    # Set the endpoint name and the content type of the request
    endpoint_name = event.get('endpoint_name')
    content_type = '<content_type>'

    # Set the input data for the request
    input_data = event.get('input')

    # Make the inference request
    response = sagemaker.invoke_endpoint(
        EndpointName=endpoint_name,
        ContentType=content_type,
        Body=input_data
    )

    # Get the response from the endpoint
    result = response['Body'].read()

    # Return the result
    return {
        'result': result
    }

````

With the above end-point you can make call to your sagemaker end-point to get inferences on live data.

## Conclusion
This is a reference architecture for application-driven analytics. This architecture can be extended for any other use cases like predictive analytics, forecasting etc. by configuring the input data and sagemaker endpoint. Reach out to partners@mongodb.com for any queries.
