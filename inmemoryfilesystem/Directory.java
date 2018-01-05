package inmemoryfilesystem;

import java.util.ArrayList;

public class Directory extends Entry {
	private ArrayList<Entry> contents;
	
	public Directory(String n, Directory p) {
		super(n, p);
		contents = new ArrayList<>();
	}
	
	protected ArrayList<Entry> getContents() {
		return contents;
	}
	
	public int size() {
		int size = 0;
		for (Entry e : contents) {
			size += e.size();
		}
		return size;
	}
	
	public int numberOfFiles() {
		int count = 0;
		for (Entry e : contents) {
			if (e instanceof Directory) {
				count++;	// Directory counts as a file.
				Directory d = (Directory) e;
				count += d.numberOfFiles();
			}
			else if (e instanceof File) {
				count++;
			}
		}
		return count;
	}
	
	public Entry getChild(String name) {
		for (Entry e : contents) {
			if (e.getName().equals(name)) {
				return e;
			}
		}
		return null;
	}
	
	public boolean deleteEntry(Entry entry) {
		return contents.remove(entry);
	}
	
	public void addEntry(Entry entry) {
		contents.add(entry);
	}
}
