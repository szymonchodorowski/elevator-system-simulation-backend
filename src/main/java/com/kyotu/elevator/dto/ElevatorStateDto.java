package com.kyotu.elevator.dto;

import com.kyotu.elevator.Direction;
import com.kyotu.elevator.DoorStatus;

import java.util.Set;

public record ElevatorStateDto(int id, int currentFloor, Direction direction, DoorStatus doorStatus, Set<Integer> targetFloors)
{}
