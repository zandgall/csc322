/* CSC322 SESSION 5: ASSIGNMENT - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Address Book
 # A program that registers people in an address book
 ## Entry
 # A mini class that stores information and implements Serializable to facilitate I/O

 : MADE IN NEOVIM */

package com.zandgall.csc322.session5.assignment;

import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.collections.ObservableList;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;

public class AddressBook extends Application {

	// Data
	ArrayList<Entry> entries = new ArrayList<Entry>();
	int currentSelection = 0;

	/* UI elements */

	Label title = new Label("Address Book");
	ListView<String> list;
	ObservableList<String> listContent = FXCollections.observableArrayList();
	ChangeListener<? super Number> listListener;

	// Form content
	GridPane content = new GridPane(10, 10);
	TextField firstNameField = new TextField(),
	          lastNameField = new TextField(),
	          streetField = new TextField(),
	          cityField = new TextField(),
	          phoneField = new TextField(),
	          emailField = new TextField();
	ComboBox<String> stateField = new ComboBox<>();
	TextArea notesField = new TextArea();

	Label firstNameLabel = new Label("First"),
	      lastNameLabel = new Label("Last"),
	      streetLabel = new Label("Street"),
	      cityLabel = new Label("City"),
	      phoneLabel = new Label("Phone"),
	      emailLabel = new Label("Email"),
	      notesLabel = new Label("Notes"),
	      stateLabel = new Label("State");

	// Buttons
	HBox buttonContainer = new HBox(10);
	Button add = new Button("Add"), delete = new Button("Delete"), update = new Button("Update");

	@Override
	public void start(Stage stage) {

		BorderPane mainPane = new BorderPane();

		// Add list and title	
		list = new ListView<String>(listContent);
		mainPane.setMargin(list, new Insets(12,12,12,12));
		mainPane.setLeft(list);
		title.setFont(new Font(36));
		mainPane.setTop(title);
		mainPane.setAlignment(title, Pos.CENTER);

		listListener = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				switchFromTo(oldValue.intValue(), newValue.intValue());
			}
		};
		list.getSelectionModel().selectedIndexProperty().addListener(listListener);

		// Add buttons
		buttonContainer.getChildren().addAll(add, delete, update);
		buttonContainer.setAlignment(Pos.CENTER);
		mainPane.setMargin(buttonContainer, new Insets(12, 12, 12, 12));
		mainPane.setBottom(buttonContainer);
		add.setOnAction(e -> {addEntryFromForm();});

		// Add form content
		content.setAlignment(Pos.CENTER);	
		content.add(firstNameLabel, 0, 0);
		content.add(firstNameField, 1, 0);
		content.add(lastNameLabel, 2, 0);
		content.add(lastNameField, 3, 0);
		content.add(streetLabel, 0, 1);
		content.add(streetField, 1, 1, 3, 1); // Covers 3 columns right
		content.add(cityLabel, 0, 2);
		content.add(cityField, 1, 2);
		content.add(stateLabel, 2, 2);
		content.add(stateField, 3, 2);
		content.add(phoneLabel, 0, 3);
		content.add(phoneField, 1, 3);
		content.add(emailLabel, 0, 4);
		content.add(emailField, 1, 4);
		content.add(notesLabel, 0, 5);
		content.add(notesField, 1, 5, 3, 6); // covers 3 columns x 6 rows
		mainPane.setMargin(content, new Insets(12, 12, 12, 12));
		mainPane.setCenter(content);

		Scene scene = new Scene(mainPane, 900, 500);
		stage.setTitle("Address Book");
		stage.setScene(scene);
		stage.show();

		autoload();
	}

	private void switchFromTo(int previous, int current) {
		// Don't update entry if none were selected last
		if(previous != -1) {
			updateEntryFromForm(entries.get(previous));
			listContent.set(previous, firstNameField.getText() + " " + lastNameField.getText());
		}
		// Don't update form if no entry is selected now, or selected out of bounds
		if(current < 0 || current >= entries.size())
			updateFormFromEntry(entries.get(current));
		autosave();
		currentSelection = current;
	}

	private void updateEntryFromForm(Entry e) {
		firstNameField.setText(e.firstName);
		lastNameField.setText(e.lastName);
		streetField.setText(e.street);
		cityField.setText(e.city);
		phoneField.setText(e.phone);
		emailField.setText(e.email);
		notesField.setText(e.notes);
		// TODO: State
	}

	private void updateFormFromEntry(Entry e) {
		e.firstName = firstNameField.getText();
		e.lastName = lastNameField.getText();
		e.street = streetField.getText();
		e.city = cityField.getText();
		e.phone = phoneField.getText();
		e.email = emailField.getText();
		e.notes = notesField.getText();
		// TODO: State
	}

	private void autoload() {
		try { 
			ObjectInputStream s = new ObjectInputStream(new FileInputStream("data.bin"));
			while(s.available()>0)
				entries.add((Entry)s.readObject());
			// If there is a selected entry, update the form to reflect
			if(currentSelection != -1)
				updateFormFromEntry(entries.get(currentSelection));
		} catch(Exception e) {
			System.out.println("Could not open database, using blank template");
		}
	}

	private void addEntryFromForm() {
		currentSelection = entries.size();
		entries.add(new Entry());
		listContent.add(firstNameField.getText() + " " + lastNameField.getText());
		updateEntryFromForm(entries.get(currentSelection));
	}

	private void autosave() {
		// Make sure current entry is updated (if one is selected) before writing it
		if(currentSelection == -1)
			updateEntryFromForm(entries.get(currentSelection));

		try {
			ObjectOutputStream s = new ObjectOutputStream(new FileOutputStream("data.bin"));
			for(Entry e : entries)
				s.writeObject(e);
		} catch(Exception e) {
			System.err.println("Could not save database!");
			e.printStackTrace();
		}
	}

	// A simple storage class
	private static class Entry {
		public String firstName = "", lastName = "", street = "", city = "", phone = "", email = "", notes = "";
		public Entry() {}
	}
}

