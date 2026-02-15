package com.kyotu.elevator;

import com.kyotu.elevator.dto.BuildingConfigDto;
import com.kyotu.elevator.dto.ElevatorStateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ElevatorFacade {

	private final Building building;
	private final ElevatorService elevatorService;

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
}