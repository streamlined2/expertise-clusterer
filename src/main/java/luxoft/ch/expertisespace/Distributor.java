package luxoft.ch.expertisespace;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
		ExpertiseSpace space = new ExpertiseSpace();
		space.addRole("ROLE-1", List.of("Java"));
		space.addRole("ROLE-2", List.of("Java"));
		space.addRole("ROLE-3", List.of("Java"));
		space.addRole("ROLE-4", List.of("Spring Framework"));
		space.addRole("ROLE-5", List.of("Spring Framework"));
		Distributor distributor = new Distributor(space);
		Set<Cluster> clusters = distributor.distribute(2);
		clusters.forEach(System.out::println);

		space = new ExpertiseSpace();
		space.addRole("ROLE-1", List.of("Java", "Python"/* , "MySQL", "PostgreSQL", */));
		space.addRole("ROLE-2", List.of("Java"/* , "MongoDB", "REST", "Spring Boot", "SQL" */));
		space.addRole("ROLE-3", List.of("Java", "Python"/* , "Spring Boot", "PostgreSQL", */));
		space.addRole("ROLE-4", List.of(/* "Hibernate", "Oracle", "SQL", */"Java", "Spring Framework"));
		space.addRole("ROLE-5", List.of(/*
										 * "Concurrent Programming", "Hibernate", "Manual Testing", "RabbitMQ",
										 */
				"Java", "Spring Framework"));
		distributor = new Distributor(space);
		clusters = distributor.distribute(2);
		clusters.forEach(System.out::println);

		space = new ExpertiseSpace();
		space.addRole("ROLE-1", List.of("Java", "Python", "PostgreSQL"/* , "MySQL", , */));
		space.addRole("ROLE-2", List.of("Java", "SQL"/* , "MongoDB", "REST", "Spring Boot", */));
		space.addRole("ROLE-3", List.of("Java", "Python", "PostgreSQL"/* , "Spring Boot", , */));
		space.addRole("ROLE-4", List.of(/* "Hibernate", "Oracle", */"Java", "Spring Framework", "SQL"));
		space.addRole("ROLE-5", List.of(/*
										 * "Concurrent Programming", "Hibernate", "Manual Testing", "RabbitMQ",
										 */
				"Java", "Spring Framework"));
		distributor = new Distributor(space);
		clusters = distributor.distribute(2);
		clusters.forEach(System.out::println);

		space = new ExpertiseSpace();
		space.addRole("ROLE-1", List.of("Java", "Python", "PostgreSQL"/* "MySQL" */));
		space.addRole("ROLE-2", List.of("Java", "SQL", "Spring Boot"/* "MongoDB", "REST" */));
		space.addRole("ROLE-3", List.of("Java", "Python", "PostgreSQL", "Spring Boot"));
		space.addRole("ROLE-4", List.of(/* "Oracle" */"Hibernate", "Java", "Spring Framework", "SQL"));
		space.addRole("ROLE-5", List.of(/*
										 * "Concurrent Programming", "Manual Testing", "RabbitMQ",
										 */
				"Hibernate", "Java", "Spring Framework"));
		distributor = new Distributor(space);
		clusters = distributor.distribute(2);
		clusters.forEach(System.out::println);

		space = new ExpertiseSpace();
		space.addRole("ROLE-1", List.of("Java", "Python", "PostgreSQL", "MySQL"));
		space.addRole("ROLE-2", List.of("Java", "SQL", "Spring Boot", "MongoDB", "REST"));
		space.addRole("ROLE-3", List.of("Java", "Python", "PostgreSQL", "Spring Boot"));
		space.addRole("ROLE-4", List.of("Oracle", "Hibernate", "Java", "Spring Framework", "SQL"));
		space.addRole("ROLE-5", List.of("Concurrent Programming", "Manual Testing", "RabbitMQ", "Hibernate", "Java",
				"Spring Framework"));
		distributor = new Distributor(space);
		clusters = distributor.distribute(2);
		clusters.forEach(System.out::println);

		System.out.println("-----------------------------------");
		Parser parser = new Parser("Java Flavors.csv");
		space = parser.parse();
		System.out.println("%d: %s".formatted(space.getExpertiseTokenCount(),space.getExpertiseTokens()));
		distributor = new Distributor(space);
		clusters = distributor.distribute(10);
		clusters.forEach(System.out::println);

	}

}
