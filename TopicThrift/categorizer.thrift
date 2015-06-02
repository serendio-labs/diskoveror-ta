namespace py categorizer

service Categorizer {
  void ping(),
  string getTopic(1:string msg)
}

