package cn.suming.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author suming
 * @since 2025/5/20 16:03
 */
@RestController
public class TestController {


	@GetMapping("/test")
	public String test(){
		return "test";
	}
}
