# BIZTRENDER-search-and-visualization-application-for-Yelp-dataset

**Implementation Summary**:  
• Developed a Java Spring Boot application with MVC backend and AngularJS frontend, for exploration and visualization of key entities from the Yelp Dataset 
• Implemented data retrieval from Elasticsearch and MongoDB, and interactive plots using Highcharts.js 
• Explored caching with Mongo GridFS and streaming HTTP responses for faster data processing 

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
> show dbs
> use biztrender
> db.business.createIndex({"business_id" : 1})
> db.review.createIndex({"business_id" : 1})
> db.tip.createIndex({"business_id" : 1})
> db.review.createIndex({"review_id" : 1})
> db.checkin.createIndex({"business_id" : 1})
```  
5) Configure BizTrenderWebApplication/src/main/resources/application.properties.  
```
server.port=8888  

security.basic.enabled=true
security.user.name=admin
security.user.password=admin@987

mongo.host=localhost
mongo.port=27017
mongo.db=biztrender

server.compression.enabled=true
server.compression.mime-types=application/json,application/xml,text/html,text/xml,text/plain,application/javascript,text/css,application/octet-stream,image/gif,image/png

elastic.host=localhost
elastic.port=9200
```  
5) Build and run the application.  
```
cd BizTrenderWebApplication
mvn clean install
java -jar target/BizTrenderWebApplication-0.0.1-SNAPSHOT.war &
```  
6) Visit ```http://localhost:8888/service/cache/clear``` on the browser to clear the response cache.  
7) Application is deployed at ```http://localhost:8888```.  

