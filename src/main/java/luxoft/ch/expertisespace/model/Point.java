package luxoft.ch.expertisespace.model;

import java.util.BitSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

public class Point implements Iterable<Integer> {

	private final BitSet bitSet;

	public Point() {
		bitSet = new BitSet();
	}

	public Point(int dimension) {
		if (dimension <= 0)
			throw new IllegalArgumentException("dimension should be greater than 0");
		bitSet = new BitSet(dimension);
	}

	public int getLength() {
		return bitSet.length();
	}

	public boolean get(int index) {
		if (index >= bitSet.size()) {
			return false;
		}
		return bitSet.get(index);
	}

	public void set(int index) {
		bitSet.set(index);
	}

	@Override
	public String toString() {
		StringJoiner join = new StringJoiner(",", "[", "]");
		for (var dimension : this) {
			join.add(Integer.toString(dimension));
		}
		return join.toString();
	}

	@Override
	public Iterator<Integer> iterator() {
		return new Iterator<Integer>() {

			private int index = bitSet.nextSetBit(0);

			@Override
			public boolean hasNext() {
				return index >= 0;
			}

			@Override
			public Integer next() {
				if (!hasNext())
					throw new NoSuchElementException("no more bits set");
				int currentIndex = index;
				index = bitSet.nextSetBit(index + 1);
				return currentIndex;
			}

		};
	}

}
