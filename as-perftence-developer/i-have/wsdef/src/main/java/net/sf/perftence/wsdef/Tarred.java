package net.sf.perftence.wsdef;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.tools.ant.taskdefs.Tar;
import org.apache.tools.ant.taskdefs.Tar.TarCompressionMethod;
import org.fluentjava.iwant.api.model.Path;
import org.fluentjava.iwant.api.model.Target;
import org.fluentjava.iwant.api.model.TargetEvaluationContext;

public class Tarred extends Target {

	private Target target;

	public Tarred(String name, Target target) {
		super(name);
		this.target = target;
	}

	public static Target target(String name, Target target) {
		return new Tarred(name, target);
	}

	@Override
	public InputStream content(TargetEvaluationContext ctx) throws Exception {
		throw new UnsupportedOperationException("TODO test and implement");
	}

	@Override
	public List<Path> ingredients() {
		return Arrays.asList(target);
	}

	@Override
	public void path(TargetEvaluationContext ctx) throws Exception {
		File dest = ctx.cached(this);
		File cachedTarget = ctx.cached(target);
		Tar tar = new Tar();
		tar.setBasedir(cachedTarget);
		tar.setDestFile(dest);

		TarCompressionMethod compression = new TarCompressionMethod();
		compression.setValue("gzip");
		tar.setCompression(compression);

		tar.execute();
	}

	@Override
	public String contentDescriptor() {
		return getClass().getCanonicalName() + ":" + ingredients();
	}

}
