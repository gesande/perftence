package net.sf.perftence;

class FullyQualifiedMethodNameWithClassName {
	@SuppressWarnings("static-method")
	public String idFor(final Class<?> clazz, final String methodName) {
		return new StringBuilder(clazz.getName()).append(".")
				.append(methodName).toString();
	}
}