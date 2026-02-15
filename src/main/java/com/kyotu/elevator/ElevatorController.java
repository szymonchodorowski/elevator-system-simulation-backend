package com.kyotu.elevator;

import com.kyotu.elevator.dto.BuildingConfigDto;
import com.kyotu.elevator.dto.ElevatorStateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
class ElevatorController {

	private static final String ELEVATORS_ENDPOINT = "/elevators";

	private final ElevatorFacade elevatorFacade;

	@GetMapping("/building")
	ResponseEntity<BuildingConfigDto> getBuildingConfig() {
		return ResponseEntity.ok(elevatorFacade.getBuildingConfig());
	}

	@GetMapping(ELEVATORS_ENDPOINT)
	ResponseEntity<List<ElevatorStateDto>> getAllElevators() {
		return ResponseEntity.ok(elevatorFacade.getAllElevatorStates());
	}

	@GetMapping(value = ELEVATORS_ENDPOINT + "/stream", produces = "text/event-stream")
	SseEmitter streamElevators() {
		return elevatorFacade.subscribe();
	}
}