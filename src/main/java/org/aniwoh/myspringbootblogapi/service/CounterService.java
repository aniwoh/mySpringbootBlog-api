package org.aniwoh.myspringbootblogapi.service;

import reactor.core.publisher.Mono;
public interface CounterService {

    Integer getNextSequence(String name);
}
