package tibano.service;

import java.util.ArrayList;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tibano.dto.UserInfo;
import tibano.entity.CarRepository;
import tibano.entity.User;
import tibano.entity.UserRepository;

@RestController
@CrossOrigin(origins = "http://localhost:8000")
public class UserService {
	private final UserRepository userRepository;
	private final CarRepository carRepository;
	
	@Autowired
	public UserService(UserRepository userRepository, CarRepository carRepository) {
		this.userRepository = userRepository;
		this.carRepository = carRepository;
	}
	@RequestMapping("/user")
	public UserInfo getUser(@RequestParam(name="userId") Long userId) {
		User user = userRepository.findOne(userId);
		if (user != null) {
			Set<String> licensePlates = carRepository.findLicensePlateByUserId(userId);
			return new UserInfo(user.getName(), new ArrayList<String>(licensePlates));
		}
		else 
			return null;
	 }
}
