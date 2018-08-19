package com.java.monitoring.jmx.client;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import com.java.monitoring.jmx.mbean.QueueSample;
import com.java.monitoring.jmx.mbean.QueueSamplerMXBean;


/**
 * A lancer apres le main LancerAgent 
 * @author baptiste
 *
 */
public class ClientJMXAvecRMI {
  public static void main(String[] args) {
    MBeanServerConnection mbsc = null;
    JMXConnector connecteur = null;

    ObjectName name = null;
    try {

      JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9000/server");

      connecteur = JMXConnectorFactory.connect(url, null);

      mbsc = connecteur.getMBeanServerConnection();

      name = new ObjectName("com.java.monitoring.jmx.mxbeans:type=QueueSampler"); 
      QueueSamplerMXBean proxy = JMX.newMXBeanProxy(mbsc, name, QueueSamplerMXBean.class); 
      QueueSample queueSample = proxy.getQueueSample(); 
      System.out.println("Size: " + queueSample.getSize());
      
      System.out.println("Launch clear:");
      proxy.clearQueue();
      queueSample = proxy.getQueueSample(); 
      System.out.println("Size: " + queueSample.getSize());
 

    } catch (MalformedObjectNameException e) {
      e.printStackTrace();
    } catch (NullPointerException e) {
      e.printStackTrace();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (mbsc != null) {
        try {
          connecteur.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

  }
}
