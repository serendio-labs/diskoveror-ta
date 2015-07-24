'''
Copyright 2015 Serendio Inc.
Author - Satish Palaniappan

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and limitations under the License.
'''
__author__ = "Satish Palaniappan"


# SET this path to the senti modules base path
basePath = "Sentiment/"


#DO NOT change below paths

# Social Text Filter Path

SocialFilter = "/"

general = "model/general/"
microblog = "model/microblogs/"
review_Base = "model/review/"

# title, review_text
# 0 - No
# 1 - Yes

review = dict()

products = dict()
electronics_tech = dict()
movie = dict()
services = dict()

products["apparel"] = [1,"products/apparel/title/","products/apparel/review_text/"]
products["automotive"] = [1,"products/automotive/title/","products/automotive/review_text/"]
products["baby"] = [1,"products/baby/title/","products/baby/review_text/"]
products["beauty"] = [1,"products/beauty/title/","products/beauty/review_text/"]
products["food"] = [1,"products/food/title/","products/food/review_text/"]
products["grocery"] = [1,"products/grocery/title/","products/grocery/review_text/"]
products["health"] = [1,"products/health_&_personal_care/title/","products/health_&_personal_care/review_text/"]
products["jewelry"] = [1,"products/jewelry_&_watches/title/","products/jewelry_&_watches/review_text/"]
products["housewares"] = [1,"products/kitchen_&_housewares/title/","products/kitchen_&_housewares/review_text/"]
products["magazines"] = [1,"products/magazines/title/","products/magazines/review_text/"]
products["musical_instruments"] = [1,"products/musical_instruments/title/","products/musical_instruments/review_text/"]
products["office_products"] = [1,"products/office_products/title/","products/office_products/review_text/"]
products["outdoor_living"] = [1,"products/outdoor_living/title/","products/outdoor_living/review_text/"]
products["sports"] = [1,"products/sports_&_outdoors/title/","products/sports_&_outdoors/review_text/"]
products["tools"] = [1,"products/tools_&_hardware/title/","products/tools_&_hardware/review_text/"]
products["toys"] = [1,"products/toys_&_games/title/","products/toys_&_games/review_text/"]

electronics_tech["camera"] = [1,"electronics_tech/camera_&_photo/title/","electronics_tech/camera_&_photo/review_text/"]
electronics_tech["cell_phones"] = [1,"electronics_tech/cell_phones_&_service/title/","electronics_tech/cell_phones_&_service/review_text/"]
electronics_tech["computer"] = [1,"electronics_tech/computer_&_video_games/title/","electronics_tech/computer_&_video_games/review_text/"]
electronics_tech["general"] = [1,"electronics_tech/main/title/","electronics_tech/main/review_text/"]
electronics_tech["software"] = [1,"electronics_tech/software/title/","electronics_tech/software/review_text/"]

movie["dvd"] = [1,"movie/dvd/title/","movie/dvd/review_text/"]
movie["general"] = [0,"movie/main/"]
movie["video"] = [1,"movie/video/title/","movie/video/review_text/"]

services["automotive"] = [0,"services/automotive/"]
services["beauty"] = [0,"services/beauty/"]
services["healthcare"] = [0,"services/healthcare/"]
services["other"] = [0,"services/other/"]

review["books"] = [1,"books/title/","books/review_text/"]
review["food"] = [0,"food/"]
review["hotels_bars"] = [0,"hotels_bars/"]
review["music_albums"] = [1,"music_albums/title/","music_albums/review_text/"]
review["places"] = [0,"places/"]
review["restaurants"] = [0,"restaurants/"]
review["travel_tour"] = [0,"travel_tour/"]


review["products"] = products
review["electronics_tech"] = electronics_tech
review["movie"] = movie
review["services"] = services


# # Review Categories
# review
# 	products
# 		apparel
# 		automotive
# 		baby
# 		beauty
# 		food
# 		grocery
# 		health
# 		jewelry
# 		housewares
# 		magazines
# 		musical_instruments
# 		office_products
# 		outdoor_living
# 		sports
# 		tools
# 		toys
# 	electronics_tech
# 		general
# 		camera
# 		cell_phones
# 		computer
# 		software
# 	movie
# 		general
# 		dvd
# 		video
# 	services
# 		automotive
# 		beauty
# 		healthcare
# 		other
# 	books
# 	food
# 	hotels_bars
# 	music_albums
# 	places
# 	restaurants
# 	travel_tour
