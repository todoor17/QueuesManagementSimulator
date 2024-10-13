package GUI;

import BusinessLogic.SelectionPolicy;
import BusinessLogic.SimulationManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class HelloController {
    @FXML
    private TextField nrOfServers;

    @FXML
    private TextField nrOfClients;

    @FXML
    private TextField minArrivalTime;

    @FXML
    private TextField maxArrivalTime;

    @FXML
    private TextField minProcessingTime;

    @FXML
    private TextField maxProcessingTime;

    @FXML
    private TextField timeLimit;

    @FXML
    private Button btn;

    @FXML
    private TextArea textArea;

    @FXML
    private RadioButton rbShortestTime;

    @FXML
    private RadioButton rbShortestQueue;

    public void setText(ActionEvent e) {
        if (!verifiyInputs()) {
            return;
        } else {
            int timeLimitValue = Integer.parseInt(timeLimit.getText());
            int minArrivalTimeValue = Integer.parseInt(minArrivalTime.getText());
            int maxArrivalTimeValue = Integer.parseInt(maxArrivalTime.getText());
            int minProcessingTimeValue = Integer.parseInt(minProcessingTime.getText());
            int maxProcessingTimeValue = Integer.parseInt(maxProcessingTime.getText());
            int nrOfServersValue = Integer.parseInt(nrOfServers.getText());
            int nrOfClientsValue = Integer.parseInt(nrOfClients.getText());
            if (rbShortestQueue.isSelected()) {
                SimulationManager sim = new SimulationManager(SelectionPolicy.SHORTEST_QUEUE, timeLimitValue, minArrivalTimeValue, maxArrivalTimeValue, minProcessingTimeValue, maxProcessingTimeValue, nrOfServersValue, nrOfClientsValue, textArea);
                new Thread(sim).start();
            } else if (rbShortestTime.isSelected()) {
                SimulationManager sim = new SimulationManager(SelectionPolicy.SHORTEST_TIME, timeLimitValue, minArrivalTimeValue, maxArrivalTimeValue, minProcessingTimeValue, maxProcessingTimeValue, nrOfServersValue, nrOfClientsValue, textArea);
                new Thread(sim).start();
            }
        }
    }

    public boolean verifiyInputs() {
        if (!rbShortestQueue.isSelected() && !rbShortestTime.isSelected()) {
            textArea.setText("Select a strategy");
            return false;
        } else if (nrOfClients.getText().isEmpty() || nrOfServers.getText().isEmpty() || minArrivalTime.getText().isEmpty() || maxArrivalTime.getText().isEmpty()) {
            textArea.setText("One or more inputs are empty");
            return false;
        } else if (minProcessingTime.getText().isEmpty() || maxProcessingTime.getText().isEmpty() || timeLimit.getText().isEmpty()) {
            textArea.setText("One or more inputs are empty");
            return false;
        } else if (Integer.parseInt(timeLimit.getText()) <= 0) {
            textArea.setText("Time is smaller than 0");
            return false;
        } else if (Integer.parseInt(minArrivalTime.getText()) <= 0 || Integer.parseInt(minProcessingTime.getText()) <= 0) {
            textArea.setText("Arrival or processing time are smaller than o");
            return false;
        } else if (Integer.parseInt(minArrivalTime.getText()) >= Integer.parseInt(maxArrivalTime.getText()) || Integer.parseInt(maxArrivalTime.getText()) > Integer.parseInt(timeLimit.getText())) {
            textArea.setText("Arrival time interval is invalid");
            return false;
        } else if (Integer.parseInt(minProcessingTime.getText()) >= Integer.parseInt(maxProcessingTime.getText()) || Integer.parseInt(maxProcessingTime.getText()) > Integer.parseInt(timeLimit.getText())) {
            textArea.setText("Processing time interval is invalid");
            return false;
        }
        return true;
    }
}