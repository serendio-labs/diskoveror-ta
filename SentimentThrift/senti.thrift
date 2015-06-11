namespace py sentiments

service Sentiments {
  void ping(),
  double getSentiment(1:string msg)
}

