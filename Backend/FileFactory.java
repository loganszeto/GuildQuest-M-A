package backend;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public abstract class FileFactory<T extends Savable> {
	public void write(String f, String v) {
		 try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(f));
			writer.write(v);
			writer.close();
		 } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
	}
	public abstract void save(T obj);
	public abstract T load(String f);
}
