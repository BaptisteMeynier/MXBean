package com.java.monitoring.jmx.agent;

import java.lang.management.ManagementFactory;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import com.java.monitoring.jmx.mbean.QueueSampler;

/**
 * Pour utiliser un connecteur RMI, il faut obligatoirement lancer un registre RMI 
 * en lancant la commande:
 * rmiregistry 9000
 * 
 * @author baptiste
 *
 */
public class LancerAgent {

	public static void main(String[] args) throws Exception  {
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer(); 


		ObjectName name = 
				new ObjectName("com.java.monitoring.jmx.mxbeans:type=QueueSampler"); 

		Queue<String> queue = new ArrayBlockingQueue<String>(10); 
		queue.add("Request-1"); 
		queue.add("Request-2"); 
		queue.add("Request-3"); 
		QueueSampler mxbean = new QueueSampler(queue); 

		mbs.registerMBean(mxbean, name); 

		// Creation et demarrage du connecteur RMI
		JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9000/server");
		JMXConnectorServer cs = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbs);
		cs.start();
		System.out.println("Lancement connecteur RMI "+url);


		System.out.println("Waiting..."); 
		Thread.sleep(Long.MAX_VALUE); 
	}
}
