package Backend;

public interface Savable<T>{
	public T load();
	public String save();
	
}
