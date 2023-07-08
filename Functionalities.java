import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Scanner;
import java.lang.Integer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

//For handling of Date And Time
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDate;
import java.time.LocalTime;


public class Functionalities {
	Scanner menuInput = new Scanner(System.in);

    public void Greet() throws ParseException {

        System.out.println("Hi, there tell us what can we do for you");
        System.out.println("press 1 for creating new account");
        System.out.println("press 2 for doing a transaction");
        System.out.println("press 3 for checking balance in your account");

        int choice = Integer.parseInt(menuInput.nextLine());
        switch (choice){
            case 1:
                createAccount();
                break;
            case 2:
                try {
                    doTransaction();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                break;
            case 3:
                checkBalance();
                break;
            default:
                System.out.println("Invalid Choice");
        }

    }
	public void createAccount(){
        System.out.println("Enter the below details for account opening :");

        System.out.println("Account Type?");
        String accType = menuInput.nextLine();

        System.out.println("Username ?");
        String username = menuInput.nextLine();

        System.out.println("age ?");
        String age = menuInput.nextLine();

        System.out.println("Deposit Amount ?");
        String depositAmt = menuInput.nextLine();


        int accNumb=Dashboard.Acc.size()+1;
        String accountNumber=Integer.toString(accNumb);

        String[] temp= new String[6];
        temp[0]=username;
        temp[1]=age;
        temp[2]=accType;
        temp[3]=depositAmt;
        temp[4]=DateHandle.findDate();
        temp[5]=accountNumber;

        //write back to the file;
        Dashboard.AccountTable.add(temp);
        print(temp);

        String[] newEntry=new String[5];
        newEntry[0]=accountNumber;
        newEntry[1]="Deposit";
        newEntry[2]=depositAmt;
        newEntry[3]=DateHandle.findDate();
        newEntry[4]=DateHandle.findTime();

        Dashboard.TransactionTable.add(newEntry);
    }

    public void doTransaction() throws ParseException {
        System.out.println("Please Enter the account number");
        //System.out.println("Account Type");
        String accountNumber = menuInput.nextLine();

        System.out.println("Enter the type of transaction ie 1 for Deposit or 2 for Withdrawl");

        int choice = Integer.parseInt(menuInput.nextLine());
        if(choice==1)
        {
            doDeposit(accountNumber);
        }
        else if(choice==2)
        {
            doWidthdraw(accountNumber);
        }
        else{
            System.out.println("enter a valid choice");
        }
    }

    public void doWidthdraw(String accountNumber) throws ParseException {
        String[] accDetails = new String[6];
        accDetails = Dashboard.Acc.get(Integer.parseInt(accountNumber));

        ArrayList<String[]> transacList = new ArrayList<String[]>();
        transacList=Dashboard.Trans.get(Integer.parseInt(accountNumber));

        transacList.sort(new UserComparator());  //sorted the trasactionList based on Date and time

        int balanceAtAccountOpening = getBalanceAtAccountOpening(accountNumber,transacList);
        int balanceAtMonthStarting = getBalanceAtMonthStarting(accountNumber,transacList);
        int numberofTransactionsOnSameDay = getNumberOfTransactionOnSameDay(accountNumber,transacList);

        System.out.println("Enter the Amount you want to Widthdraw");
        String Amount = menuInput.nextLine();
        String currAmount = accDetails[3];

        if (accDetails[2].equals("Savings")) {
            if (Integer.parseInt(Amount) > Integer.parseInt(currAmount)) {
                System.out.println("Insufficient Balance!");
            } else if (Integer.parseInt(Amount) > 0.4 * balanceAtMonthStarting) {
                System.out.println("Balance at month Starting was: "+balanceAtMonthStarting+" 40% of it is: "+(0.4*balanceAtMonthStarting));
                System.out.println("Not allowed as amount is 40% of Balance at month Starting!!");
            }
            else if (numberofTransactionsOnSameDay >= 2) {
                System.out.println("Not allowed as you have already reached the Daily transaction limit of 2");

            } else {
                //changing Deposited_amount in AccountTable
                int finalAmount=Integer.parseInt(currAmount)-Integer.parseInt(Amount);
                Dashboard.AccountTable.get(Integer.parseInt(accountNumber))[3]=Integer.toString(finalAmount);

                //adding transaction in TransactionTable
                String[] newEntry=new String[5];
                newEntry[0]=accountNumber;
                newEntry[1]="Withdraw";
                newEntry[2]=Amount;
                newEntry[3]=DateHandle.findDate();
                newEntry[4]=DateHandle.findTime();

                Dashboard.TransactionTable.add(newEntry);
                print(newEntry);
            }
        }
        else{
            if (Integer.parseInt(Amount) > Integer.parseInt(currAmount)) {
                System.out.println("Insufficient Balance!");
            }
            else if (Integer.parseInt(Amount) > 0.8* balanceAtMonthStarting) {
                System.out.println("Balance at month Starting was: "+balanceAtMonthStarting+" 80% of it is: "+(0.8*balanceAtMonthStarting));
                System.out.println("Not allowed as amount is 80% of Balance at month Starting!!");
            }
            else if (numberofTransactionsOnSameDay >= 5) {
                System.out.println("Not allowed as you have already reached the Daily transaction limit of 5");
            }
            else{ //do transaction

                int finalAmount=Integer.parseInt(currAmount)-Integer.parseInt(Amount);
                Dashboard.AccountTable.get(Integer.parseInt(accountNumber))[3]=Integer.toString(finalAmount);

                //adding transaction in TransactionTable
                String[] newEntry=new String[5];
                newEntry[0]=accountNumber;
                newEntry[1]="Withdraw";
                newEntry[2]=Amount;
                newEntry[3]=DateHandle.findDate();
                newEntry[4]=DateHandle.findTime();

                Dashboard.TransactionTable.add(newEntry);
                print(newEntry);
            }
        }

    }


    public void doDeposit(String accountNumber) throws ParseException {
        String[] accDetails=new String[6];
        accDetails=Dashboard.Acc.get(Integer.parseInt(accountNumber));

        ArrayList<String[]> transacList = new ArrayList<String[]>();
        transacList=Dashboard.Trans.get(Integer.parseInt(accountNumber));

        transacList.sort(new UserComparator());

        System.out.println("Enter the Amount you want to Deposit");
        String Amount = menuInput.nextLine();
        String currAmount=accDetails[3];

        int balanceAtAccountOpening = getBalanceAtAccountOpening(accountNumber,transacList);
        int balanceAtMonthStarting = getBalanceAtMonthStarting(accountNumber,transacList);
        int numberofTransactionsOnSameDay = getNumberOfTransactionOnSameDay(accountNumber,transacList);


        if(accDetails[2].equals("Savings"))
        {
            if (Integer.parseInt(Amount) > 5 * balanceAtAccountOpening) {
                System.out.println("not allowed as amount is 5 times of AccountOpening");
            }
            else if (numberofTransactionsOnSameDay >= 2) {
                System.out.println("Not allowed as you have already reached the Daily transaction limit of 2 for Savings Account");
            }
            else{
                //changing Deposit_amount in AccountTable
                int finalAmount=Integer.parseInt(currAmount)+Integer.parseInt(Amount);
                Dashboard.AccountTable.get(Integer.parseInt(accountNumber))[3]=Integer.toString(finalAmount);

                //adding transaction in TransactionTable
                String[] newEntry=new String[5];
                newEntry[0]=accountNumber;
                newEntry[1]="Deposit";
                newEntry[2]=Amount;
                newEntry[3]=DateHandle.findDate();
                newEntry[4]=DateHandle.findTime();

                Dashboard.TransactionTable.add(newEntry);
                print(newEntry);
            }
        }
        else {
                if (Integer.parseInt(Amount) > 10 * balanceAtAccountOpening) {
                    System.out.println("not allowed as amount is 10 times of AccountOpening");
                }
                else if (numberofTransactionsOnSameDay >= 5) {
                    System.out.println("Not allowed as you have already reached the Daily transaction limit");
                }
                else {

                    //changing Deposit_amount in AccountTable
                    int finalAmount=Integer.parseInt(currAmount)+Integer.parseInt(Amount);
                    Dashboard.AccountTable.get(Integer.parseInt(accountNumber))[3]=Integer.toString(finalAmount);

                    //adding transaction in TransactionTable
                    String[] newEntry=new String[5];
                    newEntry[0]=accountNumber;
                    newEntry[1]="Deposit";
                    newEntry[2]=Amount;
                    newEntry[3]=DateHandle.findDate();
                    newEntry[4]=DateHandle.findTime();

                    Dashboard.TransactionTable.add(newEntry);
                    print(newEntry);
                }
        }
    }

    public void checkBalance(){
        System.out.println("Please Enter the account number");
        //System.out.println("Account Type");
        String accountNumber = menuInput.nextLine();
        String[] accDetails = new String[6];
        accDetails = Dashboard.Acc.get(Integer.parseInt(accountNumber));

        ArrayList<String[]> transacList = new ArrayList<String[]>();
        transacList = Dashboard.Trans.get(Integer.parseInt(accountNumber));
        Collections.sort(transacList, new UserComparator());  //sorted the trasactionList based on Date and time


        int numberofInquiry =  getNumberOfInquiry(accountNumber,transacList);
        int ans=0;
        if(accDetails[2].equals("Savings")){
            if(numberofInquiry>=7){
                System.out.println("You Have Exceeded the Enquiry Limit for Savings Account (which is 7)");
            }
            else{
                ans=Integer.parseInt(accDetails[3]);
            }
        }
        else{
            if(numberofInquiry>=5){
                System.out.println("You Have Exceeded the Enquiry Limit for Current Account (which is 5)");
            }
            else{
                ans=Integer.parseInt(accDetails[3]);
            }
        }

        System.out.println("Your Account Balance is : "+ans);

    }

    public int getBalanceAtAccountOpening(String AccountNumber, ArrayList<String[]> transacList) {
        return Integer.parseInt(transacList.get(0)[2]);
    }

    public int getBalanceAtMonthStarting(String AccountNumber,ArrayList<String[]> transacList) throws ParseException {
        LocalDate currentDate = LocalDate.now();
        String currYear =Integer.toString(currentDate.getYear()) ;
        String currMonth = Integer.toString(currentDate.getMonthValue());
        if(currMonth.length()==1)
            currMonth="0"+currMonth;

        //dateCompare is Date object variable which is storing the value -"02-mm-yyyy 00:00:00"
        Date dateCompare=DateHandle.convert("02-"+currMonth+"-"+currYear+" 00:00:00");

        //now we need to find any date which is lower than dateCompare
        int net=0;

        int sizeOfTransac=transacList.size(); //default value is the value with which account was open.
        for(int i=sizeOfTransac-1;i>=0;i--)
        {
            String dateAndTime= transacList.get(i)[3]+" "+transacList.get(i)[4];
            Date date=DateHandle.convert(dateAndTime);
            if(date.compareTo(dateCompare)>=0){
                if(transacList.get(i)[1].equals("Deposit"))
                    net+=Integer.parseInt(transacList.get(i)[2]);
                else
                    net-=Integer.parseInt(transacList.get(i)[2]);

            }
        }

        int currAmt = Integer.parseInt(Dashboard.Acc.get(Integer.parseInt(AccountNumber))[3]);
        int ans=currAmt-net;
        //System.out.println("The Balance on 1st of this Month was :" +ans);

        return ans;

    }

    public int getNumberOfTransactionOnSameDay(String AccountNumber,ArrayList<String[]> transacList){

        String dateToday=DateHandle.findDate();
        int count=0;

        int sizeOfTransac=transacList.size(); //default value is the value with which account was open.
        for(int i=sizeOfTransac-1;i>=0;i--)
        {
            if(transacList.get(i)[3].equals(dateToday)&&(transacList.get(i)[1].equals("Withdraw")||transacList.get(i)[1].equals("Deposit")))
                count++;
        }
        return count;
    }


    public int getNumberOfInquiry(String AccountNumber,ArrayList<String[]> transacList){
        LocalDate currentDate = LocalDate.now();
        String currYear =Integer.toString(currentDate.getYear()) ;
        String currMonth = Integer.toString(currentDate.getMonthValue());
        String currDay = Integer.toString(currentDate.getDayOfMonth());
        if(currMonth.length()==1)
            currMonth="0"+currMonth;
        if(currDay.length()==1)
            currDay="0"+currDay; //converts 6 to 06 and so on.

        String dateToday=currDay+"-"+currMonth+"-"+currYear;
        int count=0;

        int sizeOfTransac=transacList.size(); //default value is the value with which account was open.
        for(int i=sizeOfTransac-1;i>=0;i--)
        {
            if(transacList.get(i)[3].equals(dateToday)&&transacList.get(i)[1].equals("CheckBalance"))
                count++;
        }
        return count;
    }

    public void print(String[] str)
    {
        int len=2+str.length;
        for(int i=0;i<str.length;i++)
            len+=str[i].length();

        for(int i=0;i<len;i++)
            System.out.print("'");
        System.out.println();
        System.out.print(" ");
        for(int i=0;i<str.length;i++)
        {
            System.out.print(str[i]+" ");
        }
        System.out.print(" ");
        System.out.println();
        for(int i=0;i<len;i++)
            System.out.print("'");

    }
}

class UserComparator implements Comparator<String[]> {
    @Override
    public int compare(String[] s, String[] s2) {
        //return s[]
        Date date1,date2;
        try {
             date1=DateHandle.convert(s[3]+" "+s[4]);
             date2=DateHandle.convert(s2[3]+" "+s2[4]);
             return date1.compareTo(date2);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}

class DateHandle{
    public static Date convert(String dateAndTime) throws ParseException {
        //String dateString = "26-06-2023 14:30:00";
        String pattern = "dd-MM-yyyy HH:mm:ss";

        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date convertedDate = dateFormat.parse(dateAndTime);

        return convertedDate;
    }
    public static String findDate(){
        LocalDate currentDate = LocalDate.now();
        String currYear =Integer.toString(currentDate.getYear()) ;
        String currMonth = Integer.toString(currentDate.getMonthValue());
        String currDay = Integer.toString(currentDate.getDayOfMonth());
        if(currMonth.length()==1)
            currMonth="0"+currMonth;
        if(currDay.length()==1)
            currDay="0"+currDay; //converts 6 to 06 and so on.

        String dateToday=currDay+"-"+currMonth+"-"+currYear;
        return dateToday;
    }

    public static String findTime(){
        LocalTime currentTime = LocalTime.now();

        String currHour =Integer.toString(currentTime.getHour());
        String currMin = Integer.toString(currentTime.getMinute());
        String currSec = Integer.toString(currentTime.getSecond());

//        //converting Time from UTC to IST
//        if(currentTime.getMinute()+30<60){
//            currMin= Integer.toString(currentTime.getMinute()+30);
//            currHour= Integer.toString((currentTime.getHour()+5)%24);
//        }
//        else{
//            currMin= Integer.toString((currentTime.getMinute()+30)%60);
//            currHour= Integer.toString((currentTime.getHour()+6)%24);
//        }//conversion of Time to IST is done

        if(currHour.length()==1)
            currHour="0"+currHour;
        if(currMin.length()==1)
            currMin="0"+currMin;
        if(currSec.length()==1)
            currSec="0"+currSec;

        String finalTimeIST=currHour+":"+currMin+":"+currSec;
        //System.out.println(finalTimeIST);
        return finalTimeIST;
    }
}

