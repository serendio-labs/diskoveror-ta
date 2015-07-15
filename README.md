# **Overview**

DisKoveror is a Text Analytics framework developed by Serendio. Built on top of other open source packages, it provides a flexible and extensible way to extract Entities, Topics, Categories, Sentiments, and Keywords from unstructured text regardless of its source. The key advantage of DisKoveror over the numerous open source options is it provides access to the best-of-breed components through a plug and play approach and a unified programming interface.  DisKoveror has also improved the output quality, in some cases, through Training sets, domain specific ontology, and folksonomy. 

DisKoveror has been used to mine brand sentiments from social media, understand customer satisfaction from emails, extract topics from Tweets, compute social influence score, auto-categorize legal documents and much more. 

DisKoveror can be accessed through Java APIs or a RESTful interface.

License: Apache 2.0

## **Key Functionalities**

  *  [Sentiment analysis](https://github.com/serendio-labs/diskoveror-ta/wiki/Sentiment-Extraction)
  *  [Topic detection](https://github.com/serendio-labs/diskoveror-ta/wiki/Topic-Detection)
  *  [Named Entity Extraction](https://github.com/serendio-labs/diskoveror-ta/wiki/Name-Entity-Extraction)
    
### System Architecture

The architecture of the system is as given below.

![System Architecture](/Diskoveror_architecture.png "System Architechture")

DisKoveror supports Java APIs and a RESTful interface.

#####**DisKoveror leverages the open source modules shown below:**

######**Name Entity extraction**
* Stanford CoreNLP
* Apache OpenNLP
* DUKE

######**Sentiment extraction**
* Twokenize
* Scikit-Learn
* RAKE
 
######**Topic extraction**
* WORD2VEC
* Scikit-Learn
* RAKE
* Gensim

######**Keyword extraction**
* RAKE

###**Getting Started**

##### **Software Requirements**

 * JDK (Version 7 or above)
 * Maven (Apache Maven 3.0.5 or above)
 * Thrift server (Apache Thrift 0.9.2)
 * Python (version 2.7.X)
 * Pip (version 7.1.X)

##### **Workspaces to Download**

 * [DisKoveror-ta](https://github.com/serendio-labs/diskoveror-ta/archive/master.zip) 
 * [DisKoveror-ml-server](https://github.com/serendio-labs/diskoveror-ml-server/archive/master.zip)

##### **Starting Thrift servers for Sentiment and Topics in DisKoveror-ml-server**

The requirements.txt file specifies the software packages along with their versions to be installed. Execute the
below command to install all python related dependencies for the Sentiment and Topics.

>     /diskoveror-ml-server$ sudo pip install -r requirements.txt

Start the thrift servers for Topics and Sentiments 

>     /diskoveror-ml-server/TopicThrift$ python server.py
>     /diskoveror-ml-server/SentimentThrift/Thrift$ python server.py

## **Compiling DisKoveror TA Engine**
To package it in a single executable jar for distribution (.jar file), the following command has to be run from the command line.
>      /diskoveror-ta$ mvn package dependency:copy-dependencies clean

## **Execution**
To display the output, run the following command from command line.
>      /diskoveror-ta$ java -Xmx2048m -jar diskoverorta-0.1.jar 


