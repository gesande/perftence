package net.sf.perftence.wsdef;

import java.io.File;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;

import net.sf.iwant.api.core.ScriptGenerated;
import net.sf.iwant.api.model.TargetEvaluationContext;
import net.sf.iwant.api.target.TargetBase;

public class SvnRevisionProperties extends TargetBase {

	/**
	 * We have no control over svn state so we are always dirty, to keep things
	 * correct
	 */
	private static final long ALWAYS_DIRTY = System.currentTimeMillis();

	public SvnRevisionProperties() {
		super("svn-revision.properties");
	}

	@Override
	protected IngredientsAndParametersDefined ingredientsAndParameters(
			IngredientsAndParametersPlease iUse) {
		return iUse.parameter("always-dirty", ALWAYS_DIRTY).nothingElse();
	}

	@Override
	public void path(TargetEvaluationContext ctx) throws Exception {
		File tmp = ctx.freshTemporaryDirectory();
		File script = new File(tmp, "sh");
		FileUtils.writeStringToFile(script, script(ctx));
		script.setExecutable(true);

		ScriptGenerated.execute(ctx.wsRoot(),
				Arrays.asList(script.getAbsolutePath()));
	}

	private String script(TargetEvaluationContext ctx) {
		StringBuilder sh = new StringBuilder();
		sh.append("#!/bin/bash\n");
		sh.append("set -eu\n");
		sh.append("cd '").append(ctx.wsRoot().getAbsolutePath() + "'\n");
		sh.append("svn info | grep '^Revision: ' | sed 's/: /=/' >'")
				.append(ctx.cached(this)).append("'\n");
		return sh.toString();
	}

}
