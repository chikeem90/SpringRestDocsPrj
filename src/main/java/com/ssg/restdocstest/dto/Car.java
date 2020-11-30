package com.ssg.restdocstest.dto;

import org.springframework.hateoas.ResourceSupport;

// hateoas를 지원하기 위해서는 ResourceSupport를 상속받아야함.
public class Car extends ResourceSupport {
	
	private Long carId;
	private String brand;
	private String name;
	
	public Car(long carId, String brand, String name) {
		super();
		this.carId = carId;
		this.brand = brand;
		this.name = name;
	}

	public Long getCarId() {
		return carId;
	}
	public void setCarId(Long carId) {
		this.carId = carId;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
