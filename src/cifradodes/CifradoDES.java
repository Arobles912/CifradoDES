/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cifradodes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 *
 * @author Ángel
 */
public class CifradoDES {

    Cipher ecipher;
    Cipher dcipher;

    CifradoDES(SecretKey key) throws Exception {
        ecipher = Cipher.getInstance("DES");
        dcipher = Cipher.getInstance("DES");
        ecipher.init(Cipher.ENCRYPT_MODE, key);
        dcipher.init(Cipher.DECRYPT_MODE, key);
    }

    public String encriptar(String str) throws Exception {
        // Encode the string into bytes using utf-8
        byte[] utf8 = str.getBytes("UTF8");

        // Encrypt
        byte[] enc = ecipher.doFinal(utf8);

        // Encode bytes to base64 to get a string
        return Base64.getEncoder().encodeToString(enc);

    }

    public String desencriptar(String str) throws Exception {
        // Decode base64 to get bytes
        byte[] dec = Base64.getDecoder().decode(str);

        byte[] utf8 = dcipher.doFinal(dec);

        // Decode using utf-8
        return new String(utf8, "UTF8");
    }

    public static void creacionArchivoEncriptado(File archivo, File archivoEncriptado, CifradoDES encrypter) {
        try {
            FileReader fr = new FileReader(archivo);
            BufferedReader br = new BufferedReader(fr);
            FileReader fr2 = new FileReader(archivoEncriptado);

            FileWriter fw = new FileWriter(archivoEncriptado);
            BufferedWriter bw = new BufferedWriter(fw);
            String line = "";
            String textoEncriptado = "";
            while ((line = br.readLine()) != null) {
                System.out.println("Linea: " + line);
                textoEncriptado = encrypter.encriptar(line);
                bw.write(textoEncriptado);
                bw.newLine();
                System.out.println("Texto encriptado: " + textoEncriptado);
            }
            br.close();
            bw.close();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Introduce la ruta del archivo a encriptar:");
            final String ruta = sc.nextLine();
            final String rutaFormated = ruta.substring(0, ruta.length() - 4);
            final String ruta2 = rutaFormated + "Encriptado.txt";
            SecretKey key;
            key = KeyGenerator.getInstance("DES").generateKey();
            CifradoDES encrypter = new CifradoDES(key);
            File archivo = new File(ruta);
            if (!archivo.isFile()) {
                System.out.println("No se ha podido encontrar el archivo especificado.");
            } else {
                File archivoEncriptado = new File(ruta2);
                if (!archivoEncriptado.isFile()) {
                    archivoEncriptado.createNewFile();
                }
                creacionArchivoEncriptado(archivo, archivoEncriptado, encrypter);
                FileReader fr = new FileReader(archivoEncriptado);
                BufferedReader br = new BufferedReader(fr);
                String line2 = "";
                String desencriptado = "";
                while ((line2 = br.readLine()) != null) {
                    desencriptado = encrypter.desencriptar(line2);
                    System.out.println("Texto desencriptado: " + desencriptado);
                }
                br.close();
            }

        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
