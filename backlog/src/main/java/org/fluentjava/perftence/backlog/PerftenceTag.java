package org.fluentjava.perftence.backlog;

import java.util.ArrayList;
import java.util.List;

import net.sf.mybacklog.Tag;

public enum PerftenceTag implements Tag {
	developmentSupport, development, ide, codeQuality, deployment, feature, infrastructure, refactoring, build, backlog;

	public static List<Tag> toTags(String[] args) {
		final List<Tag> tags = new ArrayList<>();
		if (args.length > 0) {
			for (final String value : args) {
				tags.add(PerftenceTag.valueOf(value));
			}
		} else {
			for (final Tag tag : PerftenceTag.values()) {
				tags.add(tag);
			}
		}
		return tags;
	}
}
