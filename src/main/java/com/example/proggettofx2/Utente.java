package com.example.proggettofx2;


import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Utente {

    private static Utente istanza= null;

    private final int idutente;
    private String Nome;
    private String Cognome;
    private String Nazionalita;
    private String Email;
    private String Password;


   private Utente(String N,String C,String Na,String E,String P,int id)
   {
       Nome=N;
       Cognome=C;
       Nazionalita=Na;
       Email=E;
       Password=P;
       idutente=id;
   }


    public static Utente getUtente(String N,String C,String Na,String E,String P,int id)
    {
        if(istanza==null) {istanza=new Utente(N,C,Na,E,P,id);}
        return istanza;
    }

    public  void setdefault() {istanza=null;}



    public void Modifica(String N,String C,String Na,String E,String P)throws SQLException
    {
        Nome=N;
        Cognome=C;
        Nazionalita=Na;
        Email=E;
        Password=P;

        MainController Main= MainController.getInstance();
        PreparedStatement pst= Main.DoPrepared("update utente set nome= ?,cognome= ?,email= ?,nazionalit√†= ?,password= ? where id_utente= ?");

        pst.setString(1,Nome);
        pst.setString(2,Cognome);
        pst.setString(3,Email);
        pst.setString(4,Nazionalita);
        pst.setString(5,Password);
        pst.setInt(6,idutente);

        pst.execute();
        pst.close();

        Main.Closeall();

    }


    public static  Utente getUtente(){return istanza;}
    public String getNome() {return Nome;}
    public String getCognome() {return Cognome;}
    public String getNazionalita() {return Nazionalita;}
    public String getEmail() {return Email;}
    public String getPassword() {return Password;}
    public int getIdutente(){return idutente;}









}
