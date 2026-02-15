package com.kyotu.elevator;

import com.kyotu.elevator.dto.ElevatorStateDto;

class ElevatorMapper {

	static ElevatorStateDto toDto(Elevator elevator) {
		return new ElevatorStateDto(
				elevator.getId(),
				elevator.getCurrentFloor(),
				elevator.getDirection(),
				elevator.getDoorStatus(),
				elevator.getTargetFloors()
		);
	}
}