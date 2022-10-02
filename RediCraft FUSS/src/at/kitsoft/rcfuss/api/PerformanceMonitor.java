package at.kitsoft.rcfuss.api;

import java.lang.management.ManagementFactory;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

public class PerformanceMonitor {
	
	public static double getProcessCpuLoad() {
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		ObjectName name;
		AttributeList list = null;
		try {
			name = ObjectName.getInstance("java.lang:type=OperatingSystem");
			list = mbs.getAttributes(name, new String[] { "ProcessCpuLoad"});
		} catch (MalformedObjectNameException | NullPointerException | InstanceNotFoundException | ReflectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		if(list.isEmpty()) return Double.NaN;
		
		Attribute att = (Attribute)list.get(0);
		Double value = (Double)att.getValue();
		if(value == -1.0) return Double.NaN;
		return ((int) (value* 1000) / 10.0);
	}
}