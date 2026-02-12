package com.kyotu.elevator.request;

import jakarta.validation.constraints.Min;

public record ElevatorCallRequest(@Min(0) int targetFloor)
{}
