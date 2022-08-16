package luxoft.ch.expertisespace.clustering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

import luxoft.ch.expertisespace.model.Point;
import luxoft.ch.expertisespace.model.Role;

public class Cluster implements Iterable<Role> {

	private final int id;
	private final Set<Role> roles;
	private final List<Integer> dimensionTotals;

	public Cluster(int id) {
		this.id = id;
		roles = new HashSet<>();
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
		final int maxDimension = Math.max(dimensionTotals.size(), point.getLength());
		ensureDimensionTotalsSize(maxDimension);
		for (int dimension = 0; dimension < maxDimension; dimension++) {
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

	@Override
	public Iterator<Role> iterator() {
		return new Iterator<Role>() {

			private final Iterator<Role> iterator = roles.iterator();
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
			public void remove() {
				if (lastRole == null)
					throw new IllegalStateException("'next' should be called first before removal");
				lastRole.getPoint().forEach(Cluster.this::decreaseDimensionTotal);
				iterator.remove();
			}

		};
	}

}
