package luxoft.ch.expertisespace.model;

import java.util.BitSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class Role implements Iterable<Integer>, Comparable<Role> {

	private final String name;
	private final BitSet bitSet;

	public Role(String role, int dimension) {
		if (role == null || role.isBlank())
			throw new IllegalArgumentException("role should not be null or empty");
		if (dimension <= 0)
			throw new IllegalArgumentException("dimension should be greater than 0");
		this.name = role;
		bitSet = new BitSet(dimension);
	}

	public String getRole() {
		return name;
	}

	public void set(int index) {
		bitSet.set(index);
	}

	public int getDistance(Role point) {
		BitSet clone = (BitSet) bitSet.clone();
		clone.xor(point.bitSet);
		return clone.cardinality();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Role p) {
			return Objects.equals(name, p.name);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
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

	@Override
	public int compareTo(Role p) {
		return name.compareToIgnoreCase(p.name);
	}

}
