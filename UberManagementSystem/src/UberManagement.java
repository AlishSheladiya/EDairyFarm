import java.io.*;
import java.sql.*;
import java.util.*;

public class UberManagement{
   public static void main(String[] args)throws Exception {
        Scanner sc = new Scanner(System.in);
        String url="jdbc:mysql://localhost:3306/alish";
        String user="root";
        String pass="";
        Connection con=DriverManager.getConnection(url,user,pass);
        if(con!=null){
            System.out.println("Connection success");
        }
        else{
            System.out.println("Connection failed");
        }
        Uber u =new Uber();
        System.out.println("------WELCOME TO CAR BOOKING SYSTEM --------");
        while (true) {
            System.out.println("1. Register ");
            System.out.println("2. Login ");
            System.out.println("3. Display All Users ");
            System.out.println("4. EXIT!!! ");
            int choice = sc.nextInt();
            switch(choice)
            {
                case 1 :
                {
                    u.add(con);
                    break;
                }
                case 2 :
                {
                    u.login(con);
                    break;
                }
                case 3 :
                {
                    u.display(con);
                    break;
                }
                case 4 :
                {
                    System.exit(0);
                }
                default :
                {
                    System.out.println("Enter Valid Choice");
                    break;
                }
            }
        }
   }
}
class User 
{
    int id;
    String name;
    String mobile;
    public User(int id, String name, String mobile) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
    }
    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", mobile=" + mobile + "]";
    }
}
class Uber{
    Scanner sc=new Scanner(System.in);
    Payment payment = new Payment();

    void add(Connection con)throws Exception{
        System.out.println(" Enter your details for registrastion");
        System.out.println("Enter id ");
        int id=sc.nextInt();
        System.out.println("Enter Name");
        String name=sc.next();
        System.out.println("Enter Password");
        String pass=sc.next();
        System.out.println("Enter Mobile Number");
        String mn=sc.next();
        File f = new File("src/"+mn+".txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
        int OTP =(int)(Math.random()*10000);
        bw.write("Your OTP For Mobile No. "+mn+" is "+OTP);
        bw.close();
        System.out.println("Enter OTP ");
        int otp1=sc.nextInt();
        if(OTP==otp1)
        {
            String sql="insert into user(id,name,password,mobile) values (?,?,?,?)";
            PreparedStatement pst=con.prepareStatement(sql);
            pst.setInt(1, id);
            pst.setString(2, name);
            pst.setString(3, pass);
            pst.setString(4, mn);
            int r=pst.executeUpdate();
            if (r > 0) {
                System.out.println("Registeration success");
            } else {
                System.out.println("Insertion failed..!");
            }
            pst.close();
        }
        else
        {
            System.out.println("Registeration Failed..!");
        }
    }

    void login(Connection con)throws Exception{
        System.out.println("Enter your details to login");
        System.out.println("Enter your id");
        int id=sc.nextInt();
        System.out.println("Enter password");
        String pass=sc.next();
        String sql = "select id,password from user where id="+id;
        PreparedStatement pst = con.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        boolean b = false;
        while(rs.next())
        {
            if(id==rs.getInt(1))
            {
                if(pass.equals(rs.getString(2)))
                {
                    b=true;
                    System.out.println("----- Login Success -----");
                   options(con);
                }
            }
        }
        if(!b)
        {
            System.out.println("Invalid ID or Password");
        }
    }

    void book(Connection con)throws Exception
    {
        System.out.println("Available Location :");
        System.out.println("Sanand || Isckon || Shivranjni || Navrangpura || Bopal || Bapunagar || Nikol || Naroda ");
        System.out.println("Enter Your Location :-");
        String area=sc.next().toLowerCase();
        String sql="select * from Location where Source=?";
        PreparedStatement pst=con.prepareStatement(sql);
        pst.setString(1, area);
        ResultSet rs = pst.executeQuery();
        System.out.println("------ Available Destination From "+area+"are as follows ------");
        boolean b = false;
        while(rs.next())
        {
            b=true;
            System.out.println("Route ID : "+rs.getInt(4)+"\tDestinations : "+rs.getString(2)+"\tPrice : "+rs.getString(3));
        }
        if(b)
        {
            System.out.println("Enter Route ID From Above List : ");
            int r_id=sc.nextInt();
            String sql1="select * from Location where Source=  ? and route_id = "+r_id;
            PreparedStatement pst1=con.prepareStatement(sql1);
            pst1.setString(1, area);
            ResultSet rs1 = pst1.executeQuery();
            boolean c = false;
            while(rs1.next())
            {
                c=true;
                int y=rs1.getInt(4);
                System.out.println("Route ID : "+rs1.getInt(4)+"\tSource : "+rs1.getString(1)+"\tDestinations : "+rs1.getString(2)+"\tPrice : "+rs1.getString(3));
                System.out.println("ReEnter Route ID To Confirm This Booking: ");
                int x = sc.nextInt();
                if(x==y)
                {
                    System.out.println("Congratulations Booking is confirmed");
                    payment.showPaymentOptions();  // Display payment options
                    payment.processPayment();      // Process payment
                }
                else
                {
                    System.out.println("  Redirecting to User Page ");
                }
            }
            if(!c){
                System.out.println("Invalid Route Id");
            }
        }
        else{
            System.out.println("No Destination Found");
        }
    }

    void detailBySource(Connection con)throws Exception{
        System.out.println("Available Location :");
        System.out.println("Sanand || Isckon || Shivranjni || Navrangpura || Bopal || Bapunagar || Nikol || Naroda ");
        System.out.println("Enter Your Location : ");
        String area=sc.next().toLowerCase();
        String sql="select * from Location where Source=?";
        PreparedStatement pst=con.prepareStatement(sql);
        pst.setString(1, area);
        ResultSet rs = pst.executeQuery();
        System.out.println("------ Available Destination From "+area+" ------");
        while(rs.next())
        {
            System.out.println("Route ID : "+rs.getInt(4)+"\tDestinations : "+rs.getString(2)+"\tPrice : "+rs.getString(3));
        }
    }

    void detailByDestination(Connection con)throws Exception{
        System.out.println("Available Destionation :");
        System.out.println("Sanand || Isckon || Shivranjni || Navrangpura || Bopal || Bapunagar || Nikol || Naroda ");
        System.out.println("Enter Your destionation : ");
        String area=sc.next().toLowerCase();
        String sql="select * from Location where Destination=?";
        PreparedStatement pst=con.prepareStatement(sql);
        pst.setString(1, area);
        ResultSet rs = pst.executeQuery();
        System.out.println("------ Available Places From "+area+" ------");
        while(rs.next())
        {
            
            System.out.println("Route ID : "+rs.getInt(4)+"\tSource : "+rs.getString(1)+"\tPrice : "+rs.getString(3));
        }
    }

    void display(Connection con) throws Exception
    {
        String sql = "select * from user";
        PreparedStatement pst = con.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        LinkedList1 ll = new LinkedList1();
        while (rs.next()) {
            User u = new User(rs.getInt(1), rs.getString(2), rs.getString(4));
            ll.add(u);
        }
        ll.display();
    }
    void options(Connection con)throws Exception
    {
        boolean b=true;
        while(b){
            System.out.println("----- WELCOME TO USER PAGE ------");
        System.out.println("Enter 1 to book ticket");
        System.out.println("Enter 2 to search rides by your location");
        System.out.println("Enter 3 to search rides by destination");
        System.out.println("Enter 4 to exit");
        int n=sc.nextInt();
        switch (n) {
            case 1:
            {
                book(con);
                break;
            }

                case 2:
                {
                detailBySource(con);
                break;
                }

                case 3:
                {
                detailByDestination(con);           
                break;
                }

                case 4:b=false;
                break;

        
            default:System.out.println("Enter valid option");
                break;
        } 
    }
    }
}
class Payment {
    LinkedList<String> paymentOptions;

    public Payment() {
        paymentOptions = new LinkedList<>();
        paymentOptions.add("Credit Card");
        paymentOptions.add("Debit Card");
        paymentOptions.add("Net Banking");
        paymentOptions.add("UPI");
        paymentOptions.add("Wallet");
    }

    void showPaymentOptions() {
        System.out.println("---- Payment Options ----");
        for (int i = 0; i < paymentOptions.size(); i++) {
            System.out.println((i + 1) + ". " + paymentOptions.get(i));
        }
    }
    void processPayment() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Select Payment Method by Number: ");
        int choice = sc.nextInt();
        if (choice > 0 && choice <= paymentOptions.size()) {
            System.out.println("Processing payment through " + paymentOptions.get(choice - 1));
            System.out.println("Payment Successful!");
        } else {
            System.out.println("Invalid Payment Method Selected!");
        }
    }
}
class LinkedList1
{
    class Node
    {
        User m;
        Node next;
        Node (User m)
        {
            this.m=m;
            next=null;
        }
    }    
    Node first=null;
    void add(User m)
    {
        Node n = new Node(m);
        if(first==null)
        {
            first=n;
        }
        else
        {
            n.next = first;
            first = n;
        }
    }
    void display()
    {
        Node temp = first;
        while (temp != null) {
            System.out.println(temp.m);
            System.out.println("===========================================");
            temp = temp.next;
        }
    }
}