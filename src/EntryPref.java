// Author: Ben Harris
// object class for Entry Preferences
// June 23, 2020

public class EntryPref {

	private String[][] ep;

	public EntryPref () {
		ep = new String[0][7];
	} // end constructor EntryPref()

	public void add (String[] record) {
		String[][] tmplst = ep;
		String[] tmp = record;
		ep = new String[tmplst.length + 1][7];
		//UtilityShed.popup("EP length:" + ep.length,  null);
		for (int i = 0; i < tmplst.length; i++) {
			ep[i] = tmplst[i];
		} // end for
		for (int j = 0; j < ep[0].length; j++) {
			ep[ep.length - 1][j] = (tmp.length > j ? tmp[j] : "");
			//UtilityShed.popup((tmp.length > j ? tmp[j] : ""), null);
		} // end for
		
	} // end add()

	public String[] getEntryPref (String key1, String key2, String key3, String key4) {
		String[] vals = new String[3];
		int index = getIndex(key1, key2, key3, key4);
		if (index < 0) {
			return new String[]{"0", "", ""};
		} // end if
		vals[0] = ep[index][4];
		vals[1] = ep[index][5];
		vals[2] = ep[index][6];
		
		return vals;
	} // end getEntryPref()

	private int getIndex (String key1, String key2, String key3, String key4) {
		for (int i = 0; i < ep.length; i++) {
			if (key1.equals(ep[i][0]) && key2.equals(ep[i][1]) && key3.equals(ep[i][2]) && key4.equals(ep[i][3])) {
				return i;
			} // end if
		} // end for
		return -1;
	} // end getIndex()

} // end object class EntryPref
