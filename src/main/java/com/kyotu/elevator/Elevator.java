package com.kyotu.elevator;

import com.kyotu.elevator.enums.Direction;
import com.kyotu.elevator.enums.DoorStatus;
import lombok.Getter;

import java.util.concurrent.ConcurrentSkipListSet;

@Getter
class Elevator {

	private final int id;
	private int currentFloor;
	private Direction direction;
	private DoorStatus doorStatus;
	private final ConcurrentSkipListSet<Integer> targetFloors;
	private int doorOpenTicksRemaining;

	Elevator(int id) {
		this.id = id;
		this.currentFloor = 0;
		this.direction = Direction.IDLE;
		this.doorStatus = DoorStatus.CLOSED;
		this.targetFloors = new ConcurrentSkipListSet<>();
		this.doorOpenTicksRemaining = 0;
	}

	void addTargetFloor(int floor) {
		targetFloors.add(floor);
	}

	void removeTargetFloor(int floor) {
		targetFloors.remove(floor);
	}

	boolean hasTargetFloors() {
		return !targetFloors.isEmpty();
	}

	boolean hasTargetsAbove() {
		return targetFloors.higher(currentFloor) != null;
	}

	boolean hasTargetsBelow() {
		return targetFloors.lower(currentFloor) != null;
	}

	boolean isCurrentFloorTarget() {
		return targetFloors.contains(currentFloor);
	}

	void moveUp() {
		currentFloor++;
		direction = Direction.UP;
	}

	void moveDown() {
		currentFloor--;
		direction = Direction.DOWN;
	}

	void setDirection(Direction direction) {
		this.direction = direction;
	}

	void stop() {
		direction = Direction.IDLE;
	}

	void openDoor(int ticks) {
		doorStatus = DoorStatus.OPEN;
		doorOpenTicksRemaining = ticks;
	}

	void closeDoor() {
		doorStatus = DoorStatus.CLOSED;
		doorOpenTicksRemaining = 0;
	}

	void decrementDoorTicks() {
		doorOpenTicksRemaining--;
	}
}