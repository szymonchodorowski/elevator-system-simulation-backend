package com.kyotu.elevator;

import com.kyotu.elevator.enums.Direction;
import com.kyotu.elevator.enums.DoorStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorTest {

	private Elevator elevator;

	@BeforeEach
	void setUp() {
		elevator = new Elevator(0);
	}

	@Test
	void shouldStartAtGroundFloorIdle() {
		assertEquals(0, elevator.getCurrentFloor());
		assertEquals(Direction.IDLE, elevator.getDirection());
		assertEquals(DoorStatus.CLOSED, elevator.getDoorStatus());
		assertTrue(elevator.getTargetFloors().isEmpty());
	}

	@Test
	void shouldAddTargetFloor() {
		elevator.addTargetFloor(5);

		assertTrue(elevator.getTargetFloors().contains(5));
		assertTrue(elevator.hasTargetFloors());
	}

	@Test
	void shouldDeduplicateTargetFloors() {
		elevator.addTargetFloor(5);
		elevator.addTargetFloor(5);

		assertEquals(1, elevator.getTargetFloors().size());
	}

	@Test
	void shouldRemoveTargetFloor() {
		elevator.addTargetFloor(5);
		elevator.removeTargetFloor(5);

		assertFalse(elevator.hasTargetFloors());
	}

	@Test
	void shouldMoveUp() {
		elevator.moveUp();

		assertEquals(1, elevator.getCurrentFloor());
		assertEquals(Direction.UP, elevator.getDirection());
	}

	@Test
	void shouldMoveDown() {
		elevator.addTargetFloor(3);
		elevator.moveUp();
		elevator.moveUp();
		elevator.moveUp();
		elevator.moveDown();

		assertEquals(2, elevator.getCurrentFloor());
		assertEquals(Direction.DOWN, elevator.getDirection());
	}

	@Test
	void shouldStopAndBecomeIdle() {
		elevator.moveUp();
		elevator.stop();

		assertEquals(Direction.IDLE, elevator.getDirection());
	}

	@Test
	void shouldOpenAndCloseDoor() {
		elevator.openDoor(3);

		assertEquals(DoorStatus.OPEN, elevator.getDoorStatus());
		assertEquals(3, elevator.getDoorOpenTicksRemaining());

		elevator.closeDoor();

		assertEquals(DoorStatus.CLOSED, elevator.getDoorStatus());
		assertEquals(0, elevator.getDoorOpenTicksRemaining());
	}

	@Test
	void shouldDecrementDoorTicks() {
		elevator.openDoor(3);
		elevator.decrementDoorTicks();

		assertEquals(2, elevator.getDoorOpenTicksRemaining());
	}

	@Test
	void shouldDetectTargetsAbove() {
		elevator.addTargetFloor(5);

		assertTrue(elevator.hasTargetsAbove());
		assertFalse(elevator.hasTargetsBelow());
	}

	@Test
	void shouldDetectTargetsBelow() {
		elevator.moveUp();
		elevator.moveUp();
		elevator.moveUp();
		elevator.addTargetFloor(1);

		assertTrue(elevator.hasTargetsBelow());
		assertFalse(elevator.hasTargetsAbove());
	}

	@Test
	void shouldDetectCurrentFloorAsTarget() {
		elevator.addTargetFloor(0);

		assertTrue(elevator.isCurrentFloorTarget());
	}

	@Test
	void shouldNotDetectCurrentFloorWhenNoTarget() {
		elevator.addTargetFloor(5);

		assertFalse(elevator.isCurrentFloorTarget());
	}

	@Test
	void shouldKeepTargetFloorsSorted() {
		elevator.addTargetFloor(7);
		elevator.addTargetFloor(3);
		elevator.addTargetFloor(5);

		assertEquals(3, elevator.getTargetFloors().first());
		assertEquals(7, elevator.getTargetFloors().last());
	}
}