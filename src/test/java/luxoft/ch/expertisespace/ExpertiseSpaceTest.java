package luxoft.ch.expertisespace;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import luxoft.ch.expertisespace.clustering.Cluster;
import luxoft.ch.expertisespace.model.ExpertiseSpace;

class ExpertiseSpaceTest {

	@Test
	void test1() {
		ExpertiseSpace space = new ExpertiseSpace();
		space.addRole("ROLE-1", List.of("Java"));
		space.addRole("ROLE-2", List.of("Java"));
		space.addRole("ROLE-3", List.of("Java"));
		space.addRole("ROLE-4", List.of("Spring Framework"));
		space.addRole("ROLE-5", List.of("Spring Framework"));
		Distributor distributor = new Distributor(space);
		Set<Cluster> clusters = distributor.distribute(2);
		Assertions.assertEquals(
				Set.of(new Cluster(0, "ROLE-1", "ROLE-2", "ROLE-3"), new Cluster(1, "ROLE-4", "ROLE-5")), clusters);
	}

	@Test
	void test2() {
		ExpertiseSpace space = new ExpertiseSpace();
		space.addRole("ROLE-1", List.of("Java", "Python"));
		space.addRole("ROLE-2", List.of("Java"));
		space.addRole("ROLE-3", List.of("Java", "Python"));
		space.addRole("ROLE-4", List.of("Java", "Spring Framework"));
		space.addRole("ROLE-5", List.of("Java", "Spring Framework"));
		Distributor distributor = new Distributor(space);
		Set<Cluster> clusters = distributor.distribute(2);
		Assertions.assertEquals(
				Set.of(new Cluster(0, "ROLE-1", "ROLE-2", "ROLE-3"), new Cluster(1, "ROLE-4", "ROLE-5")), clusters);
	}

	@Test
	void test3() {
		ExpertiseSpace space = new ExpertiseSpace();
		space.addRole("ROLE-1", List.of("Java", "Python", "PostgreSQL"));
		space.addRole("ROLE-2", List.of("Java", "SQL"));
		space.addRole("ROLE-3", List.of("Java", "Python", "PostgreSQL"));
		space.addRole("ROLE-4", List.of("Java", "Spring Framework", "SQL"));
		space.addRole("ROLE-5", List.of("Java", "Spring Framework"));
		Distributor distributor = new Distributor(space);
		Set<Cluster> clusters = distributor.distribute(2);
		Assertions.assertEquals(
				Set.of(new Cluster(0, "ROLE-1", "ROLE-3"), new Cluster(1, "ROLE-2", "ROLE-4", "ROLE-5")), clusters);
	}

	@Test
	void test4() {
		ExpertiseSpace space = new ExpertiseSpace();
		space.addRole("ROLE-1", List.of("Java", "Python", "PostgreSQL"));
		space.addRole("ROLE-2", List.of("Java", "SQL", "Spring Boot"));
		space.addRole("ROLE-3", List.of("Java", "Python", "PostgreSQL", "Spring Boot"));
		space.addRole("ROLE-4", List.of("Hibernate", "Java", "Spring Framework", "SQL"));
		space.addRole("ROLE-5", List.of("Hibernate", "Java", "Spring Framework"));
		Distributor distributor = new Distributor(space);
		Set<Cluster> clusters = distributor.distribute(2);
		Assertions.assertEquals(
				Set.of(new Cluster(0, "ROLE-1", "ROLE-2", "ROLE-3"), new Cluster(1, "ROLE-4", "ROLE-5")), clusters);
	}

	@Test
	void test5() {
		ExpertiseSpace space = new ExpertiseSpace();
		space.addRole("ROLE-1", List.of("Java", "Python", "PostgreSQL", "MySQL"));
		space.addRole("ROLE-2", List.of("Java", "SQL", "Spring Boot", "MongoDB", "REST"));
		space.addRole("ROLE-3", List.of("Java", "Python", "PostgreSQL", "Spring Boot"));
		space.addRole("ROLE-4", List.of("Oracle", "Hibernate", "Java", "Spring Framework", "SQL"));
		space.addRole("ROLE-5", List.of("Concurrent Programming", "Manual Testing", "RabbitMQ", "Hibernate", "Java",
				"Spring Framework"));
		Distributor distributor = new Distributor(space);
		Set<Cluster> clusters = distributor.distribute(2);
		Assertions.assertEquals(
				Set.of(new Cluster(0, "ROLE-1", "ROLE-2", "ROLE-3"), new Cluster(1, "ROLE-4", "ROLE-5")), clusters);
	}

}
