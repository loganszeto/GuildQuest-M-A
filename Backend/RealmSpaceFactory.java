package Backend;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import Backend.Maps.*;

public class RealmSpaceFactory{
	//does not inherit from FileFactory, because RealmSpaces will not user serialization.
	public RealmSpace load(String f) {
		switch (f) {
		case "UnknownRealm":
			return UnknownRealm.getSpace();
		case "RelicHunt":
			return RelicHunt.getSpace();
		default:
			return null;
		}
	}
}
