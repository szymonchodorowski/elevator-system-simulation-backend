package com.kyotu.elevator;

import com.kyotu.elevator.dto.BuildingConfigDto;
import com.kyotu.elevator.dto.ElevatorStateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ElevatorFacade {

	private final Building building;

	public BuildingConfigDto getBuildingConfig() {
		return new BuildingConfigDto(
				building.getNumberOfFloors(),
				building.getElevators().size()
		);
	}

	public List<ElevatorStateDto> getAllElevatorStates() {
		return building.getElevators().stream()
				.map(this::toDto)
				.toList();
	}

	private ElevatorStateDto toDto(Elevator elevator) {
		return new ElevatorStateDto(
				elevator.getId(),
				elevator.getCurrentFloor(),
				elevator.getDirection(),
				elevator.getDoorStatus(),
				elevator.getTargetFloors()
		);
	}
}