package luxoft.ch.expertisespace.clustering;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import luxoft.ch.expertisespace.model.Role;

public class Cluster {

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

}
