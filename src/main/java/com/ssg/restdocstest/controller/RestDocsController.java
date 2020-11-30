package com.ssg.restdocstest.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssg.restdocstest.dto.Car;

@RestController
public class RestDocsController {
	
	private List<Car> carList = new ArrayList<Car>();
	
	@RequestMapping("/cars")
	public List<Car> getCars(HttpServletRequest request, HttpServletResponse response) {
		if (carList.size() == 0) {
			initCarList();			
		}
		String id = request.getParameter("id");
		List<Car> carsWithIdList = new ArrayList<Car>();
		response.setHeader("testHeaderValue", "qpowqjgkldqiop");
		if (id == null) {
			return carList;			
		} else {
			for (Car car : carList) {
				if (car.getCarId() == Long.parseLong(id)) {
					carsWithIdList.add(car);
				}
			}
			return carsWithIdList;
		}
	}
	
	@RequestMapping("/cars/{brand}")
	public List<Car> getCarsWithBrand(@PathVariable("brand") String brand) {
		List<Car> carsWithBrandList = new ArrayList<Car>();
		for (Car car : carList) {
			if (car.getBrand().equals(brand)) {
				carsWithBrandList.add(car);
			}
		}
		return carsWithBrandList;
	}
	
	@RequestMapping(value="/carsWithLinks")
	public Car getCarsWithLinks() {
		Car car6 = new Car(6, "SsangYong", "Tivoli");
		car6.add(linkTo(methodOn(RestDocsController.class).getCarsWithLinks()).withSelfRel());
		return car6;
	}
	
	private void initCarList() {
		Car car1 = new Car(1, "BMW", "X3");
		Car car2 = new Car(2, "HYUNDAI", "SONATA");
		Car car3 = new Car(3, "KIA", "K5");
		Car car4 = new Car(4, "AUDI", "A6");
		Car car5 = new Car(5, "Benz", "C class");
		
		carList.add(car1);
		carList.add(car2);
		carList.add(car3);
		carList.add(car4);
		carList.add(car5);
	}
}
