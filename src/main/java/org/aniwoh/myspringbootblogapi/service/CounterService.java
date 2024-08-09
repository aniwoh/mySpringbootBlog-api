package org.aniwoh.myspringbootblogapi.service;

import reactor.core.publisher.Mono;
public interface CounterService {
    Mono<Integer>  getNextSequence(String name);
}
