/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2015 Preversoft
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.pangdata.apps.monitor;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pangdata.sdk.Pang;
import com.pangdata.sdk.http.PangHttp;
import com.pangdata.sdk.util.PangProperties;
import com.pangdata.sdk.util.SdkUtils;

public class WebMonitor {
  private static final Logger logger = LoggerFactory.getLogger(WebMonitor.class);
  private static boolean running = true;
  private static int TIMEOUT_VALUE = 5000;
  private static String tname = null;

  public static void main(String[] args) throws Exception {
    // Pang must be initialized first to use pang.properties by PangProperties
    final Pang pang = new PangHttp();
    
    Map<Integer, Map<String, String>> targets = extractTargets();

    if(targets.size() == 0) {
      logger.error("No target web server found");
      return;
    }
    final ExecutorService threadPools = Executors.newFixedThreadPool(targets.size(), new ThreadFactory() {
      
      public Thread newThread(final Runnable r) {
        Thread t = new Thread() {
          public void run() {
            r.run();
          }
        };
        
        t.setName(tname);
        return t;
      }
    });

 
    Iterator<Entry<Integer, Map<String, String>>> iterator = targets.entrySet().iterator();
    while (iterator.hasNext()) {
      Entry<Integer, Map<String, String>> next = iterator.next();
      final Map<String, String> target = next.getValue();
      tname = target.get("devicename");
      threadPools.execute(new Runnable() {

        public void run() {

          while (running) {
            // start timer
            long milliStart = System.currentTimeMillis();

            // do work to be timed
            try {
              getResponse(target.get("url"));
              // stop timer
              long milliEnd = System.currentTimeMillis();

              // report response times
              long milliTime = milliEnd - milliStart;
              Map<String, Long> data = new HashMap<String, Long>();

              data.put(target.get("devicename"), milliTime);
              pang.sendData(data);
            } catch (Throwable e) { 
              logger.error("Monitor has an error", e);
            }

            try {
              TimeUnit.MILLISECONDS.sleep(PangProperties.getPeriod());
            } catch (InterruptedException e) {
            }
          }
        }
        

        private void getResponse(String targetUrl) throws Exception {

          HttpParams myParams = new BasicHttpParams();
          HttpConnectionParams.setSoTimeout(myParams, 10000);
          HttpConnectionParams.setConnectionTimeout(myParams, 10000); // Timeout
          
          DefaultHttpClient httpClient = SdkUtils.createHttpClient(targetUrl);
          HttpGet get = new HttpGet(targetUrl);
          HttpResponse response = httpClient.execute(get);
          HttpEntity entity = response.getEntity();
          InputStream in = entity.getContent();
          
          try {
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = in.read(buffer, 0, buffer.length)) != -1) {}
          } finally {
            if (in != null) {
              in.close();
            }
          }
        }
        
/*        private void getResponse(String targetUrl) throws MalformedURLException, IOException {

          URL url = new URL(targetUrl);
          
          URLConnection conn = null;
          
          if(targetUrl.startsWith("https")) {
            conn = (HttpsURLConnection)url.openConnection();
          } else {
            conn = url.openConnection();
          }
          conn.setConnectTimeout(TIMEOUT_VALUE);
          conn.setReadTimeout(TIMEOUT_VALUE);
          
          InputStream in = conn.getInputStream();

          try {
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = in.read(buffer, 0, buffer.length)) != -1) {}
          } finally {
            if (in != null) {
              in.close();
            }
          }
        }*/

      });

      Runtime.getRuntime().addShutdownHook(new Thread() {
        @Override
        public void run() {
          running = false;
          threadPools.shutdown();
        }
      });
    }
  }

  private static Map<Integer, Map<String, String>> extractTargets() {
    Properties properties = PangProperties.getProperties();
    Set<Entry<Object, Object>> entrySet = properties.entrySet();
    Iterator<Entry<Object, Object>> iterator = entrySet.iterator();

    Map<Integer, Map<String, String>> targets = new HashMap<Integer, Map<String, String>>();

    while (iterator.hasNext()) {
      Entry<Object, Object> next = iterator.next();
      String key = (String) next.getKey();
      if (key.startsWith("webmonitor")) {
        String sub1 = key.substring(key.indexOf(".") + 1);
        int indexOf = sub1.indexOf(".");
        String number = sub1.substring(0, indexOf);
        Map<String, String> target = targets.get(Integer.valueOf(number));
        if (target == null) {
          target = new HashMap<String, String>();
          targets.put(Integer.valueOf(number), target);
        }

        target.put(sub1.substring(sub1.lastIndexOf(".") + 1).trim(), (String) next.getValue());
      }
    }

    return targets;
  }

}
