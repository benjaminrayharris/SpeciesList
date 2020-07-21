// Author: Benjamin Harris
// object class for the masterlist widget
// April 24, 2020 

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import java.awt.Dimension;

public class Widget extends JPanel {
	private static final long serialVersionUID = -5023765160182873029L;
	private int usual;
	private String[] values;
	private JLabel[] labels;
	public JCheckBox addToListJChB;
	private JComboBox<String> dd1JCoB;
	private JComboBox<String> dd2JCoB;
	private JLabel usualJL;

	public Widget (String[] tt, String[] vals, SLEngine eng, int usualValue, String dd1v, String dd2v) {
		String[] keys = tt;
		values = vals;
		if (values.length != keys.length)
			UtilityShed.popup(values[0] + " :\n\nheader and values have different lengths", null); 
		usual = usualValue;
		setLay(keys, dd1v, dd2v, eng);
	} // end constructor Widget()

	private void setLay (String[] keys, String dd1v, String dd2v, SLEngine eng) {
		int[] cw = SLEngine.colWid;
		int rh = SLEngine.rowHei;
		Dimension dimS, dimM, dimL;
		int cws = 0; 
		for (int i = 0; i < cw.length; i++) {
			cws += cw[i];
		} // end for
		dimS = new Dimension(cws / 2, rh + 6);
		dimM = new Dimension(cws, rh + 6);
		dimL = new Dimension(cws * 3, rh + 6);
		setBorder(BorderFactory.createEtchedBorder());
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setMinimumSize(dimS);
		setPreferredSize(dimM);
		setMaximumSize(dimL);

		dimS = new Dimension(cw[0], rh);
		addToListJChB = new JCheckBox(values[0]);
		addToListJChB.setMinimumSize(dimS);
		addToListJChB.setMaximumSize(dimS);
		addToListJChB.setPreferredSize(dimS);
		addToListJChB.setToolTipText(keys[0] + " - check to add to the Unit List");
		addToListJChB.addActionListener(eng);
		add(addToListJChB);

		labels = new JLabel[values.length - 1];
		//add(Box.createRigidArea(dim));
		for (int i = 1; i < values.length; i++) {

			dimS = new Dimension(cw[i], rh);
			labels[i - 1] = new JLabel(i < values.length ? values[i] : "XXXX");
			labels[i - 1].setMinimumSize(dimS);
			labels[i - 1].setMaximumSize(dimS);
			labels[i - 1].setPreferredSize(dimS);
			labels[i - 1].setToolTipText(keys[i] + " : " + values[i]);
			add(labels[i - 1]);

			//add(Box.createRigidArea(dim));

		} // end for

		dd1JCoB = new JComboBox<String>(SLEngine.dd1);
		dd1JCoB.setSelectedIndex(getIndex(SLEngine.dd1, dd1v));
		//dd1JCoB.addActionListener(eng);
		dd1JCoB.setEnabled(false);
		add(dd1JCoB);

		dd2JCoB = new JComboBox<String>(SLEngine.dd2);
		dd2JCoB.setSelectedIndex(getIndex(SLEngine.dd2, dd2v));
		//dd2JCoB.addActionListener(eng);
		dd2JCoB.setEnabled(false);
		add(dd2JCoB);

		dimS = new Dimension(40, rh);
		usualJL = new JLabel("  " + usual);
		usualJL.setMinimumSize(dimS);
		usualJL.setPreferredSize(dimS);
		usualJL.setMaximumSize(dimS);
		usualJL.setToolTipText("usual suspect counter");
		add(usualJL);
		
		add(Box.createHorizontalGlue());
		setVisible(true);
	} // end setLay()
	
	private int getIndex(String[] arr, String str) {
		int index = 0;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].equals(str)) index = i;
		} // end for
		return index;
	} // end getIndex()

	public String[] getValues () {
		return values;
	} // end getValues()

	public void setValues (String[] newVals) {
		values = newVals;
	} // end setValues()

	public String getValue (int index) {
		return values[index];
	} // end getValue()j

	public boolean isChecked () {
		return addToListJChB.isSelected();
	} // end isChecked()

	public void makeCheck (boolean check) {
		addToListJChB.setSelected(check);
	} // end uncheck()

	public void bumpUsual () { 
		usual += 1;
		usualJL.setText("  " + usual);
	} // end setUsual()

	public int getUsual () {
		return usual;
	} // end getUsual()

	public String getDD1 () {
		return SLEngine.dd1[dd1JCoB.getSelectedIndex()];
	} // end getDD1()

	public String getDD2 () {
		return SLEngine.dd2[dd2JCoB.getSelectedIndex()];
	} // end getDD2()

	public void enableButtons () {
		boolean hasCheck = isChecked();
		dd1JCoB.setEnabled(hasCheck);
		dd2JCoB.setEnabled(hasCheck);
	} // end enableButtons()

	public String toString (boolean isMaster, boolean isEP, boolean isFirst) {
		String s = "";
		int prntCol = SLEngine.prntCol;
		try {
			if (isMaster || isChecked()) {
				if (!isFirst) s = "\n";
				for (int i = 0; i < values.length ; i++) {
					if (isMaster || i < prntCol) {
						if (i != 0) s += ",";
						s += (values[i]);// != null ? values[i] : "");
					} // end if
				} // end for
				if (isEP) {
					s += "," + getUsual();
				} // end if
				if (!isMaster) {
					s += "," + getDD1() + "," + getDD2();
				} // end if
			} // end if
		} catch (Exception e) { UtilityShed.popup(values[0] + " :\n\ntoString(): " + e, null); };
		return s;
	} // end toString()

} // end object class Widget
