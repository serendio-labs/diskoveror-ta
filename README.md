# **Overview**

DisKoveror is a Text Analytics engine developed by Serendio. Built on top of other open source packages, it provides a flexible and extensible way to extract Entities, Topics, Categories, Sentiments, and Keywords from unstructured text regardless of its source. Diskoveror has been used to mine brand sentiments from social media, understand customer satisfaction from emails,  extract topics from Tweets, auto-categorize legal documents and much more. .  

## **Key Functionalities**

  *  Sentiment analysis
  *  Topic detection
  *  Named Entity Recognition
  *  Coreference Resolution
  *  Keyword Extraction
    
## **Getting Started**

### **Software Requirements**

 * JDK (Version 7 or above)
      


 * Maven (Apache Maven 3.0.5 or above)
      


 * Thrift server (Apache Thrift 0.9.2)



#### **Compiling Diskoveror TA Engine**
To package it in a single executable jar for distribution (.jar file), the following command has to be run from the command line.
>      /diskoveror-ta$ mvn package dependency:copy-dependencies clean

#### **Execution**
To execute the .jar file, the following command has to be run from the command line.
>      /diskoveror-ta/target$ java -Xmx2048m -jar diskoverorta-0.1.jar


### System Architecture

The architecture of the system is as given below.

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

New modules could be supported under these categories in a plugin like manner.

