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
import io.prever.sdk.callback.MultipleDataCallback;
import io.prever.sdk.http.PreverHttp;
import io.prever.sdk.util.PreverProperties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebMonitor {
  private static final Logger logger = LoggerFactory.getLogger(WebMonitor.class);

  public static void main(String[] args) throws Exception {
    final Prever prever = new PreverHttp();
    final String targetUrl = (String) PreverProperties.getProperty("webmonitor.url");
    final String devicename = (String) PreverProperties.getProperty("webmonitor.responseTime.devicename");
    
    prever.startTimerTask(new MultipleDataCallback() {
      public void onSuccess(Object sent) {}

      public boolean isRunning(int sentCount) {
        return true;
      }

      public Object getData() {
        // start timer
        long milliStart = System.currentTimeMillis();

        // do work to be timed
        try {
          getResponse(targetUrl);
        } catch (Exception e) {
          logger.error("Response error", e);
        }
        // stop timer
        long milliEnd = System.currentTimeMillis();

        // report response times
        long milliTime = milliEnd - milliStart;
        String rt_millitime = String.format("%,.3f", milliTime / 1.0);
        Map<String, String> data = new HashMap<String, String>();
        data.put(devicename, rt_millitime);

        return data;
      }

      private void getResponse(String targetUrl) throws MalformedURLException, IOException {
        URL url = new URL(targetUrl);
        URLConnection conn = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        while (in.readLine() != null) {
        }
        in.close();
      }
      
    }, PreverProperties.getPeriod(), TimeUnit.MILLISECONDS);

  }

}
