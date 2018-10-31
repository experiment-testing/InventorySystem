package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.DBConnection;
import main.Employee;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Author: Afif Al Mamun
 * Written on: 8/1/2018
 * Project: TeslaRentalInventory
 **/
public class EmployeeListGUI implements Initializable {

    @FXML
    private JFXTextField txtUser;

    @FXML
    private JFXPasswordField txtPass;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXComboBox<String> cboAccessLevel;

    @FXML
    private TableView<Employee> tbl;

    @FXML
    private TableColumn<Employee, String> username;

    @FXML
    private TableColumn<Employee, String> pass;

    @FXML
    private TableColumn<Employee, String> email;

    @FXML
    private TableColumn<Employee, String> access;

    @FXML
    private JFXButton btnAddNew;

    @FXML
    private FontAwesomeIconView btnAddIcon;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private FontAwesomeIconView btnAddIcon1;

    @FXML
    void deleteEmp(ActionEvent event) {
        Connection con = DBConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM user WHERE username='"+txtUser.getText()+"'");
            ps.executeUpdate();

            txtUser.getScene().getWindow().hide();
            new PromptDialogGUI("Operation Successful", "Record deleted");

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void updateEmp(ActionEvent event) {
        Connection con = DBConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE user SET username = ?, password = ?, email = ?, accessLevel = ? WHERE username = '"+txtUser.getText()+"'");
            ps.setString(1, txtUser.getText());
            ps.setString(2, txtPass.getText());
            ps.setString(3, txtEmail.getText());
            ps.setString(4, cboAccessLevel.getValue());
            ps.executeUpdate();

            txtUser.getScene().getWindow().hide();
            new PromptDialogGUI("Operation Successful", "Record updated");
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ObservableList<String> cbo = FXCollections.observableArrayList();
        cbo.addAll("Employee", "Admin");

        cboAccessLevel.setItems(cbo);

        tbl.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2)
            {
                loadContents();
            }
        });

        Connection connection = DBConnection.getConnection();
        ObservableList<Employee> list = FXCollections.observableArrayList();

        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        pass.setCellValueFactory(new PropertyValueFactory<>("pass"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        access.setCellValueFactory(new PropertyValueFactory<>("access"));

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM user");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Employee(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));
            }

            tbl.setItems(list);

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadContents() {
        Employee e = tbl.getSelectionModel().getSelectedItem();

        txtUser.setText(e.getUsername());
        txtPass.setText(e.getPass());
        txtEmail.setText(e.getEmail());
        cboAccessLevel.setValue(e.getAccess());
    }
}