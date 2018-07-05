package com.example.polls.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.polls.payload.UserIdentityAvailability;
import com.example.polls.util.ProcessRunner;

@RestController
@RequestMapping("/api/envs")
public class EnvController {

	 @GetMapping("/uname")
	    public Object captureUname() {
		 String[] command = new String[] { "vm_stat" };
	        ProcessRunner runner = new ProcessRunner();
	        try {
				runner.byRuntime(command);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return "uname:uname";
	    }
}

