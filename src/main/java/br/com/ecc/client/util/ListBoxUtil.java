package br.com.ecc.client.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.ListBox;

@SuppressWarnings("all")
public class ListBoxUtil {
	public static void setItemSelected(ListBox listBox, String value) {
		for (int i = 0; i < listBox.getItemCount(); i++) {
			if (value.equals(listBox.getValue(i))) {
				listBox.setItemSelected(i, true);
				break;
			}
		}
	}
	public static Object getItemSelected(ListBox listBox, Enum... emuns) {
		return getItemSelected(listBox, (Object[])emuns);
	}
	public static Object getItemSelected(ListBox listBox, Object... values) {
		List objects = new ArrayList();
		for(Object o : values) {
			objects.add(o);
		}
		return getItemSelected(listBox, objects);
	}
	public static Object getItemSelected(ListBox listBox, List values) {
		if(listBox.getSelectedIndex()==-1){
			return null;
		}
		for(Object obj : values) {
			if(obj.toString().equals(listBox.getValue(listBox.getSelectedIndex()))){
				return obj;
			}
		}
		return null;
	}
	
	public static void populate(ListBox listBox, Boolean bEnableNull, Enum... emuns) {
		populate(listBox, bEnableNull, (Object[])emuns);
	}
	
	public static void populate(ListBox listBox, Boolean bEnableNull, Object... values) {
		List objects = new ArrayList();
		for(Object o : values) {
			objects.add(o);
		}
		populate(listBox, bEnableNull, objects);
	}
	
	public static void populate(ListBox listBox, Boolean bEnableNull, List values) {
		listBox.clear();
		if(bEnableNull){
			listBox.addItem("");
		}
		for(Object obj : values) {
			listBox.addItem(obj.toString());
		}
		if(values.size()>0){
			listBox.setItemSelected(0, true);
		}
	}
	public static Object getListItemSelected(List values, String value) {
		if(values!=null){
			for(Object obj : values) {
				if(obj.toString().equals(value)){
					return obj;
				}
			}
		}
		return null;
	}
}