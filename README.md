# Bank-Management-System-in-JAVA
Made a bank Management Sytem in Core Java , which has certain transactional criterion that user must follow depending upon the type of account ie Current or Savings.
If users fails to fulfill the criteria then the program exits with an error message explaining the cause of transactional failure.

The program manages two csv files:

1.Account_data.csv
2.Transaction_data.csv

in account_data.csv we are saving user information and looks something like this :

![image](https://github.com/ritikagr061/Bank-Management-System-in-JAVA/assets/54122273/e5054915-d961-4fa2-9b80-27e6500e8db0)

whereas in Transaction_Data.csv , as the name suggests we are storing Transactional data of users.<br>
Our program mainly manages 3 types of transaction only, withdraw , deposit and BalanceCheck.

![image](https://github.com/ritikagr061/Bank-Management-System-in-JAVA/assets/54122273/94e0fd3b-936f-4865-9107-a162b19da032)

These are the functionality that we are providing to the user. Also there are certain criterias that needs to be followed as mentioned below :

1)     User can create/open account Saving or Current account with the providing Username, Age, Deposit Amount and Date of Deposit by reading from file input. A sample is provided in the project.
2)     After that user can do any of 3 operations(Deposit, CheckBalance, Withdraw) with different rules correspond to account type.
3)     For each transaction there should be an entry of Amount and Date
```
4)     Rules For Current Account Operations
    a) Daily transactions can’t be more than 5.
    b) Transaction of withdrawing the amount can’t be more than 80% of that month balance on 1st of that month
    c) Deposit amount of that day can’t be 10 times more than the deposit amount at account opening time.
    d) Check Balance can’t be done more than 5 times in a day
```

```
5)      Rules For Saving Account Operations
    a) Daily transactions can’t be more than 2.
    b) Transaction of withdrawing the  amount can’t be more than 40% of that month balance on 1st of that month
    c) Deposit amount of that day can’t be 5 times more than the deposit amount at account opening time.
    d) Check Balance can’t be done more than 7 times in a day. 
```

### If the user crosses his transactional limits then the program exits with an error message about why user is not allowed to do the particular transaction
