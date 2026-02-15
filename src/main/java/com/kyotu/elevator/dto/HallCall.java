package com.kyotu.elevator.dto;

import com.kyotu.elevator.enums.Direction;

public record HallCall(int floor, Direction direction) {
}
