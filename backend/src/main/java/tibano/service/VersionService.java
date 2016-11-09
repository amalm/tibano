package tibano.service;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:8000")
public class VersionService {
	 private static final String VERSION = "100";

	@RequestMapping("/getVersion")
	public String getVersion() {
		return VERSION;
	 }
}
