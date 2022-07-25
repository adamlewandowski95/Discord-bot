## General info
This project is discord bot created for Casper Army

## Setup
In `BotConfiguration` class:

* `botName` field should be changed to the target bot name
* `token` field should contain token generated in discord developers portal
* `authorizedRoles` field could be extended by roles that should have access to admin commands
* `listenedCategories` map should be filled with categories and channels to be listened
  * categories are key of the map
  * value of is the list containing names of the channels to be listened on specific category if list is empty all channels on specific category will be listened
  
Before installation there should be already [points-counter](https://github.com/adamlewandowski95/CasperArmy/tree/master) app running and in db already initialized functions.

SQL functions are in the resources of points-counter app.