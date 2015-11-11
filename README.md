# Monitor your web server's performance and play with Prever.io
Every 10 seconds monitor access your target url and send response time. Just install this application and enjoy it in Prever.io

## Getting Started
#### Sign up for Prever.io ####
Before you begin, you need an Prever.io account. 
Please visit <a href="http://prever.io" target="_blank">http://prever.io</a> and create an account and retrieve your user key in user profile.

#### Minimum requirements ####
To run the application you will need **Java 1.5+**.

#### Installation ####
Very easy to install ^^.

##### Step 1 #####

###### Windows ######
Download a <a href="https://github.com/prever-apps/prever-apps-monitor-web/releases/download/1.0/prever-apps-monitor-web.zip">zip</a> file and unzip it.

###### Linux ######
``` 
wget https://github.com/prever-apps/prever-apps-monitor-web/releases/download/1.0/prever-apps-monitor-web.tar
tar -xvf prever-apps-monitor-web.tar
``` 
##### Step 2: Configure your account #####
cd prever/conf

Confgiure your account and user key in prever.properties.
```bash
prever.username=your username in prever.io
prever.userkey=your user key in prever.io
``` 
Declare your target url to monitor.
```bash
#Web Monitor application reserved properties
webmonitor.responseTime.devicename = response_time
webmonitor.url = www.naver.com
``` 

Note: User key can be found in your profile of Prever.io
##### Step 4: Run #####
###### Windows ######
``` 
prever/prever.bat
``` 
###### Linux ######
``` 
prever/./prever.sh
``` 
##### Step 5: Access to prever.io #####
Register your device in Prever.io

Login your account.
See main dashborad and you can find unregistered device.
Register the unregistered device.

##### Step 6: Happy to paly #####
Wow!! all done. Enjoy and play with your device and your data.
