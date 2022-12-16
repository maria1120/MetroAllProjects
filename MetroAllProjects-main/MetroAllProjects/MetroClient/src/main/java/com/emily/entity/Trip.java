package com.emily.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Trip {	

	@Id
    @GeneratedValue
    private int customerId;
    private int tripId;
    private int swipeInStationId;
    private int swipeOutStationId;
    private LocalDateTime swipeInDateAnTime;
    private LocalDateTime swipeOutDateAndTime;
    private double tripFare;
    
    
	public Trip(int customerId,int swipeInStation, LocalDateTime swipeInDateAnTime) {
		
		this.customerId = customerId;
		this.swipeInStationId = swipeInStation;
		this.swipeInDateAnTime = swipeInDateAnTime;
	}
}
