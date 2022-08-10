package luxoft.ch.expertisespace.parsing;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import luxoft.ch.expertisespace.model.ExpertiseSpace;

public class Parser {

	private static final Pattern EXPERTISE_SEPARATOR = Pattern.compile(",\s*");

	private final String fileName;

	public Parser(String fileName) {
		this.fileName = fileName;
	}

	public ExpertiseSpace parse() {
		try (Stream<String> lineStream = Files
				.lines(Path.of(getClass().getClassLoader().getResource(fileName).toURI()))) {
			ExpertiseSpace space = new ExpertiseSpace();
			lineStream.skip(1).forEach(line -> addLine(space, line));
			return space;
		} catch (IOException | URISyntaxException e) {
			throw new ParseException("can't read file %s".formatted(fileName), e);
		}
	}

	private void addLine(ExpertiseSpace space, String line) {
		try {
			String[] parts = line.split(";");
			if (parts.length > 2) {
				throw new ParseException("line %s must contain exactly one ; delimiter".formatted(line));
			}
			String role = parts[0].trim();
			String expertiseList = parts[1];
			Matcher matcher = EXPERTISE_SEPARATOR.matcher(expertiseList);
			List<String> tokens = new ArrayList<>();
			int index = 0;
			while (matcher.find()) {
				tokens.add(expertiseList.substring(index, matcher.start()));
				index = matcher.end();
			}
			tokens.add(expertiseList.substring(index).trim());
			space.addRole(role, tokens);
		} catch (ArrayIndexOutOfBoundsException | IllegalStateException e) {
			throw new ParseException("wrong source file format", e);
		}

	}

}
