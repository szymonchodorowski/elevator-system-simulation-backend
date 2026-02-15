package com.kyotu.elevator;

import com.kyotu.elevator.dto.BuildingConfigDto;
import com.kyotu.elevator.dto.ElevatorStateDto;
import com.kyotu.elevator.request.ElevatorCallRequest;
import com.kyotu.elevator.request.HallCallRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

	@PostMapping(ELEVATORS_ENDPOINT + "/call")
	ResponseEntity<Integer> callElevator(@Valid @RequestBody HallCallRequest request) {
		var elevatorId = elevatorFacade.callElevator(request);
		return ResponseEntity.ok().body(elevatorId);
	}

	@PostMapping(ELEVATORS_ENDPOINT + "/{id}/floor")
	ResponseEntity<Void> selectFloor(@PathVariable int id,
	                                 @Valid @RequestBody ElevatorCallRequest request) {
		elevatorFacade.selectFloor(id, request);
		return ResponseEntity.ok().build();
	}
}