package microservices.book.multiplication.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import microservices.book.multiplication.domain.Multiplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class MultiplicationServiceImplTest {

  private MultiplicationServiceImpl multiplicationService;

  @Mock
  private RandomGeneratorService randomGeneratorService;

  @BeforeEach
  void setUp() {
    // With this call to initMocks we tell Mockito to process the annotations
    MockitoAnnotations.initMocks(this);
    multiplicationService = new MultiplicationServiceImpl(randomGeneratorService);
  }

  @Test
  @Tag("Unit")
  @DisplayName("Given a generatorRamdomFactory, when creates a random multiplication, then will it returns 1500")
  void givenGeneratorRamdomFactory_whenCreatesRandomMultiplication_thenWillReturnsMultiplication() {

    // given (our mocked Random Generator service will return first 50, then 30)
    given(randomGeneratorService.generateRandomFactor()).willReturn(50, 30);

    //when
    Multiplication multiplication = multiplicationService.createRandomMultiplication();

    //then
    assertThat(multiplication.getFactorA()).isEqualTo(50);
    assertThat(multiplication.getFactorB()).isEqualTo(30);
    assertThat(multiplication.getResult()).isEqualTo(1500);
  }
}