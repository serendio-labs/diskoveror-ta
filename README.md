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


The main idea is to segregate our text analytics capabilities into four divisions namely :

* Opensource Core softwares
* Serendio proprietary softwares
* Serendio domain specific softwares
* Third party API/Software integrations

New components could be added or removed under these categories without disturbing the existing system in any manner, since the pipeline executor is coupled to the interface and not to the implementation. This could be especially useful in updating the open source softwares since they improve significantly over time.


### Packages Used : 

###### Stanford CoreNLP
###### Apache OpenNLP
###### DUKE
###### Proprietary Topic Modeling Algorithm
###### Proprietary Sentiment Analysis Algorithm
###### Proprietary Life Science Ontologies




### Compiling Diskoveror TA Engine

To package it in a single executable jar for distribution (.jar file), the following command has to be run from the command line. 

>       mvn clean compile assembly:single
