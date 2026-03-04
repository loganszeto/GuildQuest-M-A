package backend;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public abstract class FileFactory<T extends Savable> {
	protected final void write(String f, T o) {
		 try {
			FileOutputStream fileOut = new FileOutputStream(f);
		    ObjectOutputStream out = new ObjectOutputStream(fileOut);
		    out.writeObject(o);
		    out.close();
		 } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
	}
	public abstract void save(T obj);
	protected final Object objfromfile(String f) {
		FileInputStream fileIn;
		try {
			fileIn = new FileInputStream(f);
			ObjectInputStream in = new ObjectInputStream(fileIn);
	        Object obj = in.readObject();
	        in.close();
	        return obj;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
        
	}
	public abstract T load(String f);
}
