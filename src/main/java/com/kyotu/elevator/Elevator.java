package com.kyotu.elevator;

import lombok.Getter;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

@Getter
class Elevator {

	private final int id;
	private int currentFloor;
	private Direction direction;
	private DoorStatus doorStatus;
	private final Set<Integer> targetFloors;

	Elevator(int id) {
		this.id = id;
		this.currentFloor = 0;
		this.direction = Direction.IDLE;
		this.doorStatus = DoorStatus.CLOSED;
		this.targetFloors = new ConcurrentSkipListSet<>();
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

	void moveUp() {
		currentFloor++;
		direction = Direction.UP;
	}

	void moveDown() {
		currentFloor--;
		direction = Direction.DOWN;
	}

	void stop() {
		direction = Direction.IDLE;
	}

	void openDoor() {
		doorStatus = DoorStatus.OPEN;
	}

	void closeDoor() {
		doorStatus = DoorStatus.CLOSED;
	}
}