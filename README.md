# BizTrender-a-search-and-visualization-application-for-Yelp-dataset

**Dataset**: [Yelp Dataset Challenge](https://www.yelp.com/dataset)

**Requirements**:  
Java (>=1.8)  
Elasticsearch(>=7.9.1)  
MongoDB (>=3.6)  
Maven (>=3.3.9)  
Python (3+)  

**Deployment on localhost**:  
1) Start Elasticsearch service.  
```
sudo service elasticsearch start
curl -XGET "localhost:9200"
```
2) Start MongoDB daemon.  
```
sudo service mongod start
```
3) Index businesses on Elasticsearch. Update the path to business.json and run index_business.py.  
```
python3 <path to index_business.py>
curl -XGET "localhost:9200/_cat/indices"
```  
4) Import JSON files to MongoDB collections and create indexes.  
```
mongoimport --db biztrender --collection business --file business.json
mongoimport --db biztrender --collection review --file reviews.json
mongoimport --db biztrender --collection tip --file tips.json
mongoimport --db biztrender --collection checkin --file checkins.json
mongoimport --db biztrender --collection user --file users.json

mongo

db.business.createIndex({"business_id" : 1})
db.review.createIndex({"business_id" : 1})
db.tip.createIndex({"business_id" : 1})
db.review.createIndex({"review_id" : 1})
db.checkin.createIndex({"business_id" : 1})
```
