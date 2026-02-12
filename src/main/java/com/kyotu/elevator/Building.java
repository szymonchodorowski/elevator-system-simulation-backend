package com.kyotu.elevator;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Component
class Building {

	private final int numberOfFloors;
	private final List<Elevator> elevators;

	Building(@Value("${building.floors}") int numberOfFloors,
	         @Value("${building.elevators}") int numberOfElevators) {
		this.numberOfFloors = numberOfFloors;
		List<Elevator> list = new ArrayList<>();
		for (int i = 0; i < numberOfElevators; i++) {
			list.add(new Elevator(i));
		}
		this.elevators = Collections.unmodifiableList(list);
	}

	Elevator getElevator(int id) {
		if (id < 0 || id >= elevators.size()) {
			throw new IllegalArgumentException("Invalid elevator id: " + id);
		}
		return elevators.get(id);
	}
}
