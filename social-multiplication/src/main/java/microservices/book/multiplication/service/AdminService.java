package microservices.book.multiplication.service;

/**
 * Provides methods to execute operations that only an administrator can perform
 */
public interface AdminService {

  /**
   * Deletes all the database contents
   */
  void deleteDataBaseContents();
}
