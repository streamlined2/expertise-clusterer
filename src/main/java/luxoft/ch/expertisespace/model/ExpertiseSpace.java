package luxoft.ch.expertisespace.model;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ExpertiseSpace implements Iterable<Role> {

	private final Map<String, Integer> expertiseTokens;
	private final Set<Role> roleSet;

	public ExpertiseSpace() {
		expertiseTokens = new HashMap<>();
		roleSet = new TreeSet<>();
	}

	public int getRolesCount() {
		return roleSet.size();
	}

	public String getExpertiseTokens() {
		return expertiseTokens.keySet().stream().collect(Collectors.joining(",", "{", "}"));
	}

	public int getExpertiseTokenCount() {
		return expertiseTokens.size();
	}

	public void addRole(String roleName, List<String> tokens) {
		addExpertiseTokens(tokens);
		addExpertiseSetForRole(roleName, tokens);
	}

	private void addExpertiseSetForRole(String roleName, List<String> tokens) {
		Role role = new Role(roleName, expertiseTokens.size());
		tokens.forEach(token -> role.getPoint().set(expertiseTokens.get(token)));
		roleSet.add(role);
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

	@Override
	public Iterator<Role> iterator() {
		return new Iterator<Role>() {
			private final Iterator<Role> iterator = roleSet.iterator();

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public Role next() {
				return iterator.next();
			}

		};
	}

}
