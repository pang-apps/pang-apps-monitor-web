# Monitor your web server's performance and play with Prever.io
Every 10 seconds monitor access your target url and send response time. Just install this application and enjoy it in Prever.io

## Screen shot
<img src="https://github.com/prever-apps/prever-apps-monitor-web/blob/master/screen-shot.png" width="400">
<img src="https://github.com/prever-apps/prever-apps-monitor-web/blob/master/screen-shot-2.png" width="400">
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
Download a <a href="https://github.com/prever-apps/prever-apps-monitor-web/releases/download/1.0/prever-apps-monitor-web.zip">Prever web server monitoring application</a> file and unzip it.

###### Linux ######
``` 
wget https://github.com/prever-apps/prever-apps-monitor-web/releases/download/1.0/prever-apps-monitor-web.tar
tar -xvf prever-apps-monitor-web.tar
``` 
##### Step 2: Configure prever.properties file #####
cd prever-apps-monitor-web/conf

Confgiure your account and user key in prever.properties.
```bash
prever.username=your username in prever.io
prever.userkey=your user key in prever.io
``` 
Declare your target url to monitor.
```bash
#Web Monitor application reserved properties
#Add more url by index after 'webmonitor'
webmonitor.1.url = https://www.google.com
webmonitor.1.url.devicename = google_response_time
webmonitor.2.url = http://www.naver.com
webmonitor.2.url.devicename = naver_response_time
webmonitor.3.url = http://www.yahoo.com
webmonitor.3.url.devicename = yahoo_response_time
webmonitor.4.url = http://www.alibaba.com/
webmonitor.4.url.devicename = alibaba_response_time
``` 

Note: User key can be found in your profile of Prever.io
##### Step 3: Run #####
###### Windows ######
``` 
prever/prever.bat
``` 
###### Linux ######
``` 
prever/./prever.sh
``` 
##### Step 4: Access your devices #####
Register your device in Prever.io

Login your account.
See main dashborad and you can find unregistered device.
Register the unregistered device.

##### Step 5: You are happy to play with your data #####
Wow!! all done. Enjoy and play with your device and your data.
