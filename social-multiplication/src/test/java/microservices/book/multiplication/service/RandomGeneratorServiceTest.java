package microservices.book.multiplication.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RandomGeneratorServiceTest {

  @Autowired
  private RandomGeneratorService randomGeneratorService;

  @Test
  @Tag("Integration")
  @DisplayName("when generates random factors, then all of them should be between 11 and 100")
  void whenGeneratesRandomFactors_thenAllOfThemShouldBeBetween11And100() {
    // when a good sample of randomly generated factors is  generated
    final List<Integer> randomFactors = IntStream.range(0, 1000)
        .map(factor -> randomGeneratorService.generateRandomFactor())
        .boxed()
        .collect(Collectors.toList());
    // then all of them should be between 11 and 100
    // because we want a middle-complexity calculation

    assertThat(randomFactors).containsOnlyElementsOf(
        IntStream.range(11, 100).boxed().collect(Collectors.toList()));
  }

}