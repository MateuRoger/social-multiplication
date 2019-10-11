package microservices.book.multiplication.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class RandomGeneratorServiceTest {

  @Autowired
  private RandomGeneratorService randomGeneratorService;

  @Test
  void generateRandomFactorIsBetweenExpectedLimits() {
    // when a good sample of randomly generated factors is  generated
    List<Integer> randomFactors = IntStream.range(0, 1000)
        .map(factor -> randomGeneratorService.generateRandomFactor())
        .boxed()
        .collect(Collectors.toList());
    // then all of them should be between 11 and 100
    // because we want a middle-complexity calculation
  }

}