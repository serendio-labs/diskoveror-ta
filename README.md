# Diskoveror-TA-engine

The DisKoveror Text analytics engine(DTAE) is a software product developed by Serendio for extracting information such as Sentiment, Text categories, named entities etc from unstructured text. DTAE is useful in extracting information from Twitter streams, product reviews, emails, webpages and any other source of unstructured information. 


The workspace contains the core Text analytics engine which in turn optimally leverages multiple open source packages
to give several results structured and presented in a clean user-friendly format.

## **Getting Started**

### **Software requirements**

#### JDK
>       Version 7 build 79 or above


#### Maven
>       Apache Maven 3.0.5 or above


#### Thrift servers
[Get Thrift Here](https://thrift.apache.org/)


#### **Compiling Diskoveror TA Engine:**
To package it in a single executable jar for distribution (.jar file), the following command has to be run from the command line.
>      root@root-Motherboard-H61MAT-D-E:~/Documents/diskoveror-ta$ mvn package dependency:copy-dependencies clean

#### **Execution:**
To execute the .jar file, the following command has to be run from the command line.
>      root@root-Motherboard-H61MAT-D-E:~/Documents/diskoveror-ta/target$ java -Xmx2048m -jar diskoverorta-0.1.jar

### Features

* Sentiment Extraction
* Topic detection
* Named Entity Recognition
* Java based API
* Coreference Resolution
* Sentiment Polarity



### System Architecture

The intended architecture of the system is as given below.

![System Architecture](/Diskoverer_architecture.png "System Architechture")


The diskoveror-ta-engine leverages modules under the below independent categories :

###### Opensource softwares
* Stanford CoreNLP
* Apache OpenNLP
* DUKE

###### Serendio proprietary softwares
* Topic Modeling Algorithm
* Sentiment Analysis Algorithm
 
###### Serendio domain specific softwares
* Life Science Ontologies
* Legal Ontologies

###### Third party API/Software integrations

New modules could be supported under these categories without disturbing the existing system in any manner.

