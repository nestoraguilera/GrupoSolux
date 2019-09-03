package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.services.DataAcquired;

@Controller
public class AppController {

	@Autowired
	DataAcquired dataAcquired;

	public AppController() {
	}

	@GetMapping("/")
	public String getRoot(Model model) {
		model.addAttribute("acquired", dataAcquired.acquired());
		model.addAttribute("MfaName", dataAcquired.getMfaName());
		model.addAttribute("MfaSpecie", dataAcquired.getMfaSpecie());
		model.addAttribute("vehicles", dataAcquired.getVehicles());
		return "rootView";
	}
}
