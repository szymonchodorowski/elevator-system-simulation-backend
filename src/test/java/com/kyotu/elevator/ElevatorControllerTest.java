package com.kyotu.elevator;

import com.kyotu.elevator.dto.ElevatorStateDto;
import com.kyotu.elevator.dto.BuildingConfigDto;
import com.kyotu.elevator.enums.Direction;
import com.kyotu.elevator.enums.DoorStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ElevatorController.class)
class ElevatorControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ElevatorFacade elevatorFacade;

	@Test
	void shouldReturnBuildingConfig() throws Exception {
		when(elevatorFacade.getBuildingConfig())
				.thenReturn(new BuildingConfigDto(10, 3));

		mockMvc.perform(get("/api/v1/building"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.numberOfFloors").value(10))
				.andExpect(jsonPath("$.numberOfElevators").value(3));
	}

	@Test
	void shouldReturnAllElevators() throws Exception {
		var state = new ElevatorStateDto(0, 0, Direction.IDLE, DoorStatus.CLOSED, Set.of());
		when(elevatorFacade.getAllElevatorStates())
				.thenReturn(List.of(state));

		mockMvc.perform(get("/api/v1/elevators"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(0))
				.andExpect(jsonPath("$[0].direction").value("IDLE"));
	}

	@Test
	void shouldAcceptValidHallCall() throws Exception {
		doNothing().when(elevatorFacade).callElevator(any());

		mockMvc.perform(post("/api/v1/elevators/call")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"floor\": 5, \"direction\": \"UP\"}"))
				.andExpect(status().isOk());
	}

	@Test
	void shouldAcceptValidElevatorCall() throws Exception {
		doNothing().when(elevatorFacade).selectFloor(anyInt(), any());

		mockMvc.perform(post("/api/v1/elevators/0/floor")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"targetFloor\": 7}"))
				.andExpect(status().isOk());
	}

	@Test
	void shouldReturnSseStream() throws Exception {
		when(elevatorFacade.subscribe())
				.thenReturn(new SseEmitter());

		mockMvc.perform(get("/api/v1/elevators/stream"))
				.andExpect(status().isOk());
	}
}