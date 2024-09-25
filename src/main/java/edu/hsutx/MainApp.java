package edu.hsutx;

public class MainApp {

    public static void main(String[] args) {
        // Initialize the model (CowboySeatTree)
        CowboySeatTree seatTree = new CowboySeatTree();

        // Initialize the view (Swing-based GUI)
        SwingView view = new SwingView();

        // Initialize the controller with the model and view
        ReservationController controller = new ReservationController(seatTree, view);

        // Display the view (start the GUI)
        javax.swing.SwingUtilities.invokeLater(() -> view.createAndShowGUI());

        // Start processing CSV or handle user inputs via the controller
        // TODO - Fix This
        controller.processCSV("path/to/your/csvfile.csv");
    }
}
