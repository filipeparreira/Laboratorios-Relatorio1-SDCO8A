/**
 * Laboratorio 2 - Lista 1 de Sistemas Distribuidos
 *
 * Autores: Filipe Augusto e Mabylly Kauany Ultima atualizacao: 18/04/2025
 */


package com.sistdist.lab2;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import org.json.JSONException;
import org.json.JSONObject;

public class Cliente {

    private static Socket socket;
    private static DataInputStream entrada;
    private static DataOutputStream saida;
    private static JSONObject mensagem;
    private static JSONObject resposta;

    private final int porta = 1025;

    public void iniciar() {
        System.out.println("Cliente iniciado na porta: " + porta);

        try {

            socket = new Socket("127.0.0.1", porta);

            entrada = new DataInputStream(socket.getInputStream());
            saida = new DataOutputStream(socket.getOutputStream());

            mensagem = new JSONObject();

            String method = "";
            String args = "\n";

            //Recebe do usuario algum valor
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                // Seleção de tipo de envio:
                System.out.print("\nTipos de envio:\n[1] - Leitura\n[2] - Escrita\nSelecione um tipo: ");
                int escolha = Integer.parseInt(br.readLine());

                if (escolha == 1) {
                    method = "read";
                    break;
                } else if (escolha == 2) {
                    method = "write";
                    System.out.print("Digite a mensagem de resposta: ");
                    // Formatando a entrada
                    args = br.readLine().trim() + '\n';
                    
                    // Args teste erro '\n'
                    //args = br.readLine().trim();
                    break;
                } else {
                    System.out.println("Opção incorreta!! Digite novemente.");
                }
            }
            System.out.println("Enviando mensagem...");

            //Montagem da saida json 
            mensagem.put("method", method);
            mensagem.put("args", args);

            saida.writeUTF(mensagem.toString());

            //O valor eh enviado ao servidor
            //saida.writeInt(valor);
            //Recebe-se o resultado do servidor
            resposta = new JSONObject(entrada.readUTF());

            //Mostra o resultado na tela
            System.out.println("Retorno: " + resposta.toString());

            socket.close();

        } catch (IOException | NumberFormatException | JSONException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Cliente().iniciar();
    }

}
