package luxoft.ch.expertisespace.model;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ExpertiseSpace {

	private final Map<String, Integer> expertiseTokens;
	private final Set<Role> roleSet;

	public ExpertiseSpace() {
		expertiseTokens = new HashMap<>();
		roleSet = new TreeSet<>();
	}

	public Set<Role> getRoles() {
		return Collections.unmodifiableSet(roleSet);
	}

	public String getExpertiseTokens() {
		return expertiseTokens.keySet().stream().collect(Collectors.joining(",", "{", "}"));
	}

	public int getExpertiseTokenCount() {
		return expertiseTokens.size();
	}

	public void addRole(String role, List<String> tokens) {
		addExpertiseTokens(tokens);
		addExpertiseSetForRole(role, tokens);
	}

	private void addExpertiseSetForRole(String role, List<String> tokens) {
		Role point = new Role(role, expertiseTokens.size());
		tokens.forEach(token -> point.set(expertiseTokens.get(token)));
		roleSet.add(point);
	}

	private void addExpertiseTokens(List<String> tokens) {
		tokens.forEach(token -> expertiseTokens.computeIfAbsent(token, key -> expertiseTokens.size()));
	}

	private String getTokenByIndex(int index) {
		return expertiseTokens.entrySet().stream().filter(entry -> entry.getValue().equals(index))
				.map(Map.Entry::getKey).findAny().orElseThrow(() -> new NoExpertiseTokenException(
						"can't find expertise token for index %d".formatted(index)));
	}

	public void print(PrintWriter out) {
		for (var role : roleSet) {
			out.print(role.getName());
			out.print(": ");
			for (var i = role.getPoint().iterator(); i.hasNext();) {
				out.print(getTokenByIndex(i.next()));
				if (i.hasNext()) {
					out.print(", ");
				}
			}
			out.println();
		}
		out.flush();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (var role : roleSet) {
			builder.append(role.getName()).append(": ");
			StringJoiner join = new StringJoiner(", ");
			for (var index : role.getPoint()) {
				join.add(getTokenByIndex(index));
			}
			builder.append(join).append('\n');
		}
		return builder.toString();
	}

	public static void main(String... args) {
		ExpertiseSpace space = new ExpertiseSpace();
		space.addRole("ROLE-1", List.of("Java", "MySQL", "PostgreSQL", "Python"));
		space.addRole("ROLE-2", List.of("Java", "MongoDB", "REST", "Spring Boot", "SQL"));
		space.addRole("ROLE-3", List.of("Java", "Spring Boot", "PostgreSQL", "Python"));
		space.addRole("ROLE-4", List.of("Hibernate", "Java", "Oracle", "Spring Framework", "SQL"));
		space.addRole("ROLE-5", List.of("Java", "Concurrent Programming", "Hibernate", "Manual Testing", "RabbitMQ",
				"Spring Framework"));
		space.print(new PrintWriter(System.out));
	}

}
