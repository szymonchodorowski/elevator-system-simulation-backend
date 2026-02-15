package com.kyotu.elevator.request;

import com.kyotu.elevator.enums.Direction;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record HallCallRequest(@Min(0) int floor, @NotNull Direction direction)
{}
