package luxoft.ch.expertisespace.clustering;

import java.util.HashSet;
import java.util.Set;

import luxoft.ch.expertisespace.model.Role;

public class Cluster {

	private final int id;
	private Role centroid;
	private final Set<Role> points; 

	public Cluster(int id) {
		this.id = id;
		points = new HashSet<>();
	}

}
