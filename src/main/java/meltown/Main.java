package meltown;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static JDA jda;
    private static final String PORT = System.getenv("PORT");
    static Connection connection;
    private static final String DB_URL = "jdbc:sqlite:database.db";
    private static final String DB_DRIVER = "org.sqlite.JDBC";
    public static void main(String[] args) {
        DBconnect();
        SetupDBcreate();
        EconomyDBCreate();
        ChatDBCreate();
        try {
            jda = new JDABuilder().setToken("токен").setActivity(Activity.listening("#help | Developed by qqsky")).setStatus(OnlineStatus.DO_NOT_DISTURB).build();
        }
        catch(LoginException e){
            e.printStackTrace();
        }
        jda.addEventListener(new Utils());
        jda.addEventListener(new Moderation());
        jda.addEventListener(new Help());
        jda.addEventListener(new MeltownEconomy());
    }
    public static void SetupDBcreate(){
        try{
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(DB_URL);
            String query = "CREATE TABLE SetupDB(" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "GuildName TEXT," +
                    "muteRoleId TEXT)";
            Statement st = connection.createStatement();
            st.executeUpdate(query);
            System.out.println("Таблица успешно создана");
            connection.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void DBconnect(){
        try{
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("Подключение удалось");
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    public static void SocketConnect(){
        try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(PORT))) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void EconomyDBCreate(){
        try {
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(DB_URL);
            String query = "CREATE TABLE EconomyDb(" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "USERID INTEGER, " +
                    "BAL INTEGER NULL, " +
                    "MELCOIN INTEGER NULL, " +
                    "LASTTRANSUM, " +
                    "LASTTRANSDATE)";
            Statement st = connection.createStatement();
            st.executeUpdate(query);
            System.out.println("Экономика мелдауна......");
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    public static void ChatDBCreate(){
        try {
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(DB_URL);
            String query = "CREATE TABLE ChatDB(" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "GUILDID TEXT," +
                    "HOST TEXT," +
                    "CODE TEXT," +
                    "CHANNELID TEXT," +
                    "CHANNELPARCIPANTS TEXT NULL)";
            Statement st = connection.createStatement();
            st.executeUpdate(query);
            System.out.println("ладно");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    public static void ChatDBDelete()  {
        try {
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(DB_URL);
            String query = "DROP TABLE ChatDB";
            Statement st = connection.createStatement();
            st.executeUpdate(query);
            System.out.println("удалено");
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }
    public static void EconomyDBDelete(){
        try {
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(DB_URL);
            String query = "DROP TABLE EconomyDb";
            Statement st = connection.createStatement();
            st.executeUpdate(query);
            System.out.println("удалено");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
