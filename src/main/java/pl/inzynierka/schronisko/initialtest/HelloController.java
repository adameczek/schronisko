package pl.inzynierka.schronisko.initialtest;

import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RolesAllowed({"USER"})
public class HelloController {
	private final HelloService helloService;

	@GetMapping("/hello")
	public ResponseEntity<List<Hello>> hello() {
		helloService.save(new Hello(null, "dsodaso"));

		return ResponseEntity.ok(helloService.findAll());
	}
}
