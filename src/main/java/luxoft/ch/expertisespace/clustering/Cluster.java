package luxoft.ch.expertisespace.clustering;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BinaryOperator;

import luxoft.ch.expertisespace.model.Point;
import luxoft.ch.expertisespace.model.Role;

public class Cluster {

	private static final BinaryOperator<Integer> ACCUMULATOR = (oldValue, value) -> oldValue + value;

	private final int id;
	private final Set<Role> roles;

	public Cluster(int id) {
		this.id = id;
		roles = new HashSet<>();
	}

	public void addRole(Role role) {
		roles.add(role);
	}

	public void removeRole(Role role) {
		roles.remove(role);
	}

	public Point getCentroid() {
		Map<Integer, Integer> accumulatingMap = new HashMap<>();
		for (var role : roles) {
			for (var dimension : role.getPoint()) {
				accumulatingMap.merge(dimension, 1, ACCUMULATOR);
			}
		}
		Point centroid = new Point();
		for (var entry : accumulatingMap.entrySet()) {
			if (2 * entry.getValue() >= roles.size()) {
				centroid.set(entry.getKey());
			}
		}
		return centroid;
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

	public static void main(String... args) {
		Cluster cluster = new Cluster(0);
		Role role1 = new Role("role1", 3);
		role1.set(0);
		role1.set(2);
		// role1.set(1);
		Role role2 = new Role("role2", 3);
		role2.set(1);
		role2.set(2);
		// role2.set(0);
		Role role3 = new Role("role3", 3);
		role3.set(1);
		role3.set(0);
		// role3.set(2);
		cluster.addRole(role1);
		cluster.addRole(role2);
		cluster.addRole(role3);
		Point centroid = cluster.getCentroid();
		System.out.println(centroid);
	}

}
