package com.emily.controller;

import java.time.LocalDate;
import java.util.Collection;

import javax.servlet.http.HttpSession;

import com.emily.entity.Station;
import com.emily.entity.StationList;
import com.emily.entity.Trip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.emily.entity.Customer;
import com.emily.service.ClientService;

@Controller
public class ClientController {
	
	@Autowired
	private ClientService service;

	//First page which is loaded
	// Ask user to inputs their ID to login or they can register a new account.
	@RequestMapping("/")
	public ModelAndView getUserIdPage() {
		return new ModelAndView("loginPage");
	}
	
	@RequestMapping("/addNewCustomer")
	public ModelAndView addNewCustomerController(@ModelAttribute("customer") Customer newCustomer, @RequestParam("dob") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		
		newCustomer.setCustomerDateOfBirth(date);
		Customer newRegisteredCustomer = service.addNewCustomer(newCustomer);
		
		String message;

		if (newRegisteredCustomer != null) {
			message = "New Account Created";
			modelAndView.setViewName("CustomerBalance");
			int registeredCustomerId = newRegisteredCustomer.getCustomerId();
			session.setAttribute("customerId", registeredCustomerId);
		} else {
			message = "You must be over 11 to register for a new account";
			modelAndView.setViewName("InputNewCustomer");
		}

		modelAndView.addObject("message", message);

		return modelAndView;

	}

	// Customer Account page
	@RequestMapping("/viewBalance")
	public ModelAndView accountController(@RequestParam("customerId") int id, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();

		Customer customer = service.loginCheck(id);

		if (customer != null) {
			session.setAttribute("customer", customer);
			session.setAttribute("customerName", customer.getCustomerFirstName());
			modelAndView.setViewName("viewBalance");
			modelAndView.addObject("stationObj",new Station());
		} else {
			modelAndView.addObject("message", "No account found with that Id, Please try again");
			modelAndView.setViewName("loginPage");
		}

		Collection<Station> stationList = service.getAllStations(); //List of all stations
		modelAndView.addObject("StationList", stationList); //adds the station object to website.
		
		return modelAndView; //returns everything
	}

	@ModelAttribute("stations")
	public Collection<Station> getStation(){
		Collection<Station> stationList  = service.getAllStations(); //all stations
		return stationList; //returns the station list
	}

	
	@RequestMapping("/topUp")
	public ModelAndView topUpCintroller(@RequestParam ("amount")double amount,@RequestParam("customerId") int customerId) {
		ModelAndView modelAndView = new ModelAndView();
		Customer customer = service.topUp(customerId, amount);
		
		if(customer !=null) {
			modelAndView.addObject("customer", customer);
			modelAndView.addObject("message","You successfully added:" + amount);
			modelAndView.setViewName("/viewBalance2");
			
		}
		else 
			modelAndView.addObject("message", "Sorry, transaction declined");
		    modelAndView.setViewName("/viewBalance2");
		    
		    return modelAndView;
	}
	
	
	@RequestMapping("/tapIn")
	public ModelAndView tapInController(@ModelAttribute("stationObj") Station station, HttpSession session) {
//	public ModelAndView tapInController() {
		ModelAndView modelAndView = new ModelAndView();
		
//		Customer customer = (Customer) session.getAttribute("customer");
//		
		int stationId = station.getStationId();
		System.out.println(stationId);
		session.setAttribute("stationId", stationId);
//		Trip trip = service.tapIn(customer, station.getStationId());
//		 
////		modelAndView.addObject("message", "You are travelling to "+station.getStationName());
//		
//		modelAndView.addObject("trip", trip);
		modelAndView.addObject("stationObj", new Station());
		modelAndView.setViewName("viewBalance2");
		
		return modelAndView;
	}	
}
