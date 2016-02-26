# Monitor your web server's performance and play with Pangdata.com

Every 10 seconds monitor access your target url and send response time. Just install this application and enjoy it in Pangdata.com
You can monitor using your mobile phone or tablet any devices.

## Screen shot
###### Realtime monitoring ######
![Realtime monitoring](https://github.com/pang-apps/pang-apps-monitor-web/blob/master/screen-shot.png "Realtime monitoring")
![Realtime monitoring](https://github.com/pang-apps/pang-apps-monitor-web/blob/master/screen-shot-3.png "Realtime monitoring")

###### Analysis ######
![Analysis](https://github.com/pang-apps/pang-apps-monitor-web/blob/master/screen-shot-2.png "Analysis")

## Getting Started
#### Sign up for Pangdata.com ####
Before you begin, you need an Pangdata.com account. 
Please visit <a href="http://pangdata.io" target="_blank">http://pangdata.io</a> and create an account and retrieve your user key in user profile.

#### Minimum requirements ####
To run the application you will need **Java 1.5+**.

#### Installation ####
Very easy to install ^^.

##### Step 1 #####

###### Windows ######
Download a <a href="https://github.com/pang-apps/pang-apps-monitor-web/releases/download/1.0.4/pang-apps-monitor-web.zip">Pang Data web server monitoring application</a> file and unzip it.

###### Linux ######
``` 
wget https://github.com/pang-apps/pang-apps-monitor-web/releases/download/1.0.4/pang-apps-monitor-web.tar
tar -xvf pang-apps-monitor-web.tar
``` 
##### Step 2: Configure pang.properties file #####
cd pang-apps-monitor-web/conf

Confgiure your account and user key in pang.properties.
```bash
pang.username=your username in pangdata.com
pang.userkey=your user key in pangdata.com
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

Note: User key can be found in your profile of Pangdata.com
##### Step 3: Run #####
###### Windows ######
``` 
pang-apps-monitor-web/pang.bat
``` 
###### Linux ######
``` 
pang-apps-monitor-web/./pang.sh
``` 
##### Step 4: Access your devices #####
Register your device in Pangdata.com

Login your account.
See main dashborad and you can find unregistered device.
Register the unregistered device.

##### Step 5: You are happy to play with your data #####
Wow!! all done. Enjoy and play with your device and your data.
