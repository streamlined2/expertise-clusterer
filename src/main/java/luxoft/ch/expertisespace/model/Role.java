package luxoft.ch.expertisespace.model;

import java.util.Objects;

public class Role implements Comparable<Role> {

	private final String name;
	private final Point point;

	public Role(String name, int dimension) {
		if (name == null || name.isBlank())
			throw new IllegalArgumentException("role name should not be null or empty");
		this.name = name;
		point = new Point(dimension);
	}

	public String getName() {
		return name;
	}

	public Point getPoint() {
		return point;
	}

	public void set(int index) {
		point.set(index);
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
	public int compareTo(Role p) {
		return name.compareToIgnoreCase(p.name);
	}

}
