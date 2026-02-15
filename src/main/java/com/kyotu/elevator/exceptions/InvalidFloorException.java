package com.kyotu.elevator.exceptions;

public class InvalidFloorException extends RuntimeException {

	public InvalidFloorException(int floor, int maxFloors) {
		super("Invalid floor: " + floor + ". Must be between 0 and " + (maxFloors - 1));
	}
}
