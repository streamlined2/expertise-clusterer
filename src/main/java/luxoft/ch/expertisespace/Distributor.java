package luxoft.ch.expertisespace;

import java.util.HashSet;
import java.util.Set;

import luxoft.ch.expertisespace.clustering.Cluster;
import luxoft.ch.expertisespace.model.ExpertiseSpace;
import luxoft.ch.expertisespace.model.Point;
import luxoft.ch.expertisespace.model.Role;
import luxoft.ch.expertisespace.parsing.Parser;
import luxoft.ch.expertisespace.validation.SolutionValidator;

public class Distributor {

	private final ExpertiseSpace expertiseSpace;

	public Distributor(ExpertiseSpace expertiseSpace) {
		this.expertiseSpace = expertiseSpace;
	}

	public Set<Cluster> distribute(int clusterCount) {
		var clusters = initialize(clusterCount);
		while (reassignPoints(clusters) > 0)
			;
		return clusters;
	}

	private Set<Cluster> initialize(int clusterCount) {
		final int rolesPerCluster = (int) Math.ceil((double) expertiseSpace.getRolesCount() / clusterCount);
		Set<Cluster> clusters = new HashSet<>();
		var roleIterator = expertiseSpace.iterator();
		for (int k = 0; k < clusterCount; k++) {
			Cluster cluster = new Cluster(k);
			for (int i = 0; roleIterator.hasNext() && i < rolesPerCluster; i++) {
				cluster.addRole(roleIterator.next());
			}
			clusters.add(cluster);
		}
		return clusters;
	}

	private int reassignPoints(Set<Cluster> clusters) {
		int pointsMoved = 0;
		for (var cluster : clusters) {
			for (var roleIter = cluster.iterator(); roleIter.hasNext();) {
				Role role = roleIter.next();
				Cluster closestCentroidCluster = findClosestCentroidCluster(clusters, role.getPoint());
				if (closestCentroidCluster != null && cluster != closestCentroidCluster) {
					roleIter.remove();
					closestCentroidCluster.addRole(role);
					pointsMoved++;
				}
			}
		}
		return pointsMoved;
	}

	private Cluster findClosestCentroidCluster(Set<Cluster> clusters, Point point) {
		var iter = clusters.iterator();
		if (iter.hasNext()) {
			Cluster minDistanceCluster = iter.next();
			int minDistance = minDistanceCluster.getCentroidToPointDistanceDividend(point);
			while (iter.hasNext()) {
				Cluster cluster = iter.next();
				final int distance = cluster.getCentroidToPointDistanceDividend(point);
				if (compareDistances(cluster, distance, minDistanceCluster, minDistance) < 0) {
					minDistance = distance;
					minDistanceCluster = cluster;
				}
			}
			return minDistanceCluster;
		}
		return null;
	}

	private int compareDistances(Cluster cluster1, int distance1, Cluster cluster2, int distance2) {
		final int firstOperand = distance1 * cluster2.size() * cluster2.size();
		final int secondOperand = distance2 * cluster1.size() * cluster1.size();
		if (firstOperand < secondOperand)
			return -1;
		if (firstOperand > secondOperand)
			return 1;
		return 0;
	}

	public static void main(String... args) {
		Parser parser = new Parser("Java Flavors.csv");
		ExpertiseSpace space = parser.parse();
		System.out.println("number of expertise tokens %d:%n%s".formatted(space.getExpertiseTokenCount(),
				space.getExpertiseTokens()));
		System.out.println("--------------------------------------------------------------------");
		Distributor distributor = new Distributor(space);
		Set<Cluster> clusters = distributor.distribute(10);
		clusters.forEach(System.out::println);
		System.out.println("--------------------------------------------------------------------");
		new SolutionValidator().validate(clusters);
	}

}
