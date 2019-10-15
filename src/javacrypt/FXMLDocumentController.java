/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javacrypt;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.codec.binary.Base64;
import util.CryptoUtil;
import util.Util;

/**
 *
 * @author Italo Melo
 */
public class FXMLDocumentController implements Initializable {

    private File file;

    private File fileSelecionada;

    private FileOutputStream fileOutputStream;

    @FXML
    private Button btnCriptografar;

    @FXML
    private Button btnDescriptografar;

    @FXML
    private TextField tilLocalArquivo;

    @FXML
    private TextField tilLocalDestino;

    @FXML
    private TextField tilChave;

    @FXML
    void onLocalArquivo(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Selecione um arquivo para criptografar ou descriptografar");
        fileSelecionada = fileChooser.showOpenDialog(new Stage());

        if (fileSelecionada != null) {
            tilLocalArquivo.setText(fileSelecionada.getAbsolutePath());
            System.out.println(fileSelecionada.getAbsolutePath());
        }

    }

    @FXML
    void onLocalDestino(ActionEvent event) {

        DirectoryChooser fileChooser = new DirectoryChooser();

        fileChooser.setTitle("Selecione um arquivo para criptografar ou descriptografar");
        File path = fileChooser.showDialog(new Stage());

        if (path != null) {
            tilLocalDestino.setText(path.getAbsolutePath() + "\\");
        }
    }

    @FXML
    void onCriptografar(ActionEvent event) {
        //criptografar4
        System.out.println(tilLocalArquivo.getText());
        file = new File(tilLocalArquivo.getText());

        if (tilChave.getText().isEmpty() || tilLocalArquivo.getText().isEmpty() || tilLocalDestino.getText().isEmpty()) {

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Atenção");
            alert.setHeaderText("Campos inválidos");
            alert.setContentText("Algum campo está faltando.");
            alert.showAndWait();

        } else {

            try {
                String base64File = Util.encoder(file.getAbsolutePath());
                String filenameMd5 = Util.md5(file.getName());
                System.out.println("Nome do arquivo :" + file.getName());
                System.out.println("Nome do arquivo em md5 :" + filenameMd5);
                System.out.println("Base 64 do arquivo :" + base64File);

                String nameCrypt = CryptoUtil.encrypt(file.getName(), tilChave.getText());

                File fileCrypted = new File(tilLocalDestino.getText() + nameCrypt);

                fileOutputStream = new FileOutputStream(fileCrypted);
                fileOutputStream.write(Base64.decodeBase64(CryptoUtil.encrypt(base64File, tilChave.getText())));
                fileOutputStream.flush();
                fileOutputStream.close();

            } catch (Exception e) {

                try {
                    //fileOutputStream.close();
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Atenção");
                    alert.setHeaderText("Arquivo inválido / Chave inválida");
                    alert.setContentText("Verifique o caminho dos arquivos. \\ A cheve precisa ser no tamanho padrão AES.");
                    alert.showAndWait();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
            }
        }
    }

    @FXML
    void onDescriptografar(ActionEvent event) {
        //decriptografar
        System.out.println(tilLocalArquivo.getText());
        File file = new File(tilLocalArquivo.getText());
        String base64File = Util.encoder(file.getAbsolutePath());

        if (tilChave.getText().isEmpty() || tilLocalArquivo.getText().isEmpty() || tilLocalDestino.getText().isEmpty()) {

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Atenção");
            alert.setHeaderText("Campos inválidos");
            alert.setContentText("Algum campo está faltando.");
            alert.showAndWait();

        } else {
            try {
                File fileDecrypted = new File(tilLocalDestino.getText() + CryptoUtil.decrypt(file.getName(), tilChave.getText()));
                fileOutputStream = new FileOutputStream(fileDecrypted);
                fileOutputStream.write(Base64.decodeBase64(CryptoUtil.decrypt(base64File, tilChave.getText())));
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (Exception e) {
                try {
                    //fileOutputStream.close();
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Atenção");
                    alert.setHeaderText("Arquivo inválido / Chave inválida");
                    alert.setContentText("Verifique o caminho dos arquivos. \\ A cheve precisa ser no tamanho padrão AES.");
                    alert.showAndWait();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
            }

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        tilLocalArquivo.setText("C:\\");
        tilLocalDestino.setText("C:\\");
    }

}
