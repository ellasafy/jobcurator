package com.curator.jobcurator.job;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

public class JobParseTest {

	@Test
	public void JobParse() {
		try {
		Job job = new Job.Builder()
		                 .Id(22)
		                 .CreatedTime(System.currentTimeMillis())
		                 .Name("name")
		                 .builder();
		
		Map<String, Object> mp = new HashMap<String, Object>();
		mp.put("type", "add");
		mp.put("job", job);
	      ObjectMapper mapper = new ObjectMapper();
	    String str = mapper.writeValueAsString(mp);
	    
	    System.out.println(str);
	    
	    JsonNode node = mapper.readTree(str.getBytes());
	    JsonNode  txt = node.findValue("job");
	    System.out.println(txt);
	    Job job2 = mapper.readValue(txt, Job.class);
	    System.out.println(job2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
