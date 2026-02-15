package com.kyotu.elevator;

import com.kyotu.elevator.dto.HallCall;
import com.kyotu.elevator.enums.Direction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
class ElevatorSelector {

	private final Building building;

	Elevator select(List<Elevator> elevators, HallCall hallCall) {
		if (elevators.size() == 1) {
			return elevators.get(0);
		}

		Elevator best = null;
		var bestScore = Integer.MAX_VALUE;

		for (Elevator elevator : elevators) {
			int score = calculateScore(elevator, hallCall);
			if (score < bestScore) {
				bestScore = score;
				best = elevator;
			}
		}

		return best;
	}

	private int calculateScore(Elevator elevator, HallCall hallCall) {
		var distance = Math.abs(elevator.getCurrentFloor() - hallCall.floor());

		if (elevator.getDirection() == Direction.IDLE) {
			return distance;
		}

		if (isMovingToward(elevator, hallCall)) {
			return distance;
		}

		return distance + building.getNumberOfFloors();
	}

	private boolean isMovingToward(Elevator elevator, HallCall hallCall) {
		var currentFloor = elevator.getCurrentFloor();
		var targetFloor = hallCall.floor();

		if (elevator.getDirection() == Direction.UP) {
			return targetFloor >= currentFloor && hallCall.direction() == Direction.UP;
		}

		if (elevator.getDirection() == Direction.DOWN) {
			return targetFloor <= currentFloor && hallCall.direction() == Direction.DOWN;
		}

		return false;
	}
}
