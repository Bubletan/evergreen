package eg;

import java.io.PrintStream;

public final class Logger extends PrintStream {
	
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private StackTraceElement prev;

	public Logger(PrintStream out) {
		super(out);
	}
	
	private void write(String s, boolean ln) {
		StackTraceElement ste = Thread.currentThread().getStackTrace()[3];
		if (ste.getFileName().equals("Console.scala")) {
			ste = Thread.currentThread().getStackTrace()[5];
		}
		if (prev != null) {
			if (ste.getClass() != prev.getClass() ||
					!ste.getMethodName().equals(prev.getMethodName())) {
				s = " (" + prev.getFileName() + ":" + prev.getLineNumber() + ") " + s;
			}
		}
		if (ln) {
			s = s + " (" + ste.getFileName() + ":" + ste.getLineNumber() + ")" + LINE_SEPARATOR;
			prev = null;
		} else {
			prev = ste;
		}
		super.print(s);
	} 
	
	@Override
	public void print(boolean b) {
		write(b ? "true" : "false", false);
	}
	
	@Override
	public void print(char c) {
		write(String.valueOf(c), false);
	}
	
	@Override
	public void print(int i) {
		write(String.valueOf(i), false);
	}
	
	@Override
	public void print(long l) {
		write(String.valueOf(l), false);
	}
	
	@Override
	public void print(float f) {
		write(String.valueOf(f), false);
	}
	
	@Override
	public void print(double d) {
		write(String.valueOf(d), false);
	}
	
	@Override
	public void print(char[] s) {
		write(new String(s), false);
	}
	
	@Override
	public void print(String s) {
		if (s == null) {
			s = "null";
		}
		write(s, false);
	}
	
	@Override
	public void print(Object obj) {
		write(String.valueOf(obj), false);
	}
	
	@Override
	public void println() {
		write(null, true);
	}
	
	@Override
	public void println(boolean x) {
		write((x ? "true" : "false"), true);
	}
	
	@Override
	public void println(char x) {
		write(String.valueOf(x), true);
	}
	
	@Override
	public void println(int x) {
		write(String.valueOf(x), true);
	}
	
	@Override
	public void println(long x) {
		write(String.valueOf(x), true);
	}
	
	@Override
	public void println(float x) {
		write(String.valueOf(x), true);
	}
	
	@Override
	public void println(double x) {
		write(String.valueOf(x), true);
	}
	
	@Override
	public void println(char[] x) {
		write(new String(x), true);
	}
	
	@Override
	public void println(String x) {
		if (x == null) {
			x = "null";
		}
		write(x, true);
	}
	
	@Override
	public void println(Object x) {
		write(String.valueOf(x), true);
	}
}
