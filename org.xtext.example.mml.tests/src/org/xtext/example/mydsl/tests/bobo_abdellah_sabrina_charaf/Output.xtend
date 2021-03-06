package org.xtext.example.mydsl.tests.bobo_abdellah_sabrina_charaf

import java.util.HashMap
import java.util.Map
import org.xtext.example.mydsl.mml.FrameworkLang
import org.xtext.example.mydsl.mml.MLAlgorithm

class Output {
	
	public MLAlgorithm mlAlgorithm;
	public FrameworkLang frameworkLang;
	public String fileLocation;
	public long timestamp;
	public Map<String, Double> validationMetric_result = new HashMap<String,Double>();
	
	override String toString(){
		var String result="";
		result +="[ Data: "+fileLocation+ ", Algorithm : "+mlAlgorithm.class.getInterfaces.get(0).simpleName+ 
				", Framework: "+frameworkLang+ ", ExectutionTime: "+timestamp+"]";
		
		for(Map.Entry e: validationMetric_result.entrySet()) {
			val String key = e.key.toString();
			val double value = Double.valueOf(e.value.toString);
			
			result += ", Metric : "+key+ ", value: "+value
			
		}
		return result;
	}
}
