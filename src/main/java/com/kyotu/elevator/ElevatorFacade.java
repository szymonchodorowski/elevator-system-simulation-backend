package com.kyotu.elevator;

import com.kyotu.elevator.dto.BuildingConfigDto;
import com.kyotu.elevator.dto.ElevatorStateDto;
import com.kyotu.elevator.dto.HallCall;
import com.kyotu.elevator.exceptions.InvalidFloorException;
import com.kyotu.elevator.request.ElevatorCallRequest;
import com.kyotu.elevator.request.HallCallRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ElevatorFacade {

	private final Building building;
	private final ElevatorService elevatorService;
	private final ElevatorSelector elevatorSelector;

	public BuildingConfigDto getBuildingConfig() {
		return new BuildingConfigDto(
				building.getNumberOfFloors(),
				building.getElevators().size()
		);
	}

	public List<ElevatorStateDto> getAllElevatorStates() {
		return building.getElevators().stream()
				.map(ElevatorMapper::toDto)
				.toList();
	}

	public SseEmitter subscribe() {
		return elevatorService.subscribe();
	}

	public void callElevator(HallCallRequest request) {
		validateFloor(request.floor());
		var hallCall = new HallCall(request.floor(), request.direction());
		var elevator = elevatorSelector.select(building.getElevators(), hallCall);
		elevator.addTargetFloor(request.floor());
	}

	public void selectFloor(int elevatorId, ElevatorCallRequest request) {
		validateFloor(request.targetFloor());
		var elevator = building.getElevator(elevatorId);
		elevator.addTargetFloor(request.targetFloor());
	}

	private void validateFloor(int floor) {
		if (floor < 0 || floor >= building.getNumberOfFloors()) {
			throw new InvalidFloorException(floor, building.getNumberOfFloors());
		}
	}
}