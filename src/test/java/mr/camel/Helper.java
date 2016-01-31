package mr.camel;

import org.apache.camel.Body;

public class Helper {

	public Helper() {
	}

	public String convert(@Body User str) {
		return "converted " + str.getName();
	}
}