/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tecnoweb;

import java.net.*;
import java.io.*;
/**
 *
 * @author Estudiante
 */
public class ClienteSMTP {
    
    @SuppressWarnings("empty-statement")
    public static void main(String[] args){
        String servidor = "mail.tecnoweb.org.bo";
        String user_receptor = "grupo06sa@tecnoweb.org.bo";
        String user_emisor="jsoliz064@gmail.com";
        String line;
        String comando;
        String salt = System.getProperty("line.separator");
        comando = "";
        int puerto = 25;
         try{
            try ( //se establece conexion abriendo un socket especificando el servidor y puerto SMTP
                    Socket socket = new Socket(servidor,puerto); BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream())); DataOutputStream salida = new DataOutputStream (socket.getOutputStream())) {
                // Escribimos datos en el canal de salida establecido con el puerto del protocolo SMTP del servidor
                if( socket != null && entrada != null && salida != null )
                {
                    System.out.println("S : "+entrada.readLine());
                    
                    comando="HELO tecnoweb.org.bo \r\n";
                    salida.writeBytes( comando );
                    System.out.print("C : "+comando);
                    System.out.println("S : "+getMultiline(entrada));
                    comando = "";
                    comando="mail from: jsoliz064@gmail.com\r\n";
                    salida.writeBytes( comando );
                    System.out.print("C : "+comando);
                    System.out.println("S : "+entrada.readLine());
                    comando = "";
                    comando="rcpt to: "+user_receptor+"\r\n";
                    System.out.print("C : "+comando);
                    salida.writeBytes( comando );
                    System.out.println("S : "+entrada.readLine());
                    comando = "";
                    comando="DATA\r\n";
                    System.out.print("C : "+comando);
                    salida.writeBytes( comando );
                    System.out.println("S : "+getMultiline(entrada));
                    comando = "";
                    comando="Subject: DEMO X \r\n"+"select from * users \r\n"+"el envio de mensajes \r\n"+".\r\n";
                    System.out.print("C : "+comando);
                    salida.writeBytes( comando );
                    System.out.println("S : "+entrada.readLine());
                    comando = "";
                    comando="QUIT\r\n";
                    System.out.print("C : "+comando);
                    salida.writeBytes( comando );
                    System.out.println("S : "+entrada.readLine());
                }
                // Cerramos los flujos de salida y de entrada y el socket cliente
            }
        }catch(UnknownHostException e){
            System.out.println(" S : No se pudo conectar con el servidor indicado");
        }catch (IOException e){
        }
    }

//Permite Leer multiples lineas del Protocolo SMTP

   static protected String getMultiline(BufferedReader in) throws IOException{
        String lines = "";
        while (true){
            String line = in.readLine();
            if (line == null){
               // Server closed connection
               throw new IOException(" S : Server unawares closed the connection.");
            }
            if (line.charAt(3)==' '){
                lines=lines+"\n"+line;
                // No more lines in the server response
                break;
            }           
            // Add read line to the list of lines
            lines=lines+"\n"+line;
        }       
        return lines;
    }
}
