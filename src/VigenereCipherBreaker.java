import java.util.ArrayList;
import java.util.Arrays;

public class VigenereCipherBreaker implements CipherBreaker {
	private String text;
	private static final int MIN_KEY_LENGTH = 2;
	private static final int MAX_KEY_LENGTH = 22;
	private static final double ENGLISH_MID_IC = 0.053;

	@Override
	public void setCipherText(String text) {
		this.text = text;
	}

	private String format(String text) {
		return text.toUpperCase().replaceAll("[^\\p{L}]", "");
	}

	private int[] getLetterFrequency(String text) {
		text = format(text);
		int[] frequencies = new int[26];
		for (int i = 0; i < text.length(); i++) {
			frequencies[text.charAt(i) - 'A']++;
		}
		return frequencies;
	}

	private double calculateIC(String text) {
		int n = text.length();
		int[] f = getLetterFrequency(text);
		double sum = 0;
		// calculate the sum of each frequency
		for (int i = 0; i < f.length; i++) {
			sum += (f[i] * (f[i] - 1));
		}
		// divide by n(n-1)
		return sum / (n * (n - 1));
	}

	private String[][] divideCipher() {
		String[][] dividedCipher = new String[(MAX_KEY_LENGTH - MIN_KEY_LENGTH) + 1][];
		String[] parts;
		for (int i = MIN_KEY_LENGTH; i <= MAX_KEY_LENGTH; i++) {
			parts = new String[i];
			Arrays.fill(parts, "");
			for (int j = 0; j < this.text.length(); j++) {
				parts[j % i] += this.text.charAt(j);
			}
			dividedCipher[i - MIN_KEY_LENGTH] = parts;
		}
		return dividedCipher;
	}

	private double[] getAverageIC(String[][] dividedCipher) {
		double sumIC = 0;
		double[] avgIC = new double[MAX_KEY_LENGTH];
		for (int i = 0; i < dividedCipher.length; i++) {
			for (int j = 0; j < dividedCipher[i].length; j++) {
				sumIC += calculateIC(dividedCipher[i][j]);
			}
			avgIC[i] = sumIC / (double) dividedCipher[i].length;
			sumIC = 0;
		}
		return avgIC;
	}

	private boolean isPrimeNumber(int number) {
		if (number == 2 || number == 3) {
			return true;
		}
		if (number % 2 == 0) {
			return false;
		}
		int sqrt = (int) Math.sqrt(number) + 1;
		for (int i = 3; i < sqrt; i += 2) {
			if (number % i == 0) {
				return false;
			}
		}
		return true;
	}

	private int getSmallestPrimeLength(ArrayList<Integer> candidates) {
		int min = Integer.MAX_VALUE, minPrime = Integer.MAX_VALUE;
		for (int candidate : candidates) {
			if (candidate < min) {
				min = candidate;
				if (isPrimeNumber(candidate)) {
					minPrime = candidate;
				}
			}
		}
		return (minPrime != Integer.MAX_VALUE ? minPrime : min) + MIN_KEY_LENGTH;
	}

	@Override
	public int computeKeyLength() {
		String[][] dividedCipher = divideCipher();

		// Print the cipher divisions for each key length guess from 2 to 22
		// System.out.println("\nCipher divisions for each key length guess from 2 to
		// 22\n");
		// for (int i = 0; i < dividedCipher.length; i++) {
		// System.out.print(i + 2 + ", [ ");
		// for (int j = 0; j < dividedCipher[i].length; j++) {
		// System.out.print(dividedCipher[i][j] + (j == dividedCipher[i].length - 1 ? "
		// " : " -- "));
		// }
		// System.out.println("]\n\n");
		// }

		// System.out.println("\nThe average IC for each key length guess from 2 to
		// 22\n");

		// Choose the closest IC to the English IC
		double[] avgIC = getAverageIC(dividedCipher);
		ArrayList<Integer> candidates = new ArrayList<>();
		for (int i = 0; i < avgIC.length; i++) {
			// Print the cipher divisions from each key length guess 2 to 22
			// System.out.println(avgIC[i]);

			if (avgIC[i] >= ENGLISH_MID_IC) {
				candidates.add(i);
			}
		}
		System.out.print("[ ");
		for (int i = 0; i < candidates.size(); i++) {
			System.out.print((int) (candidates.get(i) + 2) + (i != candidates.size() - 1 ? ", " : " "));
		}
		System.out.print("]\n\n");
		return getSmallestPrimeLength(candidates);
	}
}
