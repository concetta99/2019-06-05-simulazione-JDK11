/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Model;
import it.polito.tdp.crimes.model.Vicino;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxAnno"
    private ComboBox<Year> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxMese"
    private ComboBox<Month> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="boxGiorno"
    private ComboBox<Integer> boxGiorno; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaReteCittadina"
    private Button btnCreaReteCittadina; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

  
    @FXML
    void doCreaReteCittadina(ActionEvent event) {
    	txtResult.clear();
    	Year anno = boxAnno.getValue();
    	if(anno.getValue()==0)
    	{
    		txtResult.appendText("Selezionare un anno! \n");
    		return;
    	}
    	model.creaGrafo(anno);
    	txtResult.appendText("Grafo creato con " + model.getGrafo().vertexSet().size()+ " vertici e "+ model.getGrafo().edgeSet().size()+ " archi. \n");
    	txtResult.appendText("\n");
    	for(Integer i: model.getVertici(anno))
    	{
    		txtResult.appendText("VICINI A " +i +"\n" );
    		List<Vicino> vicini = this.model.getVicini(i);
    		for(Vicino v: vicini)
    		{
    			txtResult.appendText(v.getDistretto() + " " + v.getDistanza() + "\n");

    		}
    		
    	}
    	boxMese.getItems().addAll(model.getMesi(anno));
    	
    	
    	
    
    }

    @FXML
    void doSimula(ActionEvent event) {
    	
    	txtResult.clear();
    	Year anno= boxAnno.getValue();
    	Month mese = boxMese.getValue();
    	Integer giorno = boxGiorno.getValue();
    	int N;
    	if(anno==null || mese==null || giorno==0)
    	{
    		txtResult.appendText("Inserire giorno, mese, anno!\n");
    		return ;
    	}
    	try{
    		N = Integer.parseInt(txtN.getText());
    		if(N<0 || N>10)
	    	{
	    		txtResult.appendText("Inserire un numero compreso fra 1 e 10!\n");
	    		return ;
	    	}
    		
    		
    	}catch (NumberFormatException e)
    	{
    		txtResult.appendText("Inserire un numero!\n");
    		return ;
    	}
    	
    	try {
    		LocalDate.of(anno.getValue(), mese, giorno);
    	} catch (DateTimeException e) {
        	this.txtResult.clear();
    		txtResult.appendText("Data non corretta\n");
    	}
    	
    	txtResult.appendText("Simulo con " + N + " agenti");
    	txtResult.appendText("\nCRIMINI MAL GESTITI: " + this.model.simula(anno.getValue(), mese.getValue(), giorno, N));
    	
    	
    	
    	
    	

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGiorno != null : "fx:id=\"boxGiorno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaReteCittadina != null : "fx:id=\"btnCreaReteCittadina\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	boxAnno.getItems().addAll(model.getAnni());
    	boxGiorno.getItems().addAll(model.getGiorno());
    	
    }
}
