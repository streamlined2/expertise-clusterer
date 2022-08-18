package luxoft.ch.expertisespace.clustering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

import luxoft.ch.expertisespace.model.Point;
import luxoft.ch.expertisespace.model.Role;

public class Cluster implements Iterable<Role> {

	private final int id;
	private final List<Role> roles;
	private final List<Integer> dimensionTotals;

	public Cluster(int id) {
		this.id = id;
		roles = new ArrayList<>();
		dimensionTotals = new ArrayList<>();
	}

	public Cluster(int id, String... roleName) {
		this(id);
		Arrays.asList(roleName).stream().map(Role::new).forEach(this::addRole);
	}

	public Set<Role> getRoles() {
		return Set.copyOf(roles);
	}

	public void addRole(Role role) {
		roles.add(role);
		role.getPoint().forEach(this::increaseDimensionTotal);
	}

	public void removeRole(Role role) {
		roles.remove(role);
		role.getPoint().forEach(this::decreaseDimensionTotal);
	}

	private void increaseDimensionTotal(int dimension) {
		ensureDimensionTotalsSize(dimension);
		dimensionTotals.set(dimension, dimensionTotals.get(dimension) + 1);
	}

	private void ensureDimensionTotalsSize(int dimension) {
		while (dimension >= dimensionTotals.size()) {
			dimensionTotals.add(0);
		}
	}

	private void decreaseDimensionTotal(int dimension) {
		ensureDimensionTotalsSize(dimension);
		dimensionTotals.set(dimension, dimensionTotals.get(dimension) - 1);
	}

	public int getCentroidToPointDistanceDividend(Point point) {
		int squareTotal = 0;
		final int size = Math.max(dimensionTotals.size(), point.getLength());
		ensureDimensionTotalsSize(size - 1);
		for (int dimension = 0; dimension < size; dimension++) {
			final int part = point.get(dimension) ? (roles.size() - dimensionTotals.get(dimension))
					: dimensionTotals.get(dimension);
			squareTotal += part * part;
		}
		return squareTotal;
	}

	public int size() {
		return roles.size();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Cluster cluster) {
			return id == cluster.id;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("cluster ").append(id).append(": ");
		StringJoiner join = new StringJoiner(",", "[", "]");
		for (var role : roles) {
			join.add(role.toString());
		}
		builder.append(join);
		return builder.toString();
	}

	private class RoleIterator implements ListIterator<Role> {

		private final ListIterator<Role> iterator = roles.listIterator();
		private Role lastRole;

		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public Role next() {
			lastRole = iterator.next();
			return lastRole;
		}

		@Override
		public boolean hasPrevious() {
			return iterator.hasPrevious();
		}

		@Override
		public Role previous() {
			lastRole = iterator.previous();
			return lastRole;
		}

		@Override
		public int nextIndex() {
			return iterator.nextIndex();
		}

		@Override
		public int previousIndex() {
			return iterator.previousIndex();
		}

		private void checkState() {
			if (lastRole == null)
				throw new IllegalStateException("'next' or 'previous' should be called first before modification");
		}

		@Override
		public void remove() {
			checkState();
			lastRole.getPoint().forEach(Cluster.this::decreaseDimensionTotal);
			iterator.remove();
		}

		@Override
		public void set(Role e) {
			checkState();
			lastRole.getPoint().forEach(Cluster.this::decreaseDimensionTotal);
			e.getPoint().forEach(Cluster.this::increaseDimensionTotal);
			iterator.set(e);
		}

		@Override
		public void add(Role e) {
			e.getPoint().forEach(Cluster.this::increaseDimensionTotal);
			iterator.add(e);
		}

	}

	@Override
	public Iterator<Role> iterator() {
		return listIterator();
	}

	public ListIterator<Role> listIterator() {
		return new RoleIterator();
	}

}
