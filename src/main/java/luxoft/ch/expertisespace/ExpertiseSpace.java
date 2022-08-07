package luxoft.ch.expertisespace;

import java.io.PrintWriter;
import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class ExpertiseSpace {

	private final Map<String, Integer> expertiseTokens;
	private final Map<String, BitSet> expertiseMap;

	public ExpertiseSpace() {
		expertiseTokens = new HashMap<>();
		expertiseMap = new LinkedHashMap<>();
	}

	public void addRole(String role, List<String> tokens) {
		addExpertiseTokens(tokens);
		addExpertiseSetForRole(role, tokens);
	}

	private void addExpertiseSetForRole(String role, List<String> tokens) {
		BitSet expertiseSet = new BitSet(expertiseTokens.size());
		tokens.forEach(token -> expertiseSet.set(expertiseTokens.get(token)));
		expertiseMap.put(role, expertiseSet);
	}

	private void addExpertiseTokens(List<String> tokens) {
		tokens.forEach(token -> expertiseTokens.computeIfAbsent(token, key -> expertiseTokens.size()));
	}

	private String getTokenByIndex(int index) {
		return expertiseTokens.entrySet().stream().filter(entry -> entry.getValue().equals(index))
				.map(Map.Entry::getKey).findAny().orElseThrow(() -> new NoExpertiseTokenException(
						"can't find expertise token for index %d".formatted(index)));
	}

	private void print(PrintWriter out) {
		for (var entry : expertiseMap.entrySet()) {
			out.print(entry.getKey());
			out.print(": ");
			BitSet set = entry.getValue();
			for (int index = set.nextSetBit(0); index >= 0; index = set.nextSetBit(index + 1)) {
				out.print(getTokenByIndex(index));
				if (set.nextSetBit(index + 1) >= 0) {
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
		for (var entry : expertiseMap.entrySet()) {
			builder.append(entry.getKey()).append(": ");
			StringJoiner join = new StringJoiner(", ");
			BitSet set = entry.getValue();
			for (int index = set.nextSetBit(0); index >= 0; index = set.nextSetBit(index + 1)) {
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
