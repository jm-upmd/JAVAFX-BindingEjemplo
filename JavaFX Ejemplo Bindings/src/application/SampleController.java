package application;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.binding.When;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class SampleController implements Initializable {
	
	// Colores background para la etiqueta  central
	Background whiteBG = new Background(new BackgroundFill(Color.WHITE, null, null));
    Background colorBG = new Background(new BackgroundFill(Color.PINK, null, null));
	String [] nombreColores = {"Negro","Rojo","Verde","Azul"};
   
	List<Node> rbList; // Nodos radiobutton de la derecha 
	List<MenuItem> menList; // Nodos del menú 
    
	@FXML Label mensaje;
	@FXML CheckBox bgCheck;
	@FXML Slider slider;
	@FXML Label sliderVal;
	@FXML VBox vBoxIzq;
	@FXML Menu opsMenu;
	@FXML TextField txField;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Obtiene los controles radibuttons de la derecha y
		// las opciones del menu Color.
		
		rbList = vBoxIzq.getChildren(); // Nodos dentro de Vbox 
		menList = opsMenu.getItems(); // Nodos del menú 
		
		inicializaRadibutonsYmenu();
		creaBindings();
		
	}
	
	private void creaBindings() {
		// Binding entre campo de escritura del texto y la etiqueta
		
		mensaje.textProperty().bind(txField.textProperty());
		mensaje.backgroundProperty().bind(new When(bgCheck.selectedProperty()).then(colorBG).otherwise(whiteBG));
		sliderVal.textProperty().bind(slider.valueProperty().asString("%.2f"));
		
		for(int i=0; i < nombreColores.length; i++ ) {
			
			RadioButton rb = (RadioButton) rbList.get(i);
			RadioMenuItem rm = (RadioMenuItem) menList.get(i);
			
			rb.selectedProperty().bindBidirectional(rm.selectedProperty());
		}
		
	}

	private void inicializaRadibutonsYmenu() {
		ToggleGroup colorGroup = new ToggleGroup();

		Color [] colores = {Color.BLACK,Color.RED,Color.GREEN,Color.BLUE};
		
		// Hace setText de los radiobutons y las opciones del menú con los 
		//nombres de los colores

		Node node;
		MenuItem menuItem;
		RadioMenuItem radioMenuItem;
		RadioButton rButton = null;
		
		for (int i = 0; i < nombreColores.length; i++) {
			
			// Pone nombre a los radiobuttons de la derecha
			node = rbList.get(i);
			if(node instanceof RadioButton) {
				rButton = (RadioButton)node;
				rButton.setText(nombreColores[i]);
			}
			
			// Pone nombre a las opciones de menuñ y les asigna
			// el grupo colorGroup para que solo una pueda estar 
			// seleccionada y gestionar a través de este grupo el 
			// evento de selección.
			menuItem = menList.get(i);
			menuItem.setText(nombreColores[i]);
			radioMenuItem = (RadioMenuItem)menuItem;
			radioMenuItem.setToggleGroup(colorGroup);
			
			// En el atributro userData guarda el objeto color
			// Este atributo lo tienen todos los controles y sirve para guardar
			// cualquier cosas que necesitemos asociar al control...
			
			menuItem.setUserData(colores[i]);
			
		} // for
		
		// Marca como seleccionado la primera opción del menú
		((RadioMenuItem) menList.get(0)).setSelected(true);
		
		// Crea listener para el grupo de opciones del menú.
		
		colorGroup.selectedToggleProperty().addListener(new ChangeListener<>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				Toggle t = colorGroup.getSelectedToggle(); // opción seleccionada
				if(t != null) {
					// Toma el color que tiene guardado y lo asigna al mensaje
					Color c = (Color)t.getUserData();
					mensaje.setTextFill(c);
				}
				
			}
		});
	}
}
