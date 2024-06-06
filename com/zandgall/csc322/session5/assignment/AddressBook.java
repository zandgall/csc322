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
import java.io.Serializable;

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
	boolean addFlag = false;

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
	ObservableList<String> stateList = FXCollections.observableArrayList();
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
		update.setOnAction(e -> {
			if(currentSelection < 0 || currentSelection >= entries.size())
				return;
			updateEntryFromForm(entries.get(currentSelection));
			listContent.set(currentSelection, firstNameField.getText() + " " + lastNameField.getText());

			list.getSelectionModel().select(currentSelection);
			list.getFocusModel().focus(currentSelection);
			list.scrollTo(currentSelection);
		});

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
		// Add all states abbreviations
		stateList.addAll("AL","AK","AZ","AR","CA","CO","CT","DE","FL","GA","HI","ID","IL","IN","IA","KS","KY","LA","ME","MD","MA","MI","MN","MS","MO","MT","NE","NV","NH","NJ","NM","NY","NC","ND","OH","OK","OR","PA","RI","SC","SD","TN","TX","UT","VT","VA","WA","WV","WI","WY");
		stateField.setItems(stateList);

		Scene scene = new Scene(mainPane, 900, 500);
		stage.setTitle("Address Book");
		stage.setScene(scene);
		stage.show();
		// Save when closing
		stage.onCloseRequestProperty().setValue(e -> { autosave(); });

		// Try loading data.bin
		autoload();
	}

	private void switchFromTo(int previous, int current) {
		// Don't update entry if none were selected last, or if adding
		if(previous != -1 && !addFlag) {
			updateEntryFromForm(entries.get(previous));
			listContent.set(previous, firstNameField.getText() + " " + lastNameField.getText());
		}
		addFlag = false;

		// Don't update form if no entry is selected now, or selected out of bounds
		if(current >= 0 && current < entries.size())
			updateFormFromEntry(entries.get(current));

		currentSelection = current;
		autosave();
}

	private void updateFormFromEntry(Entry e) {
		firstNameField.setText(e.firstName);
		lastNameField.setText(e.lastName);
		streetField.setText(e.street);
		cityField.setText(e.city);
		phoneField.setText(e.phone);
		emailField.setText(e.email);
		notesField.setText(e.notes);
		stateField.getSelectionModel().select(e.state);
	}

	private void updateEntryFromForm(Entry e) {
		// Using StringBuffer to copy Strings without reference
		e.firstName = new StringBuffer(firstNameField.getText()).toString();
		e.lastName = new StringBuffer(lastNameField.getText()).toString();
		e.street = new StringBuffer(streetField.getText()).toString();
		e.city = new StringBuffer(cityField.getText()).toString();
		e.phone = new StringBuffer(phoneField.getText()).toString();
		e.email = new StringBuffer(emailField.getText()).toString();
		e.notes = new StringBuffer(notesField.getText()).toString();
		e.state = stateField.getSelectionModel().getSelectedItem();
	}

	private void autoload() {
		try { 
			ObjectInputStream s = new ObjectInputStream(new FileInputStream("data.bin"));
			entries.clear();
			int n = s.readInt();
			for(int i = 0; i < n; i++) {
				entries.add((Entry)s.readObject());
				listContent.add(entries.get(i).firstName + " " + entries.get(i).lastName);
			}
			s.close();
			// If there is a selected entry, update the form to reflect
			if(currentSelection >= 0 && currentSelection < entries.size()) {
				updateFormFromEntry(entries.get(currentSelection));

				list.getSelectionModel().select(currentSelection);
				list.getFocusModel().focus(currentSelection);
				list.scrollTo(currentSelection);
			}
		} catch(Exception e) {
			System.out.println("Could not open database, using blank template");
		}
	}

	private void addEntryFromForm() {
		currentSelection = entries.size();
		entries.add(new Entry());
		listContent.add(firstNameField.getText() + " " + lastNameField.getText());	
		updateEntryFromForm(entries.get(currentSelection));
		addFlag = true;

		list.getSelectionModel().select(currentSelection);
		list.getFocusModel().focus(currentSelection);
		list.scrollTo(currentSelection);
	}

	private void autosave() {
		// Make sure current entry is updated (if one is selected) before writing it
		if(currentSelection != -1)
			updateEntryFromForm(entries.get(currentSelection));

		try {
			ObjectOutputStream s = new ObjectOutputStream(new FileOutputStream("data.bin"));
			s.writeInt(entries.size());
			for(Entry e : entries)
				s.writeObject(e);
			s.close();
		} catch(Exception e) {
			System.err.println("Could not save database!");
			e.printStackTrace();
		}
	}

	// A simple storage class
	private static class Entry implements Serializable {
		public String firstName = "", lastName = "", street = "", city = "", state = "", phone = "", email = "", notes = "";
		public Entry() {}

		// Used for debugging
		@Override
		public String toString() { return firstName + " " + lastName + " (" + notes + ")"; } 
	}
}

