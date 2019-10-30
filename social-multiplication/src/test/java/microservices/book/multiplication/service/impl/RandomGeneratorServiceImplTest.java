package microservices.book.multiplication.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import microservices.book.multiplication.service.RandomGeneratorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class RandomGeneratorServiceImplTest {

  private static RandomGeneratorService randomGeneratorService;

  @BeforeAll
  static void setUp() {
    randomGeneratorService = new RandomGeneratorServiceImpl();
  }

  @Test
  @Tag("Unit")
  @DisplayName("when we wants to generate a list of random factors, then all of them should be between 11 and 100")
  void whenGeneratesRandomFactors_thenAllOfThemShouldBeBetween11And100() {
    // when a good sample of randomly generated factors is  generated
    List<Integer> randomFactors = IntStream.range(0, 1000)
        .map(factor -> randomGeneratorService.generateRandomFactor())
        .boxed()
        .collect(Collectors.toList());
    // then all of them should be between 11 and 100
    // because we want a middle-complexity calculation

    assertThat(randomFactors).containsOnlyElementsOf(
        IntStream.range(11, 100).boxed().collect(Collectors.toList()));
  }
}