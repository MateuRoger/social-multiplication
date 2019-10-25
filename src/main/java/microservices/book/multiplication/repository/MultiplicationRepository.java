package microservices.book.multiplication.repository;

import java.util.Optional;
import microservices.book.multiplication.domain.Multiplication;
import org.springframework.data.repository.CrudRepository;

/**
 * This is the interface allows us to save and retrieve {@link Multiplication}
 */
public interface MultiplicationRepository extends CrudRepository<Multiplication, Long> {

  /**
   * Find a {@link Multiplication} by its Factors
   *
   * @param factorA the FactorA of the {@link Multiplication}
   * @param factorB the FactorB of the {@link Multiplication}
   * @return an {@link Optional<Multiplication>}
   */
  Optional<Multiplication> findByFactorAAndFactorB(final int factorA, final int factorB);
}
