package com.kyotu.elevator;

import com.kyotu.elevator.dto.ElevatorStateDto;
import com.kyotu.elevator.enums.Direction;
import com.kyotu.elevator.enums.DoorStatus;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
class ElevatorScheduler {

	private final Building building;
	private final ElevatorConfig config;
	private final ElevatorService elevatorService;
	private ScheduledExecutorService executor;

	@PostConstruct
	void start() {
		executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(this::tickAll, 0, config.getTickMs(), TimeUnit.MILLISECONDS);
	}

	@PreDestroy
	void stop() {
		if (executor != null) {
			executor.shutdown();
		}
	}

	private void tickAll() {
		try {
			for (Elevator elevator : building.getElevators()) {
				tick(elevator);
			}
			broadcastState();
		} catch (Exception e) {
			log.error("Scheduler tick failed", e);
		}
	}

	private void tick(Elevator elevator) {
		if (elevator.getDoorStatus() == DoorStatus.OPEN) {
			elevator.decrementDoorTicks();
			if (elevator.getDoorOpenTicksRemaining() <= 0) {
				elevator.closeDoor();
			}
			return;
		}

		if (!elevator.hasTargetFloors()) {
			elevator.stop();
			return;
		}

		if (elevator.isCurrentFloorTarget()) {
			elevator.removeTargetFloor(elevator.getCurrentFloor());
			elevator.openDoor(config.getDoorOpenTicks());
			return;
		}

		if (elevator.getDirection() == Direction.IDLE) {
			determineInitialDirection(elevator);
		}

		if (!hasTargetsInCurrentDirection(elevator)) {
			reverseDirection(elevator);
		}

		move(elevator);
	}

	private void determineInitialDirection(Elevator elevator) {
		var above = elevator.getTargetFloors().higher(elevator.getCurrentFloor());
		var below = elevator.getTargetFloors().lower(elevator.getCurrentFloor());

		if (above != null && below != null) {
			var distUp = above - elevator.getCurrentFloor();
			var distDown = elevator.getCurrentFloor() - below;
			elevator.setDirection(distUp <= distDown ? Direction.UP : Direction.DOWN);
		} else if (above != null) {
			elevator.setDirection(Direction.UP);
		} else {
			elevator.setDirection(Direction.DOWN);
		}
	}

	private boolean hasTargetsInCurrentDirection(Elevator elevator) {
		if (elevator.getDirection() == Direction.UP) {
			return elevator.hasTargetsAbove();
		}
		return elevator.hasTargetsBelow();
	}

	private void reverseDirection(Elevator elevator) {
		if (elevator.getDirection() == Direction.UP) {
			elevator.setDirection(Direction.DOWN);
		} else {
			elevator.setDirection(Direction.UP);
		}
	}

	private void move(Elevator elevator) {
		if (elevator.getDirection() == Direction.UP) {
			elevator.moveUp();
		} else {
			elevator.moveDown();
		}
	}

	private void broadcastState() {
		List<ElevatorStateDto> states = building.getElevators().stream()
				.map(ElevatorMapper::toDto)
				.toList();
		elevatorService.broadcast(states);
	}
}