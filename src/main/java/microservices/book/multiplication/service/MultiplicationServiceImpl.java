package microservices.book.multiplication.service;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class MultiplicationServiceImpl implements MultiplicationService {

  private RandomGeneratorService randomGeneratorService;

  public MultiplicationServiceImpl(RandomGeneratorService randomGeneratorService) {
    this.randomGeneratorService = randomGeneratorService;
  }

  @Override
  public Multiplication createRandomMultiplication() {
    int factorA = randomGeneratorService.generateRandomFactor();
    int factorB = randomGeneratorService.generateRandomFactor();
    return new Multiplication(factorA, factorB);
  }

  @Override
  public boolean checkAttempt(final MultiplicationResultAttempt attempt) {
    // Checks if it's correct
    final boolean correct = attempt.getResultAttempt()
        == attempt.getMultiplication().getFactorA()
        * attempt.getMultiplication().getFactorB();

    // Avoids 'hack' attempts
    Assert.isTrue(!attempt.isCorrect(), "You can't send an attempt marked as correct!!");

    // Creates a copy, now setting the 'correct' field accordingly

    MultiplicationResultAttempt checkedAttempt = new MultiplicationResultAttempt(attempt.getUser(),
        attempt.getMultiplication(), attempt.getResultAttempt(), correct);

    // Returns the result

    return correct;
  }
}
