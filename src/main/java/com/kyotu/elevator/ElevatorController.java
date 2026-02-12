package com.kyotu.elevator;

import com.kyotu.elevator.dto.BuildingConfigDto;
import com.kyotu.elevator.dto.ElevatorStateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/elevators")
@RequiredArgsConstructor
class ElevatorController {

	private final ElevatorFacade elevatorFacade;

	@GetMapping("/building")
	ResponseEntity<BuildingConfigDto> getBuildingConfig() {
		return ResponseEntity.ok(elevatorFacade.getBuildingConfig());
	}

	@GetMapping
	ResponseEntity<List<ElevatorStateDto>> getAllElevators() {
		return ResponseEntity.ok(elevatorFacade.getAllElevatorStates());
	}
}