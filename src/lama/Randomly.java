package lama;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class Randomly {

	private Randomly() {
	}

	public static <T> T fromList(List<T> list) {
		return list.get(ThreadLocalRandom.current().nextInt(list.size()));
	}

	@SafeVarargs
	public static <T> T fromOptions(T... options) {
		return options[ThreadLocalRandom.current().nextInt(options.length)];
	}

	public static <T> List<T> nonEmptySubset(List<T> columns) {
		int nr = 1 + ThreadLocalRandom.current().nextInt(columns.size());
		return extractNrRandomColumns(columns, nr);
	}

	public static <T> List<T> subset(List<T> columns) {
		int nr = ThreadLocalRandom.current().nextInt(columns.size() + 1);
		return extractNrRandomColumns(columns, nr);
	}

	public static <T> List<T> subset(@SuppressWarnings("unchecked") T... values) {
		List<T> list = new ArrayList<>();
		for (T val : values) {
			list.add(val);
		}
		return subset(list);
	}

	private static <T> List<T> extractNrRandomColumns(List<T> columns, int nr) {
		assert nr >= 0;
		List<T> selectedColumns = new ArrayList<>();
		List<T> remainingColumns = new ArrayList<>(columns);
		for (int i = 0; i < nr; i++) {
			selectedColumns.add(remainingColumns.remove(ThreadLocalRandom.current().nextInt(remainingColumns.size())));
		}
		return selectedColumns;
	}

	public static long greaterOrEqualInt(long intValue) {
		return greaterOrEqual(intValue);
	}

	private static long greaterOrEqual(long intValue) {
		if (intValue == Long.MAX_VALUE) {
			return Long.MAX_VALUE;
		}
		long result = ThreadLocalRandom.current().nextLong(intValue, Long.MAX_VALUE);
		assert result >= intValue && result <= Long.MAX_VALUE : intValue + " " + result;
		return result;
	}

	public static long greaterInt(long intValue) {
		if (intValue == Long.MAX_VALUE) {
			throw new IllegalArgumentException();
		}
		return greaterOrEqual(intValue + 1);
	}

	public static long smallerOrEqualInt(long intValue) {
		long smallerOrEqualInt = smallerOrEqual(intValue);
		assert smallerOrEqualInt <= intValue;
		return smallerOrEqualInt;
	}

	private static long smallerOrEqual(long intValue) {
		if (intValue == Long.MIN_VALUE) {
			return Long.MIN_VALUE;
		}
		long lessOrEqual = ThreadLocalRandom.current().nextLong(Long.MIN_VALUE, intValue);
		return lessOrEqual;
	}

	public static long smallerInt(long intValue) {
		if (intValue == Long.MIN_VALUE) {
			throw new IllegalArgumentException();
		}
		long smallerInt = smallerOrEqual(intValue - 1);
		assert smallerInt < intValue;
		return smallerInt;
	}

	public static double smallerDouble(double value) {
		if (value == Double.NEGATIVE_INFINITY) {
			throw new IllegalArgumentException();
		} else if (value == -Double.MAX_VALUE) {
			return Double.NEGATIVE_INFINITY;
		} else {
			return ThreadLocalRandom.current().nextDouble(-Double.MAX_VALUE, value);
		}
	}

	public static int smallNumber() {
		return ThreadLocalRandom.current().nextInt(5);
	}

	public static boolean getBoolean() {
		return ThreadLocalRandom.current().nextBoolean();
	}

	public static long notEqualInt(long intValue) {
		int randomInt;
		do {
			randomInt = ThreadLocalRandom.current().nextInt();
		} while (randomInt == intValue);
		assert intValue != randomInt;
		return randomInt;
	}

	public static long getInteger() {
		if (smallBiasProbability()) {
			return Randomly.fromOptions(-1l, Long.MAX_VALUE, Long.MIN_VALUE, 1l, 0l);
		}
		return ThreadLocalRandom.current().nextInt();
	}

	public static String getString() {
		if (getBoolean()) {
			return Randomly.fromOptions("test", "TRUE", "FALSE", "0.0", "-0.0", "1e500", "-1e500");
		}

		String alphabet = new String("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!#<>/.öä~-+' ");
		int n = alphabet.length();

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < Randomly.smallNumber(); i++) {
			sb.append(alphabet.charAt(ThreadLocalRandom.current().nextInt(n)));
		}

		return sb.toString();
	}

	public static void getBytes(byte[] bytes) {
		ThreadLocalRandom.current().nextBytes(bytes);
	}

	public static long getNonZeroInteger() {
		long value;
		if (smallBiasProbability()) {
			return Randomly.fromOptions(-1l, Long.MAX_VALUE, Long.MIN_VALUE, 1l);
		}
		do {
			value = Randomly.getInteger();
		} while (value == 0);
		assert value != 0;
		return value;
	}

	public static double getNonZeroReal() {
		double value;
		if (smallBiasProbability()) {
			return Randomly.fromOptions(1.0, -1.0);
		} else if (smallBiasProbability()) {
			do {
				value = Randomly.getInteger();
			} while (value == 0.0);
		}
		do {
			value = Randomly.getDouble();
		} while (value == 0.0);
		assert value != 0.0;
		return value;
	}

	public static long getPositiveInteger() {
		long value;
		if (smallBiasProbability()) {
			value = Randomly.fromOptions(0l, Long.MAX_VALUE, 1l);
		} else if (smallBiasProbability()) {
			value = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
		} else {
			value = ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
		}
		assert value >= 0;
		return value;
	}

	public static String greaterOrEqualString(String value) {
		return value; // TODO
	}

	public static double greaterOrEqualDouble(double asDouble) {
		if (asDouble == Double.POSITIVE_INFINITY) {
			return asDouble;
		} else if (asDouble == Double.MAX_VALUE) {
			return Randomly.fromOptions(Double.POSITIVE_INFINITY, Double.MAX_VALUE);
		} else {
			return ThreadLocalRandom.current().nextDouble(asDouble, Double.MAX_VALUE);
		}
	}

	public static double smallerOrEqualDouble(double value) {
		if (value == Double.NEGATIVE_INFINITY) {
			return value;
		} else if (value == -Double.MAX_VALUE) {
			return Randomly.fromOptions(-Double.MAX_VALUE, Double.NEGATIVE_INFINITY);
		} else {
			return ThreadLocalRandom.current().nextDouble(-Double.MAX_VALUE, value);
		}
	}

	public static double getDouble() {
		if (smallBiasProbability()) {
			return Randomly.fromOptions(3.3, 5.0, 0.0, -8.0, -0.0, Double.MAX_VALUE, -Double.MAX_VALUE, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
		} else {
			return ThreadLocalRandom.current().nextDouble();
		}
	}

	private static boolean smallBiasProbability() {
		return ThreadLocalRandom.current().nextInt(1000) == 1;
	}

	public static boolean getBooleanWithSmallProbability() {
		return smallBiasProbability();
	}

	public static int getInteger(int left, int right) {
		if (left == right) {
			return left;
		}
		return ThreadLocalRandom.current().nextInt(left, right);
	}

	public static String getNonZeroString() {
		if (Randomly.getBooleanWithSmallProbability()) {
			return Randomly.fromOptions("-5x");
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append(Randomly.getNonZeroInteger());
			sb.append(Randomly.getString());
			return sb.toString();
		}
	}

	public static double greaterDouble(double value) {
		if (value == Double.POSITIVE_INFINITY) {
			throw new IllegalArgumentException();
		} else if (value == Double.MAX_VALUE) {
			return Double.POSITIVE_INFINITY;
		} else {
			return ThreadLocalRandom.current().nextDouble(value + 1, Double.MAX_VALUE);
		}
	}

}
