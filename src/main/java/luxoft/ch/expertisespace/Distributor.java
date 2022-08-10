package luxoft.ch.expertisespace;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import luxoft.ch.expertisespace.clustering.Cluster;
import luxoft.ch.expertisespace.model.ExpertiseSpace;
import luxoft.ch.expertisespace.model.Point;
import luxoft.ch.expertisespace.model.Role;
import luxoft.ch.expertisespace.parsing.Parser;

public class Distributor {

	private final ExpertiseSpace expertiseSpace;

	public Distributor(ExpertiseSpace expertiseSpace) {
		this.expertiseSpace = expertiseSpace;
	}

	public Set<Cluster> distribute(int clusterCount) {
		var clusters = initialize(clusterCount);
		int pointsMoved = 0;
		do {
			var centroids = getCentroids(clusters);
			pointsMoved = reassignPoints(clusters, centroids);
		} while (pointsMoved > 0);
		return clusters;
	}

	private Set<Cluster> initialize(int clusterCount) {
		Set<Cluster> clusters = new HashSet<>();
		var roleIterator = expertiseSpace.getRoles().iterator();
		for (int k = 0; k < clusterCount; k++) {
			Cluster cluster = new Cluster(k);
			final int rolesPerCluster = (int) Math.ceil((double) expertiseSpace.getRoles().size() / clusterCount);
			for (int i = 0; roleIterator.hasNext() && i < rolesPerCluster; i++) {
				cluster.addRole(roleIterator.next());
			}
			clusters.add(cluster);
		}
		return clusters;
	}

	private Map<Point, Cluster> getCentroids(Set<Cluster> clusters) {
		Map<Point, Cluster> centroids = new HashMap<>();
		for (var cluster : clusters) {
			centroids.put(cluster.getCentroid(), cluster);
		}
		return centroids;
	}

	private int reassignPoints(Set<Cluster> clusters, Map<Point, Cluster> centroids) {
		int pointsMoved = 0;
		for (var cluster : clusters) {
			for (var roleIter = cluster.iterator(); roleIter.hasNext();) {
				Role role = roleIter.next();
				Point closestCentroid = findClosestCentroid(centroids, role.getPoint());
				Cluster centroidCluster = centroids.get(closestCentroid);
				if (cluster != centroidCluster) {
					roleIter.remove();
					centroidCluster.addRole(role);
					pointsMoved++;
				}
			}
		}
		return pointsMoved;
	}

	private Point findClosestCentroid(Map<Point, Cluster> centroids, Point point) {
		Point centroid = null;
		int minDistance = Integer.MAX_VALUE;
		for (var c : centroids.keySet()) {
			final int distance = point.getDistance(c);
			if (distance < minDistance) {
				minDistance = distance;
				centroid = c;
			}
		}
		return centroid;
	}

	public static void main(String... args) {
		Parser parser = new Parser("Java Flavors.csv");
		ExpertiseSpace space = parser.parse();
		System.out.println("number of expertise tokens %d:%n%s".formatted(space.getExpertiseTokenCount(), space.getExpertiseTokens()));
		System.out.println("--------------------------------------------------------------------");
		Distributor distributor = new Distributor(space);
		Set<Cluster> clusters = distributor.distribute(10);
		clusters.forEach(System.out::println);
	}

}
