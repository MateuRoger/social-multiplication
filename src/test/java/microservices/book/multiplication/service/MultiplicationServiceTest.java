package microservices.book.multiplication.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import microservices.book.multiplication.domain.Multiplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class MultiplicationServiceTest {

  @MockBean
  private RandomGeneratorService randomGenerator;

  @Autowired
  private MultiplicationService multiplicationService;

  @Test
  void createRandomMultiplicationTest() {
//    // given (our mocked Random Generator service will return first 50, then 30)
//
//    given(randomGenerator.generateRandomFactor()).willReturn(50, 30);
//
//    //when
//    Multiplication multiplication = multiplicationService.createRandomMultiplication();
//
//    //then
//    assertThat(multiplication.getFactorA()).isEqualTo(50);
//    assertThat(multiplication.getFactorB()).isEqualTo(30);
//    assertThat(multiplication.getResult()).isEqualTo(1500);
  }

}