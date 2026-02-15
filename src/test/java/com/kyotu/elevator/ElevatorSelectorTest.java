package com.kyotu.elevator;

import com.kyotu.elevator.dto.HallCall;
import com.kyotu.elevator.enums.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorSelectorTest {

	private ElevatorSelector selector;
	private Elevator elevator0;
	private Elevator elevator1;
	private Elevator elevator2;
	private List<Elevator> elevators;

	@BeforeEach
	void setUp() {
		var building = new Building(10, 3);
		selector = new ElevatorSelector(building);
		elevator0 = building.getElevators().get(0);
		elevator1 = building.getElevators().get(1);
		elevator2 = building.getElevators().get(2);
		elevators = building.getElevators();
	}

	@Test
	void shouldReturnOnlyElevatorWhenSingleElevator() {
		var singleBuilding = new Building(10, 1);
		var singleSelector = new ElevatorSelector(singleBuilding);
		var single = singleBuilding.getElevators().get(0);

		var call = new HallCall(5,Direction.UP);
		var result = singleSelector.select(singleBuilding.getElevators(), call);

		assertSame(single, result);
	}

	@Test
	void shouldAssignNearestIdleElevator() {
		moveToFloor(elevator0, 2);
		elevator0.stop();
		moveToFloor(elevator1, 7);
		elevator1.stop();

		var call = new HallCall(3, Direction.UP);
		var result = selector.select(elevators, call);

		assertSame(elevator0, result);
	}

	@Test
	void shouldAssignElevatorMovingInSameDirection() {
		moveToFloor(elevator0, 2);
		elevator0.setDirection(Direction.UP);
		moveToFloor(elevator1, 1);
		elevator1.stop();

		var call = new HallCall(5, Direction.UP);
		var result = selector.select(elevators, call);

		assertSame(elevator0, result);
	}

	@Test
	void shouldPenalizeElevatorMovingInWrongDirection() {
		moveToFloor(elevator0, 5);
		elevator0.setDirection(Direction.DOWN);
		moveToFloor(elevator1, 2);
		elevator1.stop();

		var call = new HallCall(7, Direction.UP);
		var result = selector.select(elevators, call);

		assertSame(elevator1, result);
	}

	@Test
	void shouldHandleAllElevatorsOnSameFloor() {
		var call = new HallCall(5, Direction.UP);
		var result = selector.select(elevators, call);

		assertNotNull(result);
	}

	@Test
	void shouldPreferIdleOverMovingAway() {
		moveToFloor(elevator0, 3);
		elevator0.setDirection(Direction.DOWN);
		moveToFloor(elevator1, 4);
		elevator1.stop();

		var call = new HallCall(5, Direction.UP);
		var result = selector.select(elevators, call);

		assertSame(elevator1, result);
	}

	private void moveToFloor(Elevator elevator, int floor) {
		for (int i = 0; i < floor; i++) {
			elevator.moveUp();
		}
	}
}