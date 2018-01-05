package inmemoryfilesystem;

import java.util.ArrayList;
import java.util.List;

public class FileSystem {
	private final Directory root;
	private Directory currentDir;
	
	public FileSystem() {
		root = new Directory("/", null);
		currentDir = new Directory("/", null);
	}
	
	/**
	 * resolve a path like /foo/bar
	 */
	private List<Entry> resolve(String path) {
		assert path.startsWith("/");
		String[] components = path.substring(1).split("/");
		List<Entry> entries = new ArrayList<Entry>(components.length + 1);
		entries.add(root);
		
		Entry entry = root;
		for (String component : components) {
			if (entry == null || !(entry instanceof Directory)) {
				throw new IllegalArgumentException("invalid path: " + path);
			}
			if (!component.isEmpty()) {
				entry = ((Directory) entry).getChild(component);
				entries.add(entry);
			}
		}
		return entries;
	}
	
	public void mkdir(String path) {
		List<Entry> entries = resolve(path);
		if (entries.get(entries.size() - 1) != null) {
			throw new IllegalArgumentException("Directory already exists: " + path);
		}
		String[] components = path.split("/");
		final String dirName = components[components.length - 1];
		final Directory parent = (Directory) entries.get(entries.size() - 2);
		Directory newDir = new Directory(dirName, parent);
		parent.addEntry(newDir);
		currentDir = newDir;
	}
	
	public void createFile(String path) {
		assert !path.endsWith("/");
		List<Entry> entries = resolve(path);
		if (entries.get(entries.size() - 1) != null) {
			throw new IllegalArgumentException("File already exists: " + path);
		}
		final String fileName = path.substring(path.lastIndexOf("/") + 1);
		final Directory parent = (Directory) entries.get(entries.size() - 2);
		File file = new File(fileName, parent, 0);
		parent.addEntry(file);
		currentDir = parent;
	}
	
	public void pwd() {
		System.out.println(currentDir.getFullPath().substring(1)); 
	}
	
	public void cd() {
		List<Entry> fileContents = currentDir.getContents();
		for (Entry e : fileContents) {
			System.out.println(e.getName());
		}
	}
	
	
	public static void main(String[] args) {
		FileSystem fs = new FileSystem();
		fs.mkdir("/foo");
		fs.createFile("/foo/file1");
		fs.createFile("/foo/file2");
		fs.cd();
	}
}



















