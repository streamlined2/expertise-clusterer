package luxoft.ch.expertisespace.clustering;

import java.util.HashSet;
import java.util.Set;

import luxoft.ch.expertisespace.model.ExpertiseSpace;

public class Distributor {

	private final ExpertiseSpace expertiseSpace;

	public Distributor(ExpertiseSpace expertiseSpace) {
		this.expertiseSpace = expertiseSpace;
	}

	public void distribute(int clusterCount) {
		initialize(clusterCount);
		//TODO
		
	}

	private void initialize(int clusterCount) {
		Set<Cluster> clusters = new HashSet<>();
		var roleIterator = expertiseSpace.getRoles().iterator();
		for (int k = 0; k < clusterCount; k++) {
			Cluster cluster = new Cluster(k);
			final int rolesPerCluster = expertiseSpace.getRoles().size() / clusterCount;
			for (int i = 0; roleIterator.hasNext() && i < rolesPerCluster; i++) {
				cluster.addRole(roleIterator.next());
			}
			clusters.add(cluster);
		}
	}

}
