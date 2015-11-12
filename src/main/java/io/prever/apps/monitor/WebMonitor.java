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

package io.prever.apps.monitor;

import io.prever.sdk.Prever;
import io.prever.sdk.http.PreverHttp;
import io.prever.sdk.util.PreverProperties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebMonitor {
  private static final Logger logger = LoggerFactory.getLogger(WebMonitor.class);
  private static boolean running = true;

  public static void main(String[] args) throws Exception {
    //Prever must be initialized first to use prever.properties by PreverProperties
    final Prever prever = new PreverHttp();

    Map<Integer, Map<String, String>> targets = extractTargets();

    final ExecutorService threadPools = Executors.newFixedThreadPool(targets.size());

    Iterator<Entry<Integer, Map<String, String>>> iterator = targets.entrySet().iterator();
    while (iterator.hasNext()) {
      Entry<Integer, Map<String, String>> next = iterator.next();
      final Map<String, String> target = next.getValue();
      threadPools.execute(new Runnable() {

        public void run() {

          while (running ) {
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
              prever.sendData(data);
            } catch (Exception e) {
              logger.error("Monitor has an error", e);
            }

            try {
              TimeUnit.SECONDS.sleep(PreverProperties.getPeriod());
            } catch (InterruptedException e) {
            }
          }
        }

        private void getResponse(String targetUrl) throws MalformedURLException, IOException {
          URL url = new URL(targetUrl);
          URLConnection conn = url.openConnection();
          BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
          while (in.readLine() != null) {
          }
          in.close();
        }

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
    Properties properties = PreverProperties.getProperties();
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
