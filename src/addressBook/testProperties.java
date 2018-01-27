package addressBook;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.DefaultListModel;

public class testProperties {

	private final String PROP_FILE_LOCATION = "myProperties.properties";
	private Properties contacts;
	DefaultListModel<String> nameList = new DefaultListModel<String>();
	
	public testProperties() {
		this.contacts = new Properties();
		this.initList();
	}
	
	private void initList() {
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args) {
		new testProperties();
		Map<String, String> m1 = new HashMap(); 
	      m1.put("Zara", "8");
	      m1.put("Mahnaz", "31");
	      m1.put("Ayan", "12");
	      m1.put("Daisy", "14");
	      
	      m1.put("Daisy", "12");

	      System.out.println();
	      System.out.println(" Map Elements");
	      System.out.print("\t" + m1);
	}

}