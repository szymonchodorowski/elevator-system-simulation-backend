package com.kyotu.elevator.dto;

import com.kyotu.elevator.enums.Direction;

record HallCall(int floor, Direction direction) {
}
