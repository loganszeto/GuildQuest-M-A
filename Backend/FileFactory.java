package backend;

public abstract class FileFactory<T extends Savable> {
	public abstract void save(T obj);
	public abstract T load(String t);
}
