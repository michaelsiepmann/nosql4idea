# NoSql Plugin for IntelliJ IDEA version 0.2.0-SNAPSHOT

## Description
This plugin is a fork from [nosqlidea](https://github.com/dboissier/nosql4idea) and intends to integrate Elasticseach and Solr databases. Please note that everything is still under construction. The internal structure will be changed to have one codebase for all databases.

Currently it will support:

* MongoDB
* Couchbase
* Redis
* Elasticsearch
* Solr

## Current status: EAP

* [Download the current SNAPSHOT for Idea 14](https://github.com/dboissier/nosql4idea/raw/master/snapshot/nosql4idea-0.1.0-SNAPSHOT-distribution.zip)
* [Download the current SNAPSHOT for Idea 15](https://github.com/dboissier/nosql4idea/raw/master/snapshot/nosql4idea-0.1.0-SNAPSHOT-Idea15-distribution.zip)

## Plugin Compatibility

This plugin is built with JDK 1.8 and idea 17.1 version.

## Installation 
 
To install it : `Settings > Plugins > Install plugin from Disk`

## Configuration

On the right, you will see a new tool window named NoSql Explorer

![NoSqlBrowser](https://github.com/dboissier/nosql4idea/raw/master/doc/explorer.png)

* Click on the Wrench Icon from the toolbar and you will be redirected to the Plugin Settings.
* You can specify the mongo and redis CLI paths at the top of the panel
* To add a server, click on the **\[+\]** button and choose you database vendor

![SettingsAddAServer](https://github.com/dboissier/nosql4idea/raw/master/doc/settings_add_a_server.png)

* Click on the **OK** button and enter the settings of your database

![MongoSettings](https://github.com/dboissier/nosql4idea/raw/master/doc/mongo_settings.png)

* When all your dabatase are configured you should see then in the explorer panel
 
![NoSqlBrowserWithServers](https://github.com/dboissier/nosql4idea/raw/master/doc/explorer_with_servers.png)

## Viewing the Redis database content

Double click on the database icon from your redis server and the results will appear as a tab

![RedisResults](https://github.com/dboissier/nosql4idea/raw/master/doc/redis_results.png)

You can filter the results (Currently, it runs a `KEYS <filter>` command. A `SCAN <filter>` will replace it in the future for optimization purpose.)

Like the **Properties editor**, you can group your data by prefix. Click on the corresponding icon and then click on the Elipsis icon to set you separator

![RedisResultsGroupedByPrefix](https://github.com/dboissier/nosql4idea/raw/master/doc/redis_group_by_prefix.png)

## Viewing the Couchbase database content
 
Double click on the database icon from your couchbase server and the results will appear as a tab

![CouchbaseResults](https://github.com/dboissier/nosql4idea/raw/master/doc/couchbase_results.png)

**Important note**
To get the results from each bucket, an **Index** must be created. Otherwise an error message is raised.

## Things, I'm currently working on

* refactor the codebase to make it easier to update a function for all databases
* add import and export of data
* add a scripting-tool to make a bulk-update for all items of a database


