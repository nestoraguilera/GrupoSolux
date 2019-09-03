/**
 * 
 */
package com.example.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pojos.Vehicle;

/**
 * @author nestor
 *
 */
@Service
public class DataAcquired {

	// MFA most frequently appeared
	private String mfaName;
	private String mfaSpecie;
	private List<Vehicle> vehicles;

	/**
	 *ctor 
	 */
	public DataAcquired() {
	}

	public Boolean acquired() {
		if (getMfaName() != null && getVehicles() != null)
			return true;
		return false;
	}

	public String getMfaName() {
		return mfaName;
	}

	public void setMfaName(String mfaName) {
		this.mfaName = mfaName;
	}

	public String getMfaSpecie() {
		return mfaSpecie;
	}

	public void setMfaSpecie(String mfaSpecie) {
		this.mfaSpecie = mfaSpecie;
	}

	public List<Vehicle> getVehicles() {
		return vehicles;
	}

	public void setVehicles(List<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}

}
