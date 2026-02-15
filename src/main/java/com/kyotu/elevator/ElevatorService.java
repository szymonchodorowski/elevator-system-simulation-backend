package com.kyotu.elevator;

import com.kyotu.elevator.dto.ElevatorStateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
class ElevatorService {

	private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

	SseEmitter subscribe(List<ElevatorStateDto> initialState) {
		SseEmitter emitter = new SseEmitter(0L);
		emitters.add(emitter);
		emitter.onCompletion(() -> emitters.remove(emitter));
		emitter.onTimeout(() -> emitters.remove(emitter));
		emitter.onError(e -> emitters.remove(emitter));

		try {
			emitter.send(SseEmitter.event()
					.name("elevator-update")
					.data(initialState));
		} catch (IOException e) {
			emitter.complete();
			emitters.remove(emitter);
		}

		return emitter;
	}

	void broadcast(List<ElevatorStateDto> states) {
		for (SseEmitter emitter : emitters) {
			try {
				emitter.send(SseEmitter.event()
						.name("elevator-update")
						.data(states));
			} catch (IOException e) {
				emitter.complete();
				emitters.remove(emitter);
			}
		}
	}
}