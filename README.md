# Diskoveror-TA-engine

The DisKoveror Text analytics engine(DTAE) is a software product developed by Serendio for extracting information such as Sentiment, Text categories, named entities etc from unstructured text. DTAE is useful in extracting information from Twitter streams, product reviews, emails, webpages and any other source of unstructured information. 


The workspace contains the core Text analytics engine which in turn optimally leverages multiple open source packages
to give several results structed and presented in a clean user-friendly format.


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

* Opensource softwares
* Serendio proprietary softwares
* Serendio domain specific softwares
* Third party API/Software integrations

New modules could be supported under these categories without disturbing the existing system in any manner.


### Packages : 

###### Stanford CoreNLP
###### Apache OpenNLP
###### DUKE
###### Proprietary Topic Modeling Algorithm
###### Proprietary Sentiment Analysis Algorithm
###### Proprietary Life Science Ontologies


### Compiling Diskoveror TA Engine

To package it in a single executable jar for distribution (.jar file), the following command has to be run from the command line. 

>       mvn clean compile assembly:single

### Example usage from tamanager

### Software requirements

## JDK
## maven
## Thrift servers running etc
