/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tecnoweb;

/**
 *
 * @author daniy
 */
import java.io.*;
import java.net.*;

public class ClientePOP {
    
    public static void main(String[] args) {
        String servidor = "mail.tecnoweb.org.bo";
        String usuario = "grupo06sa";
        String contrasena= "grup006grup006";
        String comando = "";
        String linea = "";
        int puerto= 110;
        try {
            Socket socket= new Socket(servidor,puerto);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            DataOutputStream salida = new DataOutputStream(socket.getOutputStream());
            if (socket != null && entrada != null && salida != null){
                System.out.println("S : "+ entrada.readLine()+"r\n");
                
                comando = "USER "+ usuario + "\r\n";
                System.out.println("C: "+comando);
                salida.writeBytes(comando);
                System.out.println(" S: "+ entrada.readLine()+"\r\n");
                
                comando = "PASS "+ contrasena+ "\r\n";
                System.out.println("C: "+comando);
                salida.writeBytes(comando);
                System.out.println(" S: "+ entrada.readLine()+"\r\n");
                
                comando = "STAT\r\n";
                System.out.println("C: "+comando);
                salida.writeBytes(comando);
                System.out.println(" S: "+ entrada.readLine()+"\r\n");
                
                comando = "LIST \r\n";
                System.out.println("C: "+comando);
                salida.writeBytes(comando);
                //System.out.println(" S: "+ getMultiline(entrada)+"\r\n");
                
                int n=Messages(entrada);
                for (int i = 1; i <=n; i++) {
                    comando = "RETR " +i+"\r\n";
                    System.out.println("C: "+comando);
                    salida.writeBytes(comando);
                    String message=getMultiline(entrada);
                    if (isSubject(message)){
                        System.out.println(getFrom(message));
                        System.out.println(getSubject(message));
                        //System.out.println(" S: "+ message+"\r\n");
                    }
                }
                comando = "QUIT\r\n";
                System.out.println("C: "+comando);
                salida.writeBytes(comando);
                System.out.println(" S: "+ entrada.readLine()+"\r\n");
                
            }
            salida.close();
            entrada.close();
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.out.println("S: no se pudo conectar con el servidor indicado");
        }catch (IOException e){
            e.printStackTrace();
        }   
        
    }
    
    static protected String getMultiline(BufferedReader in) throws IOException{
        String lines="";
        int i=1;
        while (true){
            String line = in.readLine();
            if (line ==null ){
                throw new IOException("S: server unawares closed the connection");
            }
            if (line.equals(".")){
                break;
            }
            if ((line.length()>0) && (line.charAt(0)=='.')){
                line= line.substring(1);
            }
            lines=lines + "\n"+line;
            i++;
        }
        return lines;
    }
    static protected int Messages(BufferedReader in) throws IOException{
        String lines="";
        int i=1;
        while (true){
            String line = in.readLine();
            if (line ==null ){
                throw new IOException("S: server unawares closed the connection");
            }
            if (line.equals(".")){
                break;
            }
            if ((line.length()>0) && (line.charAt(0)=='.')){
                line= line.substring(1);
            }
            lines=lines + "\n"+line;
            i++;
        }
        return i-2;
    }
    static protected boolean isSubject(String message){
        String line=message.replaceAll("\n", " | "); ;
        line=line+' ';
        String pal="";
        for (int j = 0; j < line.length(); j++) {
            if (line.charAt(j)!=' ' && !pal.equalsIgnoreCase("subject")){
                pal=pal+line.charAt(j);
            }else if (pal.length()>0 && pal.equalsIgnoreCase("subject")){
                return true;
            }else{
                pal="";
            }
        }
        return false;
    }
    static protected String getFrom(String message){
        String line=message.replaceAll("\n", " | "); ;
        line=line+' ';
        String pal="";
        boolean b=false;
        for (int j = 0; j < line.length(); j++) {
            if (line.charAt(j)!=' ' && !pal.equalsIgnoreCase("from:")){
                pal=pal+line.charAt(j);
            }else if (pal.length()>0 && pal.equalsIgnoreCase("from:") && !b){
                b=true;
                pal="";
            }else if (pal.length()>0 && b){
                return pal;
            }else{
                b=false;
                pal="";
            }
        }
        return pal;
    }
    static protected String getSubject(String message){
        String line=message.replaceAll("\n", " "); ;
        System.out.println(line);
        line=line+' ';
        String pal="";
        String subject="";
        boolean b=false;
        for (int j = 0; j < line.length(); j++) {
            if (line.charAt(j)!=' ' && !b){
                pal=pal+line.charAt(j);
            }else if (pal.equalsIgnoreCase("subject:")){
                b=true;
                pal="";
            }else if (b){
                pal=pal+line.charAt(j);
            }else{
                pal="";
            }
        }
        return pal;
    }
}
