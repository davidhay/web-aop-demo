package com.ealanta;

import java.util.Random;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

	
	String names[] = {"Mary","Mungo","Midge"};
	
	Random rand = new Random();

	@GetMapping(path="/hello")
	public String greet() {
		int idx = rand.nextInt(3);
		return names[idx];
	}
}
