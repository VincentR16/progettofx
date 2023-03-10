package com.example.proggettofx2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;


import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class CollezioniController implements Initializable
{
    @FXML
    public ScrollPane pannel;

    @FXML
    private ComboBox<String> combobox;

    @FXML
    void BAggiungifoto(@SuppressWarnings("UnusedParameters")ActionEvent event) throws IOException
    {
        MainController.getInstance().getStage().close();
        MainController.getInstance().CreateStage("Aggiungifotopage.fxml");
        MainController.getInstance().getStage().setWidth(920);
        MainController.getInstance().getStage().setHeight(620);
    }


    @FXML
    void BProfile(@SuppressWarnings("UnusedParameters")ActionEvent event)throws IOException
    {
        MainController.getInstance().getStage().close();
        MainController.getInstance().CreateStage("Profile-page.fxml");
    }


    @FXML
    void Bexit(@SuppressWarnings("UnusedParameters")ActionEvent event)throws IOException
    {
        MainController.getInstance().getStage().close();

        MainController C =MainController.getInstance();

        C.CreateStage("Firstpage.fxml");
        C.getStage().setHeight(450);
        C.getStage().setWidth(655);
        C.getStage().setResizable(false);

        Utente.getUtente().setdefault();

    }

    @FXML
    void Bvideo(@SuppressWarnings("UnusedParameters")ActionEvent event)throws IOException
    {
        MainController.getInstance().getStage().close();
        MainController.getInstance().CreateStage("Videopage.fxml");
    }
    @FXML
    void BCestino(@SuppressWarnings("UnusedParameters")ActionEvent event) throws IOException
    {
        MainController.getInstance().getStage().close();
        MainController.getInstance().CreateStage("Trashpage.fxml");
    }


    @FXML
    void BbackToHome(@SuppressWarnings("UnusedParameters")ActionEvent event) throws IOException
    {
        MainController.getInstance().getStage().close();
        MainController.getInstance().CreateStage("HOME_page.fxml");
    }

    @FXML
    void MouseEntered(MouseEvent event)
    {
        javafx.scene.control.Button button=(javafx.scene.control.Button) (event.getSource());
        button.setStyle("-fx-background-color:  #0C1538");
    }

    @FXML
    void MouseExited(MouseEvent event)
    {
        javafx.scene.control.Button button=(Button) (event.getSource());
        button.setStyle("-fx-background-color:  #183669 ");
    }
    @FXML
    void BnewCollection(@SuppressWarnings("UnusedParameters")ActionEvent event) throws IOException
    {
        MainController.getInstance().getStage().close();
        MainController.getInstance().CreateStage("Creacollezionepage.fxml");
    }
    @FXML
    void BaddCollection(@SuppressWarnings("UnusedParameters")ActionEvent event)throws IOException
    {
        if(combobox.getSelectionModel().getSelectedItem()==null)
        {
            Alert alert= new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Scegliere una collezione");
            alert.setTitle("IMPORTANTE");

            alert.show();
        }else
        {
            MainController.getInstance().getStage().close();
            MainController.getInstance().CreateStage("Add2Collectionpage.fxml");
        }
    }

    @FXML
    void BaddusersCollection(@SuppressWarnings("UnusedParameters")ActionEvent event)throws IOException
    {
        if(combobox.getSelectionModel().getSelectedItem()==null)
        {
            Alert alert= new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Scegliere una collezione");
            alert.setTitle("IMPORTANTE");

            alert.show();
        }else
        {
            MainController.getInstance().getStage().close();
            MainController.getInstance().CreateStage("UtenteCollezione.fxml");
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MainController main = MainController.getInstance();

        try {

            PreparedStatement pst = main.DoPrepared("select distinct nome from collezione as c, utente_possiede_collezione as u where u.id_utente=? and c.personale=0 and c.id_collezione= u.id_collezione");
            pst.setInt(1,Utente.getUtente().getIdutente());

            ResultSet rs1 = pst.executeQuery();

            while (rs1.next())
            {
                combobox.getItems().add(rs1.getString("nome"));
            }

            main.Closeall();
            rs1.close();

            combobox.setOnAction((ActionEvent er)->
            {
                main.setScelta(combobox.getSelectionModel().getSelectedItem());

                GridPane gridPane;
                try {

                    CallableStatement cs1 = main.Callprocedure("{?= call recupera_id_collezione(?)}");
                    cs1.registerOutParameter(1, Types.INTEGER);

                    cs1.setString(2, combobox.getSelectionModel().getSelectedItem());

                    cs1.execute();


                    PreparedStatement st1 = main.DoPrepared("select * from collezione_condivisa(?)");

                    st1.setString(1,combobox.getSelectionModel().getSelectedItem());
                    ResultSet rs= st1.executeQuery();



                    gridPane = new GridPane();
                    gridPane.setHgap(10);
                    gridPane.setVgap(10);


                    int i = 0;
                    int j = 0;

                    while (rs.next()) {
                        ImageView imageView = main.setImageview(rs.getBytes("val_foto"), rs.getInt("id_foto"));

                        gridPane.add(imageView, j, i);
                        // faccio un semplice ciclo per impostare la posizione delle immagevie nella griglia
                        j++;                                                                                                                    // rispetto alle matrici qui si mette prima la colonna e poi la riga
                        if (j > 4) {
                            j = 0;
                            i++;
                        }


                        imageView.setOnMouseClicked((MouseEvent e) ->                                                                           // creo un semplice listner per poter andare a eliminare le foto ogni qual volta vengano cliccate
                        {                                                                                                                       // per fare cio uso un alert
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

                            alert.setTitle("FOTO");
                            alert.setContentText("VUOI RENDERE PRIVATA LA FOTO?");


                            Optional<ButtonType> result = alert.showAndWait();

                            if (result.get() == ButtonType.OK) {

                                try {
                                    PreparedStatement ps = main.DoPrepared("call rendi_fotografia_privata_o_pubblica(?,?)");

                                    int value = (int) ((Node) e.getSource()).getUserData();
                                    Node node =(Node) e.getSource();

                                    ps.setInt(1, value);
                                    ps.setString(2,"privata");

                                    gridPane.getChildren().remove(node);

                                    ps.execute();

                                    ps.close();
                                    main.Closeall();


                                } catch (SQLException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                        });
                    }

                    rs.close();
                    main.getCon().close();

                } catch (SQLException | IOException e) {
                    throw new RuntimeException(e);
                }

                pannel.setContent(gridPane);                                                                             // imposto la griglia come contenuto dello scroll pane

            });



        } catch (SQLException e) {throw new RuntimeException(e);}

    }
}
