/**
 * The class that contains the algorithm to break the Vigenere cipher should
 * implement this CipherBreaker interface.
 * 
 */
public interface CipherBreaker {

	/**
	 * Method to set the cipher text required to break
	 * 
	 * @param text
	 */
	void setCipherText(String text);

	/**
	 * Method to compute the best key length
	 * 
	 * @return the best key length
	 */
	int computeKeyLength();
}
