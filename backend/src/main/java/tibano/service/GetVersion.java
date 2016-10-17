package tibano.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetVersion {
	 private static final String VERSION = "1.0.0";

	@RequestMapping("/getVersion")
	    public String getVersion() {
		 return VERSION;
	 }
}
