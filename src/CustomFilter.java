import java.io.File;
import javax.swing.filechooser.FileFilter;

public class CustomFilter extends FileFilter {
	public boolean accept (File f) { return false; }
	public String getDescription () { return null; }
	public String getName () { return null; }
	public String getEnd () { return null; }
} // end class CustomFilter

class CSVFilter extends CustomFilter {
	public boolean accept(File f) {
		if (f.isDirectory()) return true;
		String fName = f.getName();
		fName = fName.toLowerCase();
		return fName.endsWith(".csv");
	} // end accept()
	public String getDescription() {
		return "*.csv";
	} // end getDescription()
	public String getName() {
		return "Comma Separated Values";
	} // end getName()
	public String getEnd () {
		return ".csv";
	} // end getEnd()
} // end class CSVFilter

class EPrefFilter extends CustomFilter {
	public boolean accept(File f) {
		if (f.isDirectory()) return true;
		String fName = f.getName();
		fName = fName.toLowerCase();
		return fName.endsWith(".entry.pref");
	} // end accept()
	public String getDescription() {
		return "*.entry.pref";
	} // end getDescription()
	public String getName() {
		return "Entry Preferences";
	} // end getName()
	public String getEnd () {
		return ".entry.pref";
	} // end getEnd()
} // end class EPrefFilter

class SPrefFilter extends CustomFilter {
	public boolean accept(File f) {
		if (f.isDirectory()) return true;
		String fName = f.getName();
		fName = fName.toLowerCase();
		return fName.endsWith(".settings.pref");
	} // end accept()
	public String getDescription() {
		return "*.settings.pref";
	} // end getDescription()
	public String getName() {
		return "Settings Preferences";
	} // end getName()
	public String getEnd () {
		return ".settings.pref";
	} // end getEnd()
} // end class SPrefFilter
