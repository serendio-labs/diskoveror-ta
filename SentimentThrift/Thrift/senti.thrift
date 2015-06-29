namespace py sentiments

struct SentiRequestObject {
  1: required string mainText;
  2: optional string textType = "microblogs";
  3: optional string title = "";
  4: optional string middleParas = "";
  5: optional string lastPara = "";
  6: optional string topDomain = "";
  7: optional string subDomain = "";
  }

service Sentiments {
  void ping(),
  i32 getSentimentScore(1: SentiRequestObject obj),
}
