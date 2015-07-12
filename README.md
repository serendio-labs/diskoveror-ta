# **Overview**

DisKoveror is a Text Analytics engine developed by Serendio. Built on top of other open source packages, it provides a flexible and extensible way to extract Entities, Topics, Categories, Sentiments, and Keywords from unstructured text regardless of its source. Diskoveror has been used to mine brand sentiments from social media, understand customer satisfaction from emails,  extract topics from Tweets, auto-categorize legal documents and much more. .  

## **Key Functionalities**

  *  Sentiment analysis
  *  Topic detection
  *  Named Entity Recognition
  *  Keyword Extraction
    
### System Architecture

The architecture of the system is as given below.

![System Architecture](/Diskoveror_architecture.png "System Architechture")


The diskoveror-ta-engine leverages the below open source modules for its functionalities:

###### Name Entity extraction
* Stanford CoreNLP
* Apache OpenNLP
* DUKE

###### Sentiment extraction
* Twokenize
* Scikit-Learn
* RAKE
 
###### Topic extraction
* WORD2VEC
* Scikit-Learn
* RAKE
* Gensim

###### Keyword extraction
* RAKE

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

