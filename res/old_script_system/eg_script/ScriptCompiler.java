package eg.script;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ScriptCompiler {
	
	private static final String SOURCE_DIRECTORY = "src/eg/script/src/";
	private static final String SOURCE_FILE_EXTENSION = "egs";
	private static final String GEN_FILE = "src/eg/script/gen/ScriptGen.java";
	
	private static ScriptCompiler instance;
	
	private ScriptCompiler() {
	}
	
	public static void main(String[] args) {
		getScriptCompiler().compile();
	}
	
	public static final ScriptCompiler getScriptCompiler() {
		if (instance == null) {
			instance = new ScriptCompiler();
		}
		return instance;
	}
	
	private Map<String, List<String>> labels;
	private List<String> imports;
	private List<String> consts;
	
	public final void compile() {
		
		labels = new HashMap<>();
		imports = new ArrayList<>();
		consts = new ArrayList<>();
		
		for (File file : getSourceFiles()) {
			try {
				String path = file.getAbsolutePath();
				path = path.replace("\\", "/");
				path = path.substring(path.indexOf("eg/script/src/") + 14);
				parseSourceFile(path, new String(Files.readAllBytes(file.toPath())).toCharArray());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		writeGeneratedFile();
	}
	
	private final File[] getSourceFiles() {
		List<File> dirs = new ArrayList<>();
		dirs.add(new File(SOURCE_DIRECTORY));
		List<File> files = new ArrayList<>();
		while (dirs.size() != 0) {
			for (File file : dirs.remove(0).listFiles()) {
				if (file.exists()) {
					if (file.isDirectory()) {
						dirs.add(file);
					} else {
						String name = file.getName();
						if (name.contains(".") &&
								name.substring(name.lastIndexOf('.') + 1).equalsIgnoreCase(SOURCE_FILE_EXTENSION)) {
							files.add(file);
						}
					}
				}
			}
		}
		return files.toArray(new File[files.size()]);
	}
	
	private final void parseSourceFile(String path, char[] chars) {
		for (int i = 0, length = chars.length; i < length; i++) {
			if (chars[i] == '@' && chars[++i] == '.') {
				String keyword = "";
				char c;
				while ((c = chars[++i]) == '_' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z' ||
						(c >= '0' && c <= '9'))) {
					keyword += c;
				}
				if (c == '{') {
					i--;
				}
				if (keyword.equals("import")) {
					String base = "";
					String content = "";
					if (c == '.') {
						while ((c = chars[++i]) != '{') {
							base += c;
						}
						base = base.trim() + '.';
					} else {
						while (chars[++i] != '{');
					}
					c = chars[++i];
					do {
						content += c;
					} while ((c = chars[++i]) != '}');
					for (String add : content.split(",|\r\n|\r|\n")) {
						if (add.trim().length() != 0) {
							imports.add(base + add.trim());
						}
					}
				} else if (keyword.equals("const")) {
					//TODO
				} else if (keyword.equals("label") && c == '.') {
					String name = "";
					while ((c = chars[++i]) == '_' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z' ||
							(c >= '0' && c <= '9'))) {
						name += c;
					}
					if (c != '{') {
						while (chars[++i] != '{');
					}
					String block = "";
					c = chars[++i];
					int depth = 0;
					while (true) {
						if (c == '{') {
							depth++;
						}
						if (c == '}') {
							depth--;
						}
						if (depth < 0) {
							break;
						}
						block += c;
						c = chars[++i];
					}
					
					List<String> content;
					if (labels.containsKey(name)) {
						content = labels.get(name);
					} else {
						content = new ArrayList<>();
						labels.put(name, content);
					}
					StringBuilder sb = new StringBuilder("\t// " + path + "\n");
					sb.append(parseLabel(block));
					content.add(sb.toString());
				}
			}
		}
	}
	
	private final String parseLabel(String src) {
		String[][] replace = {
				{"$.invoke(", "eg.script.Scripts.invoke(player$author, "},
				{"$.this", "player$author"},
				{"$.int", "Integer.parseInt"},
				{"$.float", "Float.parseFloat"},
				{"$.double", "Double.parseDouble"},
				{"$.byte", "Byte.parseByte"},
				{"$.short", "Short.parseShort"},
				{"$.boolean", "Boolean.parseBoolean"},
			};
		for (String[] r : replace) {
			src = src.replace(r[0], r[1]);
		}
		for (int i = 0, length = src.length(); i < length; i++) {
			if (src.charAt(i) == '#' && src.charAt(i + 1) == '.') {
				src = src.substring(0, i) + "args$.<" + src.substring(i + 2, length);
				i += 5;
				length += 5;
				while (src.charAt(++i) != '.');
				src = src.substring(0, i) + ">get(\"" + src.substring(i + 1, length);
				i += 5;
				length += 5;
				char c;
				while ((c = src.charAt(++i)) == '_' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z' ||
						(c >= '0' && c <= '9')));
				src = src.substring(0, i) + "\")" + src.substring(i, length);
				length += 2;
				i += 2;
			}
		}
		return src;
	}
	
	private final void writeGeneratedFile() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(GEN_FILE)))) {
			writer.write("package eg.script.gen;\n\n");
			if (imports.size() != 0) {
				for (String s : imports) {
					writer.write("import " + s + ";\n");
				}
				writer.write("\n");
			}
			writer.write("/**\n");
			writer.write(" * Generated file, do not modify.\n");
			writer.write(" */\n");
			writer.write("public class ScriptGen {\n\n");
			if (consts.size() != 0) {
				for (int i = 0; i < consts.size(); i++) {
					String s = consts.get(i);
					writer.write("\tprivate static final ");
					String s3 = s.substring(0, s.indexOf("=")).replaceAll(" +", " ").trim();
					writer.write(s3.substring(0, s3.lastIndexOf(' ')) + " ");
					writer.write("const$" + s3.substring(s3.lastIndexOf(' ') + 1) + " =");
					String s4 = s.substring(s.indexOf("=") + 1).trim();
					if (!s4.startsWith(" ")) {
						writer.write(" ");
					}
					writer.write(s4);
					writer.write(";\n");
				}
				writer.write("\n");
			}
			writer.write("\tpublic void invoke(eg.model.player.Player player$author, String label$name, eg.script.ScriptArguments args$) {\n");
			if (labels.size() != 0) {
				writer.write("\t\tswitch (label$name) {\n");
				for (String key : labels.keySet()) {
					writer.write("\t\tcase \"" + key + "\":\n");
					for (String value : labels.get(key)) {
						writer.write("\t\t\ttry {\n");
						for (String line : value.split("\r\n|\r|\n")) {
							if (line.trim().length() != 0) {
								writer.write("\t\t\t" + line + "\n");
							}
						}
						writer.write("\t\t\t} catch (Throwable throwable$) {\n\t\t\t\tthrowable$.printStackTrace();\n\t\t\t}\n");
					}
					writer.write("\t\t\tbreak;\n");
				}
				writer.write("\t\t}\n");
			}
			writer.write("\t}\n}\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
