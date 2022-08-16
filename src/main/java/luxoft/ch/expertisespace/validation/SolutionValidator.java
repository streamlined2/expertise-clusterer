package luxoft.ch.expertisespace.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import luxoft.ch.expertisespace.clustering.Cluster;
import luxoft.ch.expertisespace.model.Role;

public class SolutionValidator {

	public void validate(Set<Cluster> solution) {
		for (Cluster cluster1 : solution) {
			if (cluster1.size() > 1) {
				double distanceBefore1 = calculateIntraClusterDistance(cluster1);
				List<Role> roles = new ArrayList<>(cluster1.getRoles());
				for (Role role : roles) {
					cluster1.removeRole(role);
					double distanceAfter1 = calculateIntraClusterDistance(cluster1);
					for (Cluster cluster2 : solution) {
						double distanceBefore = distanceBefore1 + calculateIntraClusterDistance(cluster2);
						cluster2.addRole(role);
						double distanceAfter = distanceAfter1 + calculateIntraClusterDistance(cluster2);
						cluster2.removeRole(role);
						if (distanceBefore > distanceAfter) {
							System.out.println("### Validation error: moving role " + role
									+ " decreases intracluster distance from " + distanceBefore + " to "
									+ distanceAfter);
							System.out.println("From " + cluster1);
							System.out.println("To " + cluster2);
						}
					}
					cluster1.addRole(role);
				}
			}
		}
	}

	public double calculateIntraClusterDistance(Cluster cluster) {
		double distance = 0;
		for (Role role : cluster) {
			distance += cluster.getCentroidToPointDistanceDividend(role.getPoint());
		}
		return distance / (cluster.size() * cluster.size() * cluster.size());
	}

}
