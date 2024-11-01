package com.example.project3;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.DatePicker;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;

/**
 * This is a controller class for managing clinic appointments and billing operations.
 * This class handles the scheduling, reschedling, and cancellation of appointments,
 * displaying all appointments and provide billing statements for patient, as well as provider's credits.
 * @author Vy Nguyen, Shahnaz Khan
 */
public class ClinicManagerController {
    //Tab Pane components
    @FXML private TabPane mainTabPane;
    @FXML private Tab homeTab, appointmentsTab, billingTab;

    // Home tab components
    @FXML private ToggleGroup schedulerActions;
    @FXML private RadioButton rb_scheduleAction, rb_rescheduleAction, rb_cancelAction;
    @FXML private DatePicker dp_appointmentDate, dp_dob;
    @FXML private ChoiceBox<Timeslot> cb_timeslot, cb_rescheduleTimeslot;
    @FXML private ToggleGroup visitTypeGroup;
    @FXML private RadioButton rb_officeVisit, rb_imagingVisit;
    @FXML private ComboBox<Radiology> cmb_roomType;
    @FXML private ComboBox<Provider> cmb_providers;
    @FXML private Button bt_loadProviders, bt_submitAction;
    @FXML private TextArea ta_output; // TextArea for output display
    @FXML private TextField tf_firstName, tf_lastName;
    @FXML private Label lb_rescheduleTimeslot;


    // instance variable for managing providers, technicians, and appointments
    private static final List<Provider> providerList = new List<>();
    private static List<Technician> techRotationList = new List<>();
    private static final List<Appointment> allAppointments = new List<>();
    private int rotationIndex = 0; // keep track of last assigned technician index

    // Appointment tab components
    @FXML private ToggleGroup appointmentTypeGroup;
    @FXML private RadioButton rb_allAppt, rb_officeAppt, rb_imagingAppt;
    @FXML private Label lb_sortBy, lb_warningSortBy;
    @FXML private ChoiceBox<String> cb_sortBy;
    @FXML private TableView tbl_appointments;
    @FXML private TableColumn<Appointment, String> col_apptDate, col_timeslot, col_patient, col_provider;

    // Billing tab components
    @FXML private ChoiceBox<String> cb_statements;
    @FXML private TextArea ta_billingOutput;

    /**
     * Initializes the controller class, setting default states for UI components,
     * and registering event handlers.
     */
    @FXML
    public void initialize() {
        // TabPane: Listener to reset data when switching between tabs
        mainTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) ->
        {
            if (newTab == homeTab) {
                resetHomeTab();
            } else if (newTab == appointmentsTab) {
                resetAppointmentsTab();
            } else if (newTab == billingTab) {
                resetBillingTab();
            }
        });

        // Home tab:  Disable all fields until providers are loaded
        disableAllFields();
        rb_scheduleAction.setDisable(true);
        rb_rescheduleAction.setDisable(true);
        rb_cancelAction.setDisable(true);

        // Home tab: Load providers to list and display doctors to ComboBox upon button click
        bt_loadProviders.setOnAction(event -> {
            loadProviders();
            loadDoctors();
            bt_loadProviders.setDisable(true);// Disable the load button after providers are loaded
            //Enable action buttons and submit button
            rb_scheduleAction.setDisable(false);
            rb_rescheduleAction.setDisable(false);
            rb_cancelAction.setDisable(false);
        });

        // Home tab: Set timeslot to Combobox
        loadTimeslots();
        // Set custom display the time format for Timeslot objects in ComboBox
        setTimeslotDisplayFormat(cb_timeslot);
        setTimeslotDisplayFormat(cb_rescheduleTimeslot);

        // Home tab: Set room type options in ComboBox
        loadRoomTypes();

        // Home tab: Listener for scheduler action, enable submit button when scheduler action is selected
        schedulerActions.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (newToggle != null) {
                schedulerActionsSelected();
                bt_submitAction.setDisable(false);
            }
        });

        // Home tab: Instant alert on validity of appointment date and date of birth on date picker
        dp_appointmentDate.setOnAction(event -> apptDateSelected());
        dp_dob.setOnAction(event -> dobSelected());

        // Home tabl: Listener for visitTypeGroup, manage behavior of ComboBox based on visit type selection
        visitTypeGroup.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (newToggle == rb_imagingVisit) {
                // Imaging visit selected
                cmb_roomType.setDisable(false);
                cmb_providers.setDisable(true);
            } else if (newToggle == rb_officeVisit) {
                // Office visit selected
                cmb_roomType.setDisable(true);
                cmb_providers.setDisable(false);
            }
        });

        // Home tab: Sets action handler for the submit button.
        bt_submitAction.setOnAction(event -> submitButtonAction());

        // Appointments tab: Set default selection and sort options
        lb_sortBy.setVisible(true);
        cb_sortBy.setVisible(true);
        lb_warningSortBy.setVisible(true);
        tbl_appointments.getItems().clear();

        // Appointment tab: populate TableView
        populateTableView();


        // Appointments tab: Populate sort options
        cb_sortBy.getItems().addAll("Patient", "Date", "Location");

        // Appointments tab: Listener for sorting options in the ChoiceBox
        rb_allAppt.setOnAction(event -> allAppointmentSelected());
        rb_officeAppt.setOnAction(event -> officeAppointmentSelected());
        rb_imagingAppt.setOnAction(event -> imagingAppointmentSelected());

        // Appointments tab: Listener for ChoiceBox sorting changes
        cb_sortBy.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                lb_warningSortBy.setVisible(false);
                sortBySelected(newValue);
            }
        });

        // Billing tab: Listener for statement of ChoiceBox
        cb_statements.getItems().addAll("Patients", "Providers");

        // Billing tab: Print billing based on selection
        cb_statements.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                handleBillingTab();
            }
        });


    }

    /**
     * Reset Home tab to its default state
     */
    @FXML
    private void resetHomeTab() {
        clearFields();
        disableAllFields();
        schedulerActions.selectToggle(null);
    }

    /**
     * Reset Appointments tab to its default state
     */
    @FXML
    private void resetAppointmentsTab() {
        appointmentTypeGroup.selectToggle(null);
        lb_sortBy.setVisible(true);
        cb_sortBy.setVisible(true);
        cb_sortBy.getSelectionModel().clearSelection();
        cb_sortBy.setDisable(true);
        tbl_appointments.getSelectionModel().clearSelection();
        tbl_appointments.getItems().clear();
    }

    /**
     * Reset Billings Tab to its default state
     */
    @FXML
    private void resetBillingTab() {
        ta_billingOutput.clear();
        cb_statements.getSelectionModel().clearSelection();
    }

    /**
     * Shows an alert with the specific message
     * @param message alert message
     */
    @FXML
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Helper method: set up the techRotationList in the right order.
     */
    private void setTechRotation() {

        // Technicians Rotation are in reverse order of added LIFO
        List<Technician> reversedList= new List<>();
        for (int i = techRotationList.size() - 1; i >= 0; i--) {
            reversedList.add(techRotationList.get(i));
        }
        // Assign the reverseList back to Rotation List
        techRotationList = reversedList;
    }

    /**
     * Helper method: display techRotationList.
     */
    private void displayTechRotation() {
        String rotationList = "\nRotation list for the technicians:\n";
        for (int i = 0; i < techRotationList.size(); i++) {
            Technician tech = techRotationList.get(i);

            rotationList += tech.getProfile().getFirstName() + " "
                    + tech.getProfile().getLastName() + " ("
                    + tech.getLocation().name() + ")";
            if ( i < techRotationList.size() - 1) {
                rotationList += " --> ";
            }
        }
        ta_output.appendText(rotationList);
    }

    /**
     * Helper method: Parse tokens to create and add providers to list.
     *
     * @param tokens an array of strings where each element represents a data token for creating providers.
     *
     */
    private void parseProviders(String[] tokens) {
        Profile profile = new Profile(tokens[1], tokens[2], new Date(tokens[3]));
        Location location = Location.valueOf(tokens[4]);

        if (tokens[0].equals("D")) {
            Doctor doctor = new Doctor(profile, location, Specialty.valueOf(tokens[5].toUpperCase()), tokens[6]);
            if(!providerList.contains(doctor)) {
                providerList.add(doctor);
            }
        } else if (tokens[0].equals("T")) {
            Technician technician = new Technician(profile, location, Integer.parseInt(tokens[5]));
            if(!providerList.contains(technician )){
                providerList.add(technician);
                techRotationList.add(technician);
            }
        }
        Sort.provider(providerList);
    }

    /**
     * An event handler to populate providers to list and enables the action buttons
     */
    @FXML
    public void loadProviders() {
        // Open FileChooser dialog to select the file, filter by .txt file
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Providers File for the import");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        File file = chooser.showOpenDialog(new Stage());
        ta_output.clear(); // Clear the output area before appending new content

        if (file != null) {
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (line.isEmpty()) continue;
                    parseProviders(line.split("\\s+"));
                }
            } catch (FileNotFoundException e) {
                showAlert("No file loaded.") ;
            }

            // Display providers
            ta_output.appendText("Provider loaded to the list.\n");
            for ( Provider provider: providerList){
                ta_output.appendText(provider.toString() +"\n");
            }
            setTechRotation();
            displayTechRotation();
        }

    }

    /**
     * Helper method: configures the ComboBox to display only
     * the NPI, first name, and last name of each Doctor.
     */
    private void setProviderDisplayFormat() {
        cmb_providers.setConverter(new StringConverter<Provider>() {
            @Override
            public String toString(Provider provider) {
                if (provider instanceof Doctor) {
                    Doctor doctor = (Doctor) provider;
                    return "#" + doctor.getNPI() + " " + doctor.getProfile().getFirstName() + " " + doctor.getProfile().getLastName();
                }
                return ""; // Handle cases where provider is null or not a Doctor
            }

            @Override
            public Provider fromString(String string) {
                // Not needed for non-editable ComboBox
                return null;
            }
        });
    }

    /**
     * Event handler to load doctors information to Combobox
     * including npi, first name, and last name
     */
    @FXML
    void loadDoctors() {
        // Create an ObservableList to hold doctor names (first name and last name)
        ObservableList<Provider> doctorList = FXCollections.observableArrayList();

        // Loop through the providerList and add only Doctor names to the doctorNames list
        for (Provider provider : providerList) {
            if (provider instanceof Doctor) {
                Doctor doctor = (Doctor) provider;
                doctorList.add(provider);
            }
        }
        // Set the doctor names to the ComboBox (cmb_providers)
        cmb_providers.setItems(doctorList);
        setProviderDisplayFormat();
    }

    /**
     * Event handler to populate room types to ComboBox
     */
    @FXML
    public void loadRoomTypes() {
        // Create an ObservableList to hold the radiology services
        ObservableList<Radiology> roomTypes = FXCollections.observableArrayList();
        roomTypes.addAll(Arrays.asList(Radiology.values()));
        // Set the radiology services to the ComboBox (cmb_roomType)
        cmb_roomType.setItems(roomTypes);
    }

    /**
     * Configures the display format for Timeslot objects in a given ChoiceBox.
     * Sets a custom StringConverter to format each Timeslot as a string using
     *
     * @param choiceBox The ChoiceBox containing Timeslot objects for which the display format is set.
     */
    @FXML
    private void setTimeslotDisplayFormat(ChoiceBox<Timeslot> choiceBox) {
        choiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Timeslot timeslot) {
                return timeslot != null ? timeslot.toTimeFormat() : "";
            }

            @Override
            public Timeslot fromString(String string) {
                // This method is not needed for displaying items, so it can be left unimplemented
                return null;
            }
        });
    }

    /**
     * Event handler to populate the  ChoiceBox with Timeslot objects
     */
    @FXML
    private void loadTimeslots() {
        // List to hold Timeslot objects
        ObservableList<Timeslot> timeslotsList = FXCollections.observableArrayList();

        for (int i = 1; i <= 12; i++) {
            Timeslot timeslot = new Timeslot(Integer.toString(i));
            timeslotsList.add(timeslot);
        }
        // set timeslot object to Choicebox
        cb_timeslot.setItems(timeslotsList);
        cb_rescheduleTimeslot.setItems(timeslotsList);
    }

    /**
     * Helper method to clear the input fields after successful scheduling or cancellation
     **/
    private void clearFields() {
        dp_appointmentDate.setValue(null);
        cb_timeslot.getSelectionModel().clearSelection();
        tf_firstName.clear();
        tf_lastName.clear();
        ta_billingOutput.clear();
        dp_dob.setValue(null);
        cmb_providers.getSelectionModel().clearSelection();
        cmb_roomType.getSelectionModel().clearSelection();
        cb_rescheduleTimeslot.getSelectionModel().clearSelection();
        visitTypeGroup.selectToggle(null);
    }
    /**
     * Disables all input fields
     */
    @FXML
    void disableAllFields() {
        dp_appointmentDate.setDisable(true);
        dp_dob.setDisable(true);
        cb_timeslot.setDisable(true);
        tf_firstName.setDisable(true);
        tf_lastName.setDisable(true);
        cmb_roomType.setDisable(true);
        cmb_providers.setDisable(true);
        cb_rescheduleTimeslot.setVisible(false); // hide new timeslot
        lb_rescheduleTimeslot.setVisible(false);
        rb_imagingVisit.setDisable(true);
        rb_officeVisit.setDisable(true);
        bt_submitAction.setDisable(true);
    }

    /**
     * Enable requested fields for scheduling
     */
    void enableScheduleFields() {
        dp_appointmentDate.setDisable(false);
        dp_dob.setDisable(false);
        cb_timeslot.setDisable(false);
        tf_firstName.setDisable(false);
        tf_lastName.setDisable(false);
        rb_officeVisit.setDisable(false);
        rb_imagingVisit.setDisable(false);
    }

    /**
     * Enable requested fields for
     */
    void enableRescheduleFields() {
        dp_appointmentDate.setDisable(false);
        dp_dob.setDisable(false);
        cb_timeslot.setDisable(false);
        tf_firstName.setDisable(false);
        tf_lastName.setDisable(false);
        cb_rescheduleTimeslot.setVisible(true); // show new timeslot
        lb_rescheduleTimeslot.setVisible(true);
    }

    /**
     * Enable requested fields for cancelation
     */
    void enableCancelFields() {
        dp_appointmentDate.setDisable(false);
        dp_dob.setDisable(false);
        cb_timeslot.setDisable(false);
        tf_firstName.setDisable(false);
        tf_lastName.setDisable(false);
    }

    /**
     * Enables specific fields based on the selection of
     * scheduler action radio button toggle group schedulerAction
     */
    void schedulerActionsSelected() {
        // reset all fields
        disableAllFields();
        clearFields();

        RadioButton selectedAction = (RadioButton)schedulerActions.getSelectedToggle();
        if (selectedAction == null) {
            ta_output.appendText("\nPlease select an action: Schedule, Reschedule, or Cancel,");
            return;
        }
        switch (selectedAction.getText()){
            case "Schedule":
                enableScheduleFields();
                break;
            case "Reschedule" :
                enableRescheduleFields();
                break;
            case "Cancel" :
                enableCancelFields();
                break;
        }
    }

    /**
     * Handles scheduling, rescheduling, and cancellation of appointments.
     */
    @FXML
    void submitButtonAction() {
        RadioButton selectedAction = (RadioButton) schedulerActions.getSelectedToggle();
        // Check selected action and call the appropriate method
        switch (selectedAction.getText()) {
            case "Schedule":
                if(!areScheduleFieldsValid()){
                    return;
                }
                if (rb_officeVisit.isSelected()) {
                    // Schedule an office visit
                    scheduleOfficeAppointment();
                } else if (rb_imagingVisit.isSelected()) {
                    // Schedule an imaging visit
                    scheduleImagingAppointment();
                } else {
                    showAlert("Select a visit type.");
                }
                break;
            case "Reschedule":
                rescheduleAppointment(); // Calls the rescheduling method
                break;
            case "Cancel":
                cancelAppointment(); // Calls the cancel method
                break;
            default:
                showAlert("\nInvalid action selected.");
                break;
        }
    }

    /**
     * Helper method: validates if the given appointment date is an acceptable date for scheduling.
     *
     * @param apptDate the date to be validated
     * @return true if the appointment date is valid; false otherwise
     */
    private boolean isValidApptDate(Date apptDate){
        if(apptDate.isWithinSixMonths()) {
            showAlert("\nAppointment date: " + apptDate + " is not within six months.");
            return false;
        }
        if(apptDate.isToday() ||apptDate.isPastDate()) {
            showAlert("\nAppointment date: " + apptDate+ " is today or a date before today."); // test this
            return false;
        }
        if(apptDate.isWeekend()) {
            showAlert("\nAppointment date: " + apptDate + " is a Saturday or Sunday.");
            return false;
        }
        return true;
    }

    /**
     * Helper method: validates the given date of birth (DOB) to ensure it is a valid date, not today, and not a future date.
     *
     * @param dob the date of birth to be validated
     * @return true if the date of birth is valid; false otherwise
     */
    private boolean isValidDOB(Date dob){
        if (dob.isToday() || dob.isFutureDate()) {
            showAlert("Patient dob: " + dob + " is today or a date after today.");
            return false;
        }
        return true;
    }

    /**
     * Checks if the given appointment already exists in the list of all appointments.
     *
     * @param appt The appointment to check.
     * @return true if a duplicate appointment is found; false otherwise.
     */
    private boolean isDuplicateAppt(Appointment appt) {
        if (allAppointments.contains(appt)) {
            ta_output.appendText("\n" + appt.getProfile()
                    + " has an existing appointment at the same time slot.");
            return true;
        }
        return false;
    }

    /**
     * Event handler that retrieves the selected appointment date from the DatePicker (dp_appointmentDate).
     * Converts the selected date to a Date object and validates it using isValidApptDate().
     *
     * @return A Date object representing the selected appointment date, or null if the date is invalid.
     */
    @FXML
    private Date apptDateSelected() {
        String apptDateStr = dp_appointmentDate.getValue().toString();
        Date apptDate = new Date(apptDateStr);
        if(!isValidApptDate(apptDate)){
            return null;
        }
        return apptDate;
    }

    /**
     * Event handler that retrieves the selected date of birth (DOB) from the DatePicker (dp_dob).
     * Converts the selected date to a Date object and validates it using isValidDOB().
     *
     * @return A Date object representing the selected date of birth, or null if the date is invalid.
     */
    @FXML private Date dobSelected() {
        String dobStr = dp_dob.getValue().toString();
        Date dob = new Date(dobStr);
        if(!isValidDOB(dob)){
            return null;
        }
        return dob;
    }

    /**
     * Helper method: Checks whether provider is available for a specific appointment
     * @param appt the appointment to check
     * @return  true if provider is available, false otherwise
     */
    private boolean isProviderAvailable(Appointment appt) {
        Provider provider = (Provider) appt.getProvider();
        Date apptDate = appt.getDate();
        Timeslot timeslot = appt.getTimeslot();

        // Validate provider and handle null case
        if (provider == null) {
            ta_output.appendText("\nProvider cannot be null.");
            return false;
        }

        // Check if the provider already has an appointment at the same timeslot and date
        for (Appointment existingAppt : allAppointments) {
            if (existingAppt.getDate().equals(apptDate) && existingAppt.getTimeslot().equals(timeslot) &&
                    existingAppt.getProvider().equals(provider)) {
                ta_output.appendText("\n" + provider + " is not available at slot " + timeslot.toTimeFormat() + ".");
                return false; // Conflict found, provider is not available
            }
        }
        // If no conflicts found, the provider is available
        return true;
    }

    /**
     * Checks if all required fields for scheduling an appointment are filled.
     * Validates appointment date, DOB, timeslot, first name, and last name.
     * For imaging visits, checks that a room type is selected;
     * for office visits, ensures a provider is selected.
     *
     * @return true if all fields are valid; false otherwise.
     */
    private boolean areScheduleFieldsValid() {
        if (dp_appointmentDate.getValue() == null ||
                dp_dob.getValue() == null ||
                cb_timeslot.getSelectionModel().isEmpty() ||
                tf_firstName.getText().isEmpty() ||
                tf_lastName.getText().isEmpty()) {
            showAlert("Missing data tokens.");
            return false;
        }
        // Additional check for Imaging-specific fields
        if (rb_imagingVisit.isSelected() && cmb_roomType.getSelectionModel().isEmpty()) {
            showAlert("Select a room type for the imaging visit.");
            return false;
        }
        // Additional check for Office visit-specific field
        if (rb_officeVisit.isSelected() && cmb_providers.getSelectionModel().isEmpty()) {
            showAlert("Select a provider for the office visit.");
            return false;
        }
        return true;
    }


    /**
     * Schedules an office appointment if all required fields are valid.
     */
    private void scheduleOfficeAppointment() {
        // Validation for Schedule Office Appointment
        if (!areScheduleFieldsValid()) {
            return;
        }

        // Proceed with scheduling
        Date apptDate = apptDateSelected();
        Timeslot timeslot = cb_timeslot.getSelectionModel().getSelectedItem();
        String fName = tf_firstName.getText();
        String lName = tf_lastName.getText();
        Date dob = dobSelected();
        Profile patientProfile = new Profile(fName,lName,dob);
        Patient patient = new Patient(patientProfile, null);
        Provider provider = cmb_providers.getSelectionModel().getSelectedItem();

        // Check for invalid dates
        if(!isValidApptDate(apptDate) || !isValidDOB(dob)) {
            return;
        }
        Appointment newAppt = new Appointment(apptDate, timeslot, patient, provider);
        // Check for duplicate appointment and if provider available
        if (isDuplicateAppt(newAppt) || !isProviderAvailable(newAppt)){
            return;
        }
        // If the appointment is valid, add appointment.
        allAppointments.add(newAppt);
        ta_output.appendText("\n" + newAppt.toString() + " booked.");
        // Clears input fields after successful scheduling.
        clearFields();
    }

    /**
     * Helper method: check the imaging room is available.
     * @return true if available, otherwise false.
     */
    private boolean isRoomAvailable(Technician assignedTech, Timeslot requestTimeslot, Location location, Radiology requestRoom) {
        for (Appointment appt : allAppointments) {
            if (appt instanceof Imaging) {
                Imaging imagingAppt = (Imaging) appt;

                Technician currentTech = (Technician) appt.getProvider();
                if (currentTech.getLocation().equals(assignedTech.getLocation())
                        && imagingAppt.getRoom().equals(requestRoom)
                        && imagingAppt.getTimeslot().equals(requestTimeslot)){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Helper method: Checks if a technician is available on a specific date and timeslot.
     *
     * @param tech The technician to check.
     * @param requestDate The requested appointment date.
     * @param requestTimeslot The requested timeslot.
     * @return true if the technician is available; false otherwise.
     */
    private boolean isTechAvailable(Technician tech, Date requestDate, Timeslot requestTimeslot) {
        for(Appointment appt : allAppointments) {
            if (appt.getDate().equals(requestDate)
                    && appt.getTimeslot().equals(requestTimeslot)
                    && appt.getProvider().equals(tech)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Helper method: Finds the next available technician from the rotation list
     * to help schedule imaging appointment
     *
     * @param requestDate The date of the requested appointment.
     * @param requestTimeslot The timeslot of the requested appointment.
     * @param requestRoom The type of radiology room required for the appointment.
     * @return The available Technician who can fulfill the request, or null if none are available.
     */
    private Technician findAvailableTech(Date requestDate, Timeslot requestTimeslot, Radiology requestRoom) {
        int techCount = techRotationList.size();
        Technician assignedTech = null;

        // Rotate though the list of technicians to find an available match
        for (int i = 0; i < techCount; i++) {
            int currentIndex = (rotationIndex + i) % techCount;
            Technician currentTech = techRotationList.get(currentIndex);

            if (isTechAvailable(currentTech, requestDate, requestTimeslot)) {
                if (isRoomAvailable(currentTech, requestTimeslot, currentTech.getLocation(), requestRoom)) {
                    assignedTech = currentTech;
                    rotationIndex = (currentIndex + 1) % techCount; // update the rotation index
                    break;
                }
            }
        }
        return assignedTech;
    }

    /**
     * Schedule imaging appointments if all require fields are valid.
     */
    void scheduleImagingAppointment() {
        // Validation for Schedule Imaging Appointment
        if (!areScheduleFieldsValid()){
            return;
        }

        // Proceed with creating objects
        Date apptDate = apptDateSelected();
        Timeslot timeslot = cb_timeslot.getSelectionModel().getSelectedItem();
        String fName = tf_firstName.getText();
        String lName = tf_lastName.getText();
        Date dob = dobSelected();
        Profile patientProfile = new Profile(fName,lName,dob);
        Patient patient = new Patient(patientProfile, null);
        Radiology requestRoom = cmb_roomType.getSelectionModel().getSelectedItem();

        // check for valid dates
        if (!isValidApptDate(apptDate) || !isValidDOB(dob)) {
            return;
        }

        // Create appointment
        Appointment newAppt = new Appointment(apptDate, timeslot, patient);

        // Check for duplicate appointment
        if(isDuplicateAppt(newAppt)){
            return;
        }

        //Find and assign available tech and create appointment
        Technician assignedTech = findAvailableTech(apptDate, timeslot, requestRoom);
        if (assignedTech != null) {
            Appointment imagingAppt = new Imaging(apptDate, timeslot, patient, requestRoom);
            imagingAppt.setProvider(assignedTech);
            allAppointments.add(imagingAppt);
            ta_output.appendText("\n" + imagingAppt.toString() + " booked.");
            clearFields();
        } else {
            ta_output.appendText("\nCannot find an available technician at all locations for "
                    + requestRoom +  " at slot " + timeslot.toTimeFormat() + " .");
        }
    }

    /**
     * Helper method to check for valid tokens for reschedule fields
     * @return true if there are enough tokens, otherwise false.
     */
    private boolean areRescheduleFieldsValid() {
        if (dp_appointmentDate.getValue() == null ||
                dp_dob.getValue() == null ||
                cb_timeslot.getSelectionModel().isEmpty() ||
                tf_firstName.getText().isEmpty() ||
                tf_lastName.getText().isEmpty() ||
                cb_rescheduleTimeslot.getSelectionModel().isEmpty()) {
            showAlert("Missing data tokens.");
            return false;
        }
        return true;
    }

    /**
     *  Reschedules an existing appointment to a new timeslot if all required fields are valid.
     */
    private void rescheduleAppointment() {
        if(!areRescheduleFieldsValid()) {
            return;
        }

        // Create objects
        Date apptDate = apptDateSelected();
        Timeslot oldTimeslot = cb_timeslot.getSelectionModel().getSelectedItem();
        String fName = tf_firstName.getText();
        String lName = tf_lastName.getText();
        Date dob = dobSelected();
        Profile patientProfile = new Profile(fName,lName,dob);
        Patient patient = new Patient(patientProfile, null);
        Timeslot newTimeslot = cb_rescheduleTimeslot.getSelectionModel().getSelectedItem();
        Appointment originalAppt = null;
        Provider provider = null;

        // Find the appointment based on patient, date, and timeslot to get provider.
        for (Appointment existingAppt : allAppointments) {
            if (existingAppt.getDate().equals(apptDate) &&
                    existingAppt.getTimeslot().equals(oldTimeslot) &&
                    existingAppt.getProfile().equals(patient)) {
                originalAppt = existingAppt;
                provider = (Provider)existingAppt.getProvider();
                break;
            }
        }

        // Check if original appointment exists
        if (originalAppt == null) {
            showAlert("\n" + apptDate + " " + oldTimeslot.toTimeFormat() + " "
                    + patientProfile + " - appointment does not exist.");
            return;
        }

        // Check whether to reschedule imaging or office appointment
        Appointment newAppt;
        if(originalAppt instanceof Imaging) {
            Radiology requestRoom = ((Imaging) originalAppt).getRoom();
            newAppt = new Imaging(apptDate, newTimeslot,patient, requestRoom );
            newAppt.setProvider(provider);
        } else {
            newAppt = new Appointment(apptDate, newTimeslot, patient, provider);
        }

        // Check for new appointment conflict
        if (isDuplicateAppt(newAppt) || !isProviderAvailable(newAppt)){
            return;
        }

        // No conflict found, reschedule appointment.
        allAppointments.remove(originalAppt);
        allAppointments.add(newAppt);
        ta_output.appendText("\nReschedule to " + newAppt);
        clearFields();
    }

    /**
     * Helper method to check for valid tokens for cancel fields
     *
     * @return true if there are enough tokens, otherwise false.
     */
    private boolean areCancelFieldsValid() {
        if (dp_appointmentDate.getValue() == null ||
                cb_timeslot.getSelectionModel().isEmpty() ||
                tf_firstName.getText().isEmpty() ||
                tf_lastName.getText().isEmpty() ||
                dp_dob.getValue() == null) {
            showAlert("Missing data tokens.");
            return false;
        }
        return true;
    }

    /**
     * Cancel appointment if all required fields are valid.
     */
    private void cancelAppointment() {
        if(!areCancelFieldsValid()) {
            return;
        }

        // Create patient profile and appointment object
        Date apptDate = apptDateSelected();
        Timeslot timeslot = cb_timeslot.getSelectionModel().getSelectedItem();
        String fName = tf_firstName.getText();
        String lName = tf_lastName.getText();
        Date dob = dobSelected();
        Profile patientProfile = new Profile(fName,lName,dob);
        Person person = new Person(patientProfile);

        Appointment appointment = new Appointment(apptDate, timeslot, person);

        if(!allAppointments.contains(appointment)){
            showAlert("\n" + appointment.getDate().toString() + " "
                    + appointment.getTimeslot().toTimeFormat() + " "
                    + appointment.getProfile()
                    + " - appointment does not exist.");
        } else {
            allAppointments.remove(appointment);
            ta_output.appendText("\n" + appointment.getDate().toString() + " "
                    + appointment.getTimeslot().toTimeFormat() + " "
                    + appointment.getProfile()
                    + " - appointment has been canceled.");
            clearFields();
        }
    }


    /**
     * Helper method to display appointments to List View
     */
    private void populateTableView() {
        // Convert the list of appointments to an ObservableList
        ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();
        for (Appointment appt : allAppointments) {
            appointmentsList.add(appt); // Adjust toString() to display desired appointment details
        }

        // Set the items for the TableView
        tbl_appointments.setItems(appointmentsList);

        // Set cell value factories for each column to specify which property of Appointment should be displayed
        col_apptDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate().toString()));
        col_timeslot.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTimeslot().toTimeFormat()));
        col_patient.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProfile().toString()));
        col_provider.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProvider().toString()));
    }

    /**
     * Helper method to display in List View all appointments, sort by date/time/provider
     */
    private void displayByDate() {
        Sort.appointment(allAppointments, 'D');
        populateTableView();
    }

    /**
     * Helper method to display in List View all appointments, sort by patient/date/time
     */
    private void displayByPatient() {
        Sort.appointment(allAppointments, 'P');
        populateTableView();
    }

    /**
     * Helper method to display in List View all appointments, sort by county/date/time.
     */
    private void displayByLocation() {
        Sort.appointment(allAppointments,'L');
        populateTableView();
    }

    /**
     * Displays all appointments in the ListView based on the selected sorting option.
     * Defaults to sorting by "Patient" if the option is unrecognized.
     *
     * @param sortOption The selected sorting criteria ("Patient", "Date", or "Location").
     */
    @FXML
    void sortBySelected(String sortOption) {
        if (allAppointments.isEmpty()){
            showAlert("Schedule calendar is empty.");
        }
        lb_warningSortBy.setVisible(false);
        switch(sortOption){
            case "Patient": displayByPatient(); break;
            case "Date": displayByDate(); break;
            case "Location": displayByLocation(); break;
            default: tbl_appointments.getItems().clear(); // Clear the table view
                break;
        }
    }

    /**
     * Handles the selection of RadioButton "All" displaying all appointments.
     */
    @FXML
    void allAppointmentSelected() {
        cb_sortBy.setDisable(false); // enable sorting options
        cb_sortBy.getSelectionModel().clearSelection();
        tbl_appointments.getItems().clear();
        lb_warningSortBy.setVisible(true);
    }

    /**
     * Helper method: list office appointments sort by county/date/time.
     */
    private void displayOfficeAppointments() {
        ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();
        boolean hasOfficeAppointments = false;

        // Check if there are any non-Imaging (office) appointments
        for (Appointment appt : allAppointments) {
            if (!(appt instanceof Imaging)) {
                hasOfficeAppointments = true;
                break;  // Exit as soon as a non-Imaging appointment is found
            }
        }

        // If no office appointments, print empty message and return
        if (!hasOfficeAppointments) {
            showAlert("Schedule calendar is empty.");
            return;
        }

        // Sort all appointments by county/date/time and print only office appointments
        Sort.appointment(allAppointments, 'L');
        for (Appointment appt : allAppointments) {
            if (!(appt instanceof Imaging)) {  // Only print non-Imaging appointments
                appointmentsList.add(appt);
            }
        }
        tbl_appointments.setItems(appointmentsList);
    }

    /**
     * Handles the selection of RadioButton "Office" displaying only office appointments.
     */
    @FXML
    void officeAppointmentSelected() {
        cb_sortBy.setValue("Location");
        cb_sortBy.setDisable(true); // the only option is set by location
        tbl_appointments.getItems().clear();
        displayOfficeAppointments();
    }

    /**
     * Handle listing in List View:  radiology appointments sort by county/date/time.
     */
    @FXML
    private void displayImagingAppointments() {
        ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();
        boolean hasImagingAppointments = false;

        // Check if there are any Imaging appointments
        for (Appointment appt : allAppointments) {
            if (appt instanceof Imaging) {
                hasImagingAppointments = true;
                break;  // Exit as soon as an Imaging appointment is found
            }
        }
        // If no Imaging appointments, print empty message and return
        if (!hasImagingAppointments) {
            showAlert("Schedule calendar is empty."); // make it into alert message
            return;
        }
        // Sort all appointments by county/date/time and print Imaging appointments
        Sort.appointment(allAppointments, 'L');
        for (Appointment appt : allAppointments) {
            if (appt instanceof Imaging) {
                appointmentsList.add(appt);
            }
        }
        tbl_appointments.setItems(appointmentsList);
    }

    /**
     * Handles the selection of RadioButton "Imaging" displaying only radiology appointments.
     */
    @FXML
    void imagingAppointmentSelected() {
        cb_sortBy.setValue("Location");
        cb_sortBy.setDisable(true); // the only option is set by location
        tbl_appointments.getItems().clear();
        displayImagingAppointments();
    }


    /**
     * Handles the selection of Radiobutton in Billing Tab
     */
    @FXML
    private void handleBillingTab() {
        // Clear TextArea before displaying new content
        ta_billingOutput.clear();
        String selectedItem = cb_statements.getSelectionModel().getSelectedItem();
        if ("Patients".equals(selectedItem)) {
            printBillingStatements(); // Display patient billing statement
        } else if ("Providers".equals(selectedItem)) {
            printExpectedCredit(); // Display provider credit statement
        }
    }

    /**
     * Helper method to print Billing Statement
     * @param count the number of the patient out of the total patients
     * @param profile the profile of the person
     * @param totalDue  the total amount due
     */
    private void displayBillingStatement(int count, Profile profile, double totalDue){
        DecimalFormat df = new DecimalFormat("#,##0.00");
        ta_billingOutput.appendText("\n(" + count + ") "
                + profile.getFirstName() + " "
                + profile.getLastName() + " "
                + profile.getDOB() + " [amount due: $"
                + df.format(totalDue) + "]");
    }

    /**
     * Prints the billing statement for all appointments.
     */
    private void printBillingStatements() {
        if (allAppointments.isEmpty()) {
            showAlert("No billing statements available.");
            return;
        }

        Sort.appointment(allAppointments, 'P'); // sort by patient
        ta_billingOutput.appendText("** Billing statement ordered by patient **");

        double totalDue = 0.0;
        int count = 1;
        Profile currentProfile = null; // keep track of the patient currently processing

        for (int i = 0; i < allAppointments.size(); i++) {
            Appointment appt = allAppointments.get(i);

            Person provider = (Provider)appt.getProvider();
            Person patientProfile = appt.getProfile();

            // Ensure we are dealing with Patient objects
            if (patientProfile instanceof Patient) {
                Patient patient = (Patient) patientProfile;

                double charge = 0.0;
                if (provider instanceof Doctor) {
                    Doctor doctor = (Doctor) provider;
                    charge = doctor.getSpecialty().getCharge();
                } else if (provider instanceof Technician) {
                    Technician tech = (Technician) provider;
                    charge = tech.rate();
                }
                // If we're on a new patient, print the previous patient's statement
                if (currentProfile == null || !patient.getProfile().equals(currentProfile)) {
                    if (currentProfile != null) {
                        displayBillingStatement(count, currentProfile, totalDue);
                        count++;
                    }
                    currentProfile = patient.getProfile();
                    totalDue = 0.0; // Reset total due for the new patient
                }
                // Accumulate the amount due for the current patient
                totalDue += charge;
            }
        }
        // Print the last patient's statement
        if (currentProfile != null) {
            displayBillingStatement(count, currentProfile, totalDue);
        }
        ta_billingOutput.appendText("\n********** End of list **********");

    }


    /**
     * Prints the expected credit amounts for each provider
     * based on their appointments.
     */
    private void printExpectedCredit() {
        if (allAppointments.isEmpty()) {
            showAlert("No provider credit statement available.");
            return;
        }

        ta_billingOutput.appendText("** Credit amount ordered by provider. **\n");

        // Loop over the providerList, and for each provider, calculate the total credit
        for (int i = 0; i < providerList.size(); i++) {
            Provider currentProvider = providerList.get(i);
            double totalCredit = 0.0;

            // Iterate through all appointments to accumulate the credit for this provider
            for (int j = 0; j < allAppointments.size(); j++) {
                Appointment appointment = allAppointments.get(j);
                Provider provider = (Provider) appointment.getProvider();

                // Check if this appointment's provider matches the current provider
                if (provider.equals(currentProvider)) {
                    // Determine the charge based on provider type
                    if (provider instanceof Doctor) {
                        totalCredit += ((Doctor) provider).getSpecialty().getCharge();  // Get charge for doctor's specialty
                    } else if (provider instanceof Technician) {
                        totalCredit += ((Technician) provider).rate();  // Get charge for technician's rate
                    }
                }
            }
            // Print the provider with their total credit amount
            String providerCreditStatement = String.format("(%d) %s [credit amount: $%.2f]%n",
                    (i + 1),
                    currentProvider.getProfile(),
                    totalCredit);
            ta_billingOutput.appendText(providerCreditStatement);
        }
        ta_billingOutput.appendText("********** End of list **********");
    }
}