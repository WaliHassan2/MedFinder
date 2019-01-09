import BST.BinarySearchTree;
import Interfaces.PriQueueInterface;
import Interfaces.UnboundedQueueInterface;
import Interfaces.WeightedGraphInterface;
import LinkedLists.LinkedUnbndQueue;
import Objects.Hospital;
import Objects.Hospital2;
import com.maxmind.geoip2.WebServiceClient;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.InsightsResponse;
import com.maxmind.geoip2.record.Location;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import priorityQueues.Heap;
import priorityQueues.MinHeap2;
import priorityQueues.WeightedGraph;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Modified by Hollan on 11/13/16.
 */
public class HospitalFX extends Application {
    private ImageView imv;
    private Button btn;
    private Button geoBtn;
    private String name;
    private String streetAddress;
    private String city;
    private String state;
    private String zip;
    private Double distance;
    private String latitude;
    private String longitude;
    private String phone;
    private String photo;
    private String website;
    private String ipLat;
    private String ipLong;
    private String finalVert;
    private Slider slid = new Slider();
    private double radiusDist = 50;
    private ObservableList<Hospital> hospitalData = FXCollections.observableArrayList();
    private BinarySearchTree<Hospital> hospitalBSTree;
    private TextField searchKey = new TextField();
    private ImageView imageView;
    private int count = 0;
    private Hospital myLocation;
    private ObservableList<Hospital> latLongData = FXCollections.observableArrayList();
    private BinarySearchTree<String> miles;
    //private Object myLocation = new Object();
    final int HOSPITAL_SIZE = 140;
    private String startVert;
    UnboundedQueueInterface<String> vertexQueue = new LinkedUnbndQueue<String>();
    WeightedGraph<String> hospitalGraph = new WeightedGraph<String>();
    Label lbl;
    //private HBox layout;

    public BinarySearchTree<Hospital> getHospitalBSTree() {
        return hospitalBSTree;
    }

    public BinarySearchTree<String> getMilesBSTree() {
        return miles;
    }

    /**
     * Returns the data as an observable list of Hospitals
     *
     * @return
     */

    public ObservableList<Hospital> getHospitalData() {
        return hospitalData;
    }

    public void setHospitalData(ObservableList<Hospital> hospitalData) {
        this.hospitalData = hospitalData;
    }

    static Stage showStage = new Stage();


    @Override
    public void start(Stage primaryStage) throws IOException {
        loadHospital();
        //Collections.sort(getHospitalData(), new DistanceComparator());
        HospitalFX.showStage = primaryStage;

        primaryStage.setTitle("\t\t\t\t\t\t\t\t\t\t\tHospital Seeker");
        ObservableList<Hospital> HospitalDistanceResultTable = FXCollections.observableArrayList();
//        TextField input = new TextField();

        /**
         * Splitpane Controls
         */
        SplitPane background = new SplitPane();
        background.setOrientation(Orientation.VERTICAL);

        while (count < 6) {
            /**
             * Tableview Controls
             */
            TableView<Hospital> table = new TableView<>();
            table.setEditable(true);
            TableColumn nameCol = new TableColumn("Name");
            nameCol.setCellValueFactory(new PropertyValueFactory<Hospital, String>("name"));
            TableColumn addressCol = new TableColumn("Address");
            addressCol.setCellValueFactory(new PropertyValueFactory<Hospital, String>("streetAddress"));
            TableColumn cityCol = new TableColumn("city");
            cityCol.setCellValueFactory(new PropertyValueFactory<Hospital, String>("city"));
            TableColumn stateCol = new TableColumn("State");
            stateCol.setCellValueFactory(new PropertyValueFactory<Hospital, String>("state"));
            TableColumn zipCol = new TableColumn("Zipcode");
            zipCol.setCellValueFactory(new PropertyValueFactory<Hospital, String>("zip"));
            TableColumn distCol = new TableColumn("Miles Away");
            distCol.setCellValueFactory(new PropertyValueFactory<Hospital, Double>("distance"));
            TableColumn latCol = new TableColumn("Latitude");
            latCol.setCellValueFactory(new PropertyValueFactory<Hospital, String>("latitude"));
            TableColumn longCol = new TableColumn("Longitude");
            longCol.setCellValueFactory(new PropertyValueFactory<Hospital, String>("longitude"));
            TableColumn phoneCol = new TableColumn("Phone Number");
            phoneCol.setCellValueFactory(new PropertyValueFactory<Hospital, String>("phone"));
            TableColumn photoCol = new TableColumn("Photo");
            photoCol.setCellValueFactory(new PropertyValueFactory<Hospital, String>("photo"));
            TableColumn webCol = new TableColumn("Website");
            webCol.setCellValueFactory(new PropertyValueFactory<Hospital, String>("website"));

            table.getColumns().addAll(nameCol, addressCol, cityCol, stateCol, zipCol, distCol, latCol, longCol, phoneCol, photoCol, webCol);
            photoCol.setVisible(false);
            webCol.setVisible(false);
            distCol.setVisible(false);

            // 1. Wrap the ObservableList in a FilteredList (initially display all data).
            FilteredList<Hospital> filteredData = new FilteredList<>(getHospitalData(), p -> true);

            // 2. Set the filter Predicate whenever the filter changes.
            searchKey.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(hospital -> {
                    // If filter text is empty, display all persons.
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    // Compare first name and last name of every person with filter text.
                    String lowerCaseFilter = newValue.toLowerCase();

                    if (hospital.getName().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches first name.
                    } else if (hospital.getState().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches last name.
                    } else if (hospital.getZip().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches last name.
                    } else if (hospital.getCity().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches last name.
                    } else if (hospital.getStreetAddress().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches last name.
                    } else if (hospital.getPhone().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches last name.
                    } else if (hospital.getWebsite().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches last name.
                    } else if (hospital.getLatitude().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches last name.
                    } else if (hospital.getLongitude().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches last name.
                    }

                    return false; // Does not match.
                });
            });

            // 3. Wrap the FilteredList in a SortedList.
            SortedList<Hospital> sortedData = new SortedList<>(filteredData);

            // 4. Bind the SortedList comparator to the TableView comparator.
            sortedData.comparatorProperty().bind(table.comparatorProperty());

            //table.setItems(getHospitalData());
            table.setItems(sortedData);

            /**
             * HBox controls
             */
            HBox layout = new HBox(10);
            layout.setMaxHeight(10);
            layout.setPrefHeight(10);
            layout.setFillHeight(false);
            VBox box = new VBox(3);
            box.setMaxHeight(3);
            box.setPrefHeight(3);
            box.setPadding(new Insets(20, 20, 20, 30));
            //box.setFillHeight(false);
            Label lb = new Label(" Distance Radius ");
            TilePane tPain = new TilePane(Orientation.HORIZONTAL);
            tPain.setPadding(new Insets(20, 20, 20, 10));
            tPain.setHgap(200.0);
            tPain.setVgap(1.0);

            //slid = new Slider();
            slid.setMin(0);
            slid.setMax(150);
            slid.setValue(50);
            slid.setShowTickLabels(true);
            slid.setShowTickMarks(true);
            slid.setBlockIncrement(15);
            //lbl.setVisible(false);

//        searchKey.setPromptText("Type Latitude & Longitude Pair Here Separated By Comma, Then Press Enter Key");
//        searchKey.setOnKeyPressed(event40 -> {

            //String zip = "";
            String latitude = "";
            String longitude = "";
            String key = "";
            String zip = "";

            /**
             * Button Controls to locate Nearby Hospitals
             */

            geoBtn = new Button();
            geoBtn.setText("Locate Nearby Hospitals");
            geoBtn.setOnMouseClicked((e41) -> {

                /**
                 * Find IP address(location)
                 */

                try (WebServiceClient client = new WebServiceClient.Builder(118280, "A5P4ZajQ983z")
                        .build()) {

                    try (java.util.Scanner s = new java.util.Scanner(new java.net.URL("https://api.ipify.org").openStream(), "UTF-8").useDelimiter("\\A")) {
                        InetAddress ipAddress = InetAddress.getByName(s.next());
                        //System.out.println("My current IP address is " + s.next());

                        // Do the lookup
                        InsightsResponse response = client.insights(ipAddress);

                        Location location = response.getLocation();
                        System.out.println(location.getLatitude());        // 44.9733
                        System.out.println(location.getLongitude());       // -93.2323

                        ipLat = String.valueOf(location.getLatitude()).trim();
                        ipLong = String.valueOf(location.getLongitude()).trim();

                        List<Hospital> mileage = new ArrayList<Hospital>();
                        List<Hospital> graphical = new ArrayList<Hospital>();
                        slid.valueProperty().addListener((ov, old_val, new_val) -> {
                            radiusDist = new_val.doubleValue();
                        });

                        /**
                         *  Creates a new min heap
                         */
                        //Heap<Hospital> minHeap = new Heap<Hospital>(150);
                        MinHeap2<Hospital> mHeap = new MinHeap2<Hospital>();

                        String locName = "My Location";
                        myLocation = new Hospital(locName, ipLat, ipLong);
                        startVert = myLocation.getName();
                        hospitalGraph.addVertex(myLocation.getName());
                        for (Hospital hospital : sortedData) {
                            DecimalFormat df = new DecimalFormat("#.#");
                            df.setRoundingMode(RoundingMode.DOWN);
                            double distance = (int) haversineDistance(ipLat, hospital.getLatitude().trim(), ipLong, hospital.getLongitude().trim());

                            if (distance <= radiusDist) {
                                hospital.setDistance(distance);
                                hospitalGraph.addVertex(hospital.getName());
                                hospitalGraph.addEdge(myLocation.getName(), hospital.getName(), (int) distance);
                                finalVert = hospital.getName();
                                mHeap.enqueue(hospital);
                                ///minHeap.enqueue(hospital);
                                mileage.add(mHeap.dequeue());
                                graphical.add(hospital);
                                for(Hospital graphHos : graphical){
                                    double edgeDistance = (int) haversineDistance((mHeap.dequeue()).getLatitude().trim(), graphHos.getLatitude().trim(), (mHeap.dequeue()).getLongitude().trim(), graphHos.getLongitude().trim());
                                    hospitalGraph.addEdge(mHeap.dequeue().getName(), graphHos.getName(), (int) edgeDistance);
                                }
                                //mileage.add(minHeap.dequeue());
                                //System.out.println(mHeap.dequeue());
                            }
                            //System.out.println(distance);

                        }

                        class DistanceComparator implements Comparator<Hospital> {

                            @Override
                            public int compare(Hospital o1, Hospital o2) {
                                return new CompareToBuilder()
                                        .append(o1.getDistance(), o2.getDistance())
                                        .append(o1.getZip(), o2.getZip())
                                        .append(o1.getLatitude(), o2.getLatitude())
                                        .append(o1.getLongitude(), o2.getLongitude())
                                        .append(o1.getName(), o2.getName())
                                        .append(o1.getStreetAddress(), o2.getStreetAddress())
                                        .append(o1.getCity(), o2.getCity())
                                        .append(o1.getState(), o2.getState())
                                        .append(o1.getPhone(), o2.getPhone())
                                        .append(o1.getWebsite(), o2.getWebsite()).toComparison();
                            }
                        }
                        Collections.sort(mileage, new DistanceComparator());
                        shortestPaths(hospitalGraph, startVert);
                        hospitalGraph.printGraph();
                        System.out.println(isPath2(hospitalGraph, startVert, finalVert));
                        System.out.println(finalVert);
                        ObservableList<Hospital> searchHospitalResultTable = FXCollections.observableArrayList(mileage);
                        distCol.setVisible(true);
                        table.setItems(searchHospitalResultTable);
                        //System.out.println(hospitalGraph);
                        //System.out.println(mHeap);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (GeoIp2Exception e) {
                        e.printStackTrace();
                    }
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }

            });

            /**
             * Selects entire row by double clicking
             * Returns Hospital Object
             * http://stackoverflow.com/questions/30191264/javafx-tableview-how-to-get-the-row-i-clicked
             * author - James_D
             */

            table.setRowFactory(tv -> {
                TableRow<Hospital> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY
                            && event.getClickCount() == 2) {
                        count++;
                        Hospital clickedRow = row.getItem();
                        String tableKey = String.valueOf(clickedRow);
                        String latKey = tableKey.split("Latitude: ")[1].split(" ")[0];
                        String longKey = tableKey.split("Longitude: ")[1].split(" ")[0];
                        Hospital hospitalKey = new Hospital(latKey, longKey);
                        //System.out.println(clickedRow);
                    System.out.println(latKey);
                    System.out.println(longKey);

                        /**JSON Parser
                         *
                         */
                        String jsonData = "";
                        String latlong = latKey + "," + longKey;
                        String postalCode;
                        try {
                            URL googly = new URL("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latlong.trim() + "&key=AIzaSyD9eLR6AMkq5AtNh9t8rb4-frawvTdamUk");
                            URLConnection webCon = googly.openConnection();
                            BufferedReader input = new BufferedReader(new InputStreamReader(webCon.getInputStream()));
                            String inputLine;

                            while ((inputLine = input.readLine()) != null)
                                jsonData += inputLine + "\n";
                            input.close();

                            JSONObject dataObject = new JSONObject(jsonData);
                            JSONArray resultsArray = dataObject.getJSONArray("results");
                            JSONObject addressComponentsObject = resultsArray.getJSONObject(0);
                            JSONArray addressComponentsArray = addressComponentsObject.getJSONArray("address_components");
                            JSONObject postalCodeObject = addressComponentsArray.getJSONObject(addressComponentsArray.length() - 1);
                            JSONArray postalCodeTypeArray = postalCodeObject.getJSONArray("types");
                            String postalCodeType = postalCodeTypeArray.getString(0).trim();
                            switch (postalCodeType) {
                                case "postal_code":
                                    postalCode = postalCodeObject.getString("long_name");
                                    break;
                                case "postal_code_suffix":
                                    postalCodeObject = addressComponentsArray.getJSONObject(addressComponentsArray.length() - 2);
                                    postalCode = postalCodeObject.getString("long_name");
                                    break;
                                default:
                                    postalCode = "wrong data input";
                            }

                            /**
                             * ImageView Controller
                             */
                            String key1 = "";

                            key1 = String.valueOf(tableKey);
                            String photoLink = key1.split("~")[1];
                            //System.out.println(photoLink);
                            imv = new ImageView();

                            imv = ImageViewBuilder.create()
                                    .image(new Image(photoLink.trim()))
                                    .build();
                            imv.setFitHeight(300);
                            imv.setFitWidth(300);
                            imv.setPreserveRatio(false);


//
                            /**
                             * Hospital Details Pane
                             */
                            //String key2 = "";
                            String key2 = "";
                            String key3 = "";
                            Alert details = new Alert(Alert.AlertType.INFORMATION);
                            details.setTitle("\t\t\t\tHospital Details");
                            key2 = String.valueOf(tableKey);
                            String nameLink = key2.split("\n\n")[0];
                            details.setHeaderText(nameLink.trim() + "");
                            details.setResizable(true);

                            /**
                             * Hyperlink
                             */
                            VBox vb = new VBox();
                            String secondary = (key2.split("\n\n")[1]).split("\t\n")[0];
                            String thrice = (key1.split("`")[0]).split("\t\n")[1];
                            String content2 = secondary + " " + postalCode + "\n" + thrice;
                            Label lbl = new Label(content2);
                            String quad = (key1.split("~")[0]).split("`")[1];
                            System.out.println(quad);
                            Hyperlink link4 = new Hyperlink(quad);
                            vb.setStyle("-fx-background-color: rgba(0, 100, 100, 0.2); -fx-background-radius: 1;");
                            link4.setOnMouseClicked(e38 -> getHostServices().showDocument(quad));
                            vb.getChildren().addAll(lbl, link4);
                            details.getDialogPane().contentProperty().set(vb);
                            details.setGraphic(imv);
                            details.showAndWait();

                        } catch (Exception e) {
                            System.out.print("Invalid lat and long coordinate\n");
                        }
                    }
                });
                return row;
            });

            /**
             * Refresh Button Controller
             */
            btn = new Button();
            btn.setText("Reset Table");
            btn.setOnMouseClicked((e35) -> {
                searchKey.setText(null);
                searchKey.setPromptText("Type Any Letter or Number Key Here To Refine Hospital Search");

                distCol.setVisible(false);
                table.setItems(sortedData);


            });
            tPain.getChildren().addAll(slid, geoBtn, btn);
            box.getChildren().addAll(lb, tPain);
            background.setDividerPositions(0.3f, 0.6f);
            background.getItems().addAll(searchKey, box, table);
            Scene converter = new Scene(background, 1075, 600);
            background.prefWidthProperty().bind(converter.widthProperty());
            background.prefHeightProperty().bind(converter.heightProperty());

            /**
             * Primary Stage Controls
             */
            primaryStage.setScene(converter);
            primaryStage.showAndWait();
            //primaryStage.setOnCloseRequest(e36 -> Platform.exit());
        }

    }

    public void loadHospital() {
        hospitalBSTree = new BinarySearchTree<Hospital>();
        List hospitalList = null;

        try {
            //hospitalList = ReadExcel.excelReader("/home/dean/Documents/School/NVCC/Summer_Sem_16/CSC_201/IntelliJ/IdeaProjects/AssignmentTwo/src/Excel/HospitalList2.xls");
            hospitalList = ReadExcel.excelReader("/home/dextop/Desktop/LoD_Share/Internal Storage/Source Code/Code/idea-IU-173.4127.27/bin/IntelliJ/IdeaProjects/AssignmentTwo/src/HospitalList2.xls");
                    //"/home/dean/Documents/School/NVCC/2018/Code/idea-IU-173.4127.27/bin/IntelliJ/IdeaProjects/AssignmentTwo/src/Excel/HospitalList2.xls");

        } catch (Exception e) {
            System.err.println("Error reading Objects.Hospital List");
            e.printStackTrace();
        }
        class HospitalComparator implements Comparator<Hospital> {

            @Override
            public int compare(Hospital o1, Hospital o2) {

                return new CompareToBuilder()
                        .append(o1.getName(), o2.getName())
                        .append(o1.getZip(), o2.getZip())
                        .append(o1.getLatitude(), o2.getLatitude())
                        .append(o1.getLongitude(), o2.getLongitude())
                        .append(o1.getState(), o2.getState())
                        .append(o1.getStreetAddress(), o2.getStreetAddress())
                        .append(o1.getCity(), o2.getCity())
                        .append(o1.getPhone(), o2.getPhone())
                        .append(o1.getWebsite(), o2.getWebsite()).toComparison();
            }
        }

        class DistanceComparator implements Comparator<Hospital> {

            @Override
            public int compare(Hospital o1, Hospital o2) {
                return new CompareToBuilder()

                        .append(o1.getDistance(), o2.getDistance())
                        .append(o1.getZip(), o2.getZip())
                        .append(o1.getLatitude(), o2.getLatitude())
                        .append(o1.getLongitude(), o2.getLongitude())
                        .append(o1.getName(), o2.getName())
                        .append(o1.getStreetAddress(), o2.getStreetAddress())
                        .append(o1.getCity(), o2.getCity())
                        .append(o1.getState(), o2.getState())
                        .append(o1.getPhone(), o2.getPhone())
                        .append(o1.getWebsite(), o2.getWebsite()).toComparison();
            }
        }

        for (int i = 0; i < hospitalList.size(); i++) {
            List record = (List) hospitalList.get(i);
            name = String.valueOf(record.get(0));
            streetAddress = String.valueOf(record.get(1));
            city = String.valueOf(record.get(2));
            state = String.valueOf(record.get(3));
            zip = String.valueOf(record.get(4));
            distance = 0.0;
            latitude = String.valueOf(record.get(6));
            longitude = String.valueOf(record.get(7));
            phone = String.valueOf(record.get(8));
            photo = String.valueOf(record.get(9));
            website = String.valueOf(record.get(10));

            Hospital hospital = new Hospital(name, streetAddress, city, state, zip, distance, latitude, longitude, phone, photo, website);
            hospitalBSTree.add(hospital);
            hospitalData.add(hospital);
            Collections.sort(hospitalData, new HospitalComparator());
        }
    }

    public static double haversineDistance(String lat1, String lat2, String long1, String long2) {
        // TODO Auto-generated method stub
        final int R = 3959; // Radius of the earth
        Double la1 = Double.parseDouble(lat1);
        Double lo1 = Double.parseDouble(long1);
        Double la2 = Double.parseDouble(lat2);
        Double lo2 = Double.parseDouble(long2);
        Double latDistance = toRad(la2 - la1);
        Double lonDistance = toRad(lo2 - lo1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(toRad(la1)) * Math.cos(toRad(la2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        Double distance = R * c;

        //System.out.println("The distance between the two lat and long values is: " + distance + " miles.");

        return distance;
    }

    private static Double toRad(Double value) {
        return value * Math.PI / 180;
    }

    public void shortestPaths(WeightedGraphInterface<String> hospitalGraph, String startVertex) {
        Hospital2 hospitals;
        Hospital2 saveHospital2;         // for saving on priority queue
        int minDistance;
        int newDistance;

        PriQueueInterface<Hospital2> pq = new Heap<Hospital2>(150);   // Assume at most 150 vertices
        String vertex;

        hospitalGraph.clearMarks();
        saveHospital2 = new Hospital2(startVert, startVert, 0);
        pq.enqueue(saveHospital2);

//        System.out.println("Last Vertex   Destination   Distance");
//        System.out.println("------------------------------------");
        List<Hospital2> graphList = new ArrayList<Hospital2>();
        do {
            hospitals = pq.dequeue();
            if (!hospitalGraph.isMarked(hospitals.getToVertex())) {
                hospitalGraph.markVertex(hospitals.getToVertex());
                graphList.add(hospitals);
                //System.out.println(hospitals);
                hospitals.setFromVertex(hospitals.getToVertex());
                minDistance = hospitals.getDistance();
                vertexQueue = hospitalGraph.getToVertices(hospitals.getFromVertex());
                while (!vertexQueue.isEmpty()) {
                    vertex = vertexQueue.dequeue();
                    if (!hospitalGraph.isMarked(vertex)) {
                        newDistance = minDistance
                                + hospitalGraph.weightIs(hospitals.getFromVertex(), vertex);
                        saveHospital2 = new Hospital2(hospitals.getFromVertex(), vertex, newDistance);
                        pq.enqueue(saveHospital2);
                    }
                }
            }
        } while (!pq.isEmpty());
        ObservableList<Hospital2> shortestPathResults = FXCollections.observableArrayList(graphList);
        Alert graphPane = new Alert(Alert.AlertType.INFORMATION);
        graphPane.setTitle("\t\t      Hospital Seeker");
        graphPane.setHeaderText("\t\t\t\tGraph Details");
        graphPane.setResizable(true);
        VBox vb = new VBox();
        String secondary = "  Destination       Distance";
        String thrice = "-----------------------------------------------------------------";
        String quad = String.valueOf(shortestPathResults);
        String content2 = secondary + "\n" + thrice + "\n" + quad;
        Label lbl = new Label(content2);
        vb.setStyle("-fx-background-color: rgba(0, 100, 100, 0.2); -fx-background-radius: 1;");
        vb.getChildren().addAll(lbl);
        graphPane.getDialogPane().contentProperty().set(vb);
        //graphPane.setGraphic(imv);
        graphPane.showAndWait();
    }

    private static boolean isPath2(WeightedGraphInterface<String> graph, String startVertex, String endVertex)

    // Returns true if a path exists on graph, from startVertex to endVertex;
    // otherwise returns false. Uses breadth-first search algorithm.
    {
        UnboundedQueueInterface<String> queue = new LinkedUnbndQueue<String>();
        UnboundedQueueInterface<String> vertexQueue = new LinkedUnbndQueue<String>();

        boolean found = false;
        String vertex;
        String item;

        graph.clearMarks();
        queue.enqueue(startVertex);
        do
        {
            vertex = queue.dequeue();
            if (vertex == endVertex)
                found = true;
            else
            {
                if (!graph.isMarked(vertex))
                {
                    graph.markVertex(vertex);
                    vertexQueue = graph.getToVertices(vertex);

                    while (!vertexQueue.isEmpty())
                    {
                        item = vertexQueue.dequeue();
                        if (!graph.isMarked(item))
                            queue.enqueue(item);
                    }
                }
            }
        } while (!queue.isEmpty() && !found);

        return found;
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    private class DistanceComparator {
    }
}
