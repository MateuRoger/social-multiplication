package microservices.book.multiplication.service.impl;

import java.util.Random;
import microservices.book.multiplication.service.RandomGeneratorService;
import org.springframework.stereotype.Service;

@Service
public class RandomGeneratorServiceImpl implements RandomGeneratorService {

  private static final int MAXIMUM_FACTOR = 99;
  private static final int MINIMUM_FACTOR = 11;

  @Override
  public int generateRandomFactor() {
    return new Random().nextInt((MAXIMUM_FACTOR - MINIMUM_FACTOR) + 1) + MINIMUM_FACTOR;
  }
}
