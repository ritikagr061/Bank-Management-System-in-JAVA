import javax.swing.text.html.Option;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import java.lang.Integer;
import java.util.SimpleTimeZone;

import java.io.FileWriter;
import java.io.File;


public class Dashboard {

	public static HashMap<Integer, ArrayList<String[]>> Trans = new HashMap<Integer, ArrayList<String[]>>();
	public static HashMap<Integer,String[]> Acc = new HashMap<Integer,String[]>();
	public static ArrayList<String[]> TransactionTable= new ArrayList<String[]>();
	public static ArrayList<String[]> AccountTable = new ArrayList<String[]>();

	public static void main(String[] args) throws IOException, ParseException {

		BufferedReader br = new BufferedReader(new FileReader("Account_Data.csv"));
		BufferedReader br2 = new BufferedReader(new FileReader("Transaction_Data.csv"));
		String line =  null;

		//storing Account Data
		boolean poss=true;
		while((line=br.readLine())!=null){
			//System.out.println(line);
			String str[] = line.split(",");
			//System.out.println(str[0]);
			String[] temp= new String[6];

			for(int i=0;i<str.length;i++){
				temp[i]=str[i];
				//System.out.println(temp[i].getClass());
			}
			if(poss)
			{
				AccountTable.add(temp);
				poss=false;
				continue;
			}

			int accNo=Integer.parseInt(temp[5]);
			//System.out.println(accNo);
			Acc.put(accNo,temp);
			AccountTable.add(temp);
		}

		poss=true;
		//storing transaction data
		while((line=br2.readLine())!=null){
			String str[] = line.split(",");
			String[] temp= new String[5];

			for(int i=0;i<str.length;i++){
				temp[i]=str[i];
				//System.out.println(temp[i].getClass());
			}

			TransactionTable.add(temp); //adding row to transactionTable
			// later on this transactionTable will be used to write updated value in the csv
			if(poss)
			{
				poss=false;
				continue;
			}

			//System.out.println(temp[0]);
			int accNo=Integer.valueOf(temp[0]);
			//System.out.println(accNo);
			ArrayList<String[]> temp2=new ArrayList<>();

			if(Trans.containsKey(accNo)) {
				temp2=Trans.get(accNo);
				temp2.add(temp);
				Trans.put(accNo,temp2);
			}
			else{
				temp2.add(temp);
				Trans.put(accNo,temp2);
			}
		}
		//System.out.println(Trans);
		introduction();
	}

	public static void introduction() throws ParseException {

		System.out.println("Welcome to the ATM Project!");

		Functionalities obj1=new Functionalities();
		try {
			obj1.Greet();
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}

		try {
			writeToCsv("Account_Data.csv",AccountTable); //whatever the changes code has made will be written to csv
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		try {
			writeToCsv("Transaction_Data.csv",TransactionTable);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void writeToCsv(String path,ArrayList<String[]> Table) throws IOException {
		File csvFile = new File(path);
		FileWriter fileWriter = new FileWriter(csvFile);

		for (String[] data : Table) {
			StringBuilder line = new StringBuilder();
			for (int i = 0; i < data.length; i++) {
				line.append(data[i]);
				if (i != data.length - 1) {
					line.append(',');
				}
			}
			line.append("\n");
			fileWriter.write(line.toString());
		}

		fileWriter.close();
	}
}
