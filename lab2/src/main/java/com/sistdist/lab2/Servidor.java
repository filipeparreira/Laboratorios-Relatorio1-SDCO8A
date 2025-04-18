/**
 * Laboratorio 2 - Lista 1 de Sistemas Distribuidos
 *
 * Autores: Filipe Augusto e Mabylly Kauany Ultima atualizacao: 18/04/2025
 */


package com.sistdist.lab2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Servidor {

    private static Socket socket;
    private static ServerSocket server;

    private static DataInputStream entrada;
    private static DataOutputStream saida;

    private JSONObject mensagemRecebida;
    private JSONObject resposta;

    private ArrayList<String> db;

    private final int porta = 1025;

    public void iniciar(String caminho) {
        System.out.println("Servidor iniciado na porta: " + porta);
        try {
            // Inicializa o atributo do banco de dados
            db = getFortunes(caminho);

            // AINDA FALTA ESCREVER NO BANCO DE DADOS 
            // Criar porta de recepcao
            server = new ServerSocket(porta);
            socket = server.accept();  //Processo fica bloqueado, ah espera de conexoes

            // Criar os fluxos de entrada e saida
            entrada = new DataInputStream(socket.getInputStream());
            saida = new DataOutputStream(socket.getOutputStream());

            String result = "";
            mensagemRecebida = new JSONObject(entrada.readUTF());
            System.out.println("Entrada Recebida: " + mensagemRecebida.toString());

            // Se o method for read
            switch (mensagemRecebida.getString("method")) {
                case "read" -> {
                    // PEGAR UMA FORTUNA ALEATORIA
                    Random index = new Random();
                    result = db.get(index.nextInt(db.size())) + "\n";
                }
                case "write" -> {
                    String fortune = mensagemRecebida.getString("args");
                    if (fortune.endsWith("\n")) {
                        result = fortune;
                        // Escreve fortune no banco
                        writeDB(fortune, caminho);
                    } else {
                        result = "false\n";
                    }
                }
                default ->
                    result = "false\n";
            }
            
            // Resposta
            resposta = new JSONObject();
            resposta.put("result", result);
            
            // Envio dos dados (resultado)
            saida.writeUTF(resposta.toString());
            socket.close();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> getFortunes(String caminho) throws FileNotFoundException {
        // Lendo a base de dados
        Scanner reader = new Scanner(new FileInputStream(caminho), StandardCharsets.UTF_8);
        ArrayList<String> fortunes = new ArrayList<>();
        String fortune = "";
        String palavra = "";

        while (reader.hasNextLine()) {
            palavra = reader.nextLine();
            if ("%".equals(palavra.trim())) {
                fortunes.add(fortune);
                fortune = "";
            } else {
                fortune += palavra;
            }
        }

        fortunes.removeLast();
        return fortunes;
    }
    
    private void writeDB(String fortune,String caminho){
        try {
            ArrayList<String> conteudoDB = (ArrayList<String>) Files.readAllLines(Paths.get(caminho), Charset.forName("ISO-8859-1"));
            conteudoDB.add(0, fortune + "%");
            Files.write(Paths.get(caminho), conteudoDB, Charset.forName("ISO-8859-1"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length == 0){
            System.out.println("Caminho para a base de dados não informado.");
            return;
        } else {
            new Servidor().iniciar(args[0]);
        }
        

    }

}
