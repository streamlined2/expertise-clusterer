package luxoft.ch.expertisespace.clustering;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.BinaryOperator;

import luxoft.ch.expertisespace.model.Point;
import luxoft.ch.expertisespace.model.Role;

public class Cluster implements Iterable<Role> {

	private static final BinaryOperator<Integer> ACCUMULATOR = (oldValue, value) -> oldValue + value;

	private final int id;
	private final Set<Role> roles;

	public Cluster(int id) {
		this.id = id;
		roles = new HashSet<>();
	}

	public Cluster(int id, String... roleName) {
		this.id = id;
		roles = new HashSet<>();
		Arrays.asList(roleName).stream().map(Role::new).forEach(this::addRole);
	}

	public void addRole(Role role) {
		roles.add(role);
	}

	public void removeRole(Role role) {
		roles.remove(role);
	}

	public Set<Role> getRoles() {
		return new HashSet<>(roles);
	}

	public Point getCentroid() {
		Map<Integer, Integer> dimensionTotals = getDimensionTotals();
		Point centroid = new Point();
		for (var entry : dimensionTotals.entrySet()) {
			if (2 * entry.getValue() >= roles.size()) {
				centroid.set(entry.getKey());
			}
		}
		return centroid;
	}

	private Map<Integer, Integer> getDimensionTotals() {
		Map<Integer, Integer> dimensionTotals = new HashMap<>();
		for (var role : roles) {
			for (var dimension : role.getPoint()) {
				dimensionTotals.merge(dimension, 1, ACCUMULATOR);
			}
		}
		return dimensionTotals;
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

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public Role next() {
				return iterator.next();
			}

			@Override
			public void remove() {
				iterator.remove();
			}

		};
	}

}
