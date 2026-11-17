package com.tradeshield.ui;

import com.tradeshield.model.Order;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Builds the Stage 1 dashboard using standard JavaFX layout classes.
 *
 * <p>The values are display-only placeholders. Future stages will replace them
 * with simulated order, pricing, and risk data.</p>
 */
public class DashboardView {

    private final ObservableList<Order> submittedOrders = FXCollections.observableArrayList();
    private final TextField symbolField = new TextField();
    private final ComboBox<Order.OrderType> orderTypeBox = new ComboBox<>();
    private final TextField quantityField = new TextField();
    private final TextField priceField = new TextField();
    private final Label orderMessage = new Label();
    private int nextOrderNumber = 1;

    /**
     * Creates the complete dashboard layout.
     *
     * @return the root node for the JavaFX scene
     */
    public BorderPane create() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("app-shell");
        root.setLeft(createSidebar());

        ScrollPane workspaceScrollPane = new ScrollPane(createWorkspace());
        workspaceScrollPane.setFitToWidth(true);
        workspaceScrollPane.getStyleClass().add("dashboard-scroll");
        root.setCenter(workspaceScrollPane);
        return root;
    }

    /** Builds the left navigation area. */
    private VBox createSidebar() {
        Label brand = new Label("TradeShield");
        brand.getStyleClass().add("brand");

        Label subtitle = new Label("ORDER SAFETY CONSOLE");
        subtitle.getStyleClass().add("brand-subtitle");

        VBox navigation = new VBox(8,
                navigationButton("Dashboard", true),
                navigationButton("Simulated Orders", false),
                navigationButton("Risk Controls", false),
                navigationButton("Audit History", false),
                navigationButton("Replay Mode", false)
        );
        navigation.getStyleClass().add("navigation");

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Label environment = new Label("SIMULATION ENVIRONMENT");
        environment.getStyleClass().add("environment-label");

        VBox sidebar = new VBox(8, brand, subtitle, navigation, spacer, environment);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPadding(new Insets(32, 22, 28, 22));
        return sidebar;
    }

    /** Builds the central dashboard area. */
    // Yashika - Dashboard UI Enhancement
    private VBox createWorkspace() {
        Label greeting = new Label("Good morning, Analyst");
        greeting.getStyleClass().add("page-title");

        Label description = new Label("Monitor simulated orders and keep portfolio risk within guardrails.");
        description.getStyleClass().add("page-description");

        VBox heading = new VBox(6, greeting, description);

        HBox metrics = new HBox(18,
                metricCard("Protected balance", "₹2,50,000.00", "Available for simulated orders"),
                metricCard("Risk exposure", "18.4%", "Within the 25% session limit"),
                metricCard("Orders reviewed", "24", "No unresolved safety alerts")
        );
        metrics.getStyleClass().add("metric-row");

        HBox contentPanels = new HBox(18, createSafetyPanel(), createMarketPanel());
        HBox.setHgrow(contentPanels.getChildren().getFirst(), Priority.ALWAYS);
        HBox.setHgrow(contentPanels.getChildren().getLast(), Priority.ALWAYS);

        VBox activityPanel = createActivityPanel();

        VBox workspace = new VBox(24, heading, metrics, createOrderManagementPanel(), contentPanels, activityPanel);
        workspace.getStyleClass().add("workspace");
        workspace.setPadding(new Insets(36));
        return workspace;
    }

    /** Creates the order entry form and the in-memory order table. */
    private VBox createOrderManagementPanel() {
        Label title = new Label("Simulated order management");
        title.getStyleClass().add("panel-title");

        Label description = new Label("Enter an order to add it to this session's simulated order book.");
        description.getStyleClass().add("panel-description");

        configureOrderForm();

        HBox form = new HBox(12,
                formField("Stock symbol", symbolField),
                formField("Side", orderTypeBox),
                formField("Quantity", quantityField),
                formField("Price", priceField),
                createSubmitButton()
        );
        form.getStyleClass().add("order-form");
        form.setAlignment(Pos.BOTTOM_LEFT);

        orderMessage.getStyleClass().add("order-message");
        orderMessage.setText("No orders submitted in this session yet.");

        TableView<Order> orderTable = createOrderTable();

        VBox panel = new VBox(12, title, description, form, orderMessage, orderTable);
        panel.getStyleClass().add("content-panel");
        return panel;
    }

    /** Sets prompts and the default BUY choice for the simple order form. */
    // Yashika - Simulated Order Form
    private void configureOrderForm() {
        symbolField.setPromptText("e.g. RELIANCE");
        quantityField.setPromptText("e.g. 10");
        priceField.setPromptText("e.g. 2941.70");

        orderTypeBox.getItems().setAll(Order.OrderType.values());
        orderTypeBox.setValue(Order.OrderType.BUY);
    }

    /** Wraps a form control with its short label. */
    private VBox formField(String labelText, Node field) {
        Label label = new Label(labelText.toUpperCase());
        label.getStyleClass().add("form-label");

        VBox fieldBox = new VBox(6, label, field);
        fieldBox.getStyleClass().add("form-field");
        HBox.setHgrow(fieldBox, Priority.ALWAYS);
        return fieldBox;
    }

    /** Creates the button that starts local validation and order submission. */
    private Button createSubmitButton() {
        Button submitButton = new Button("Submit Order");
        submitButton.getStyleClass().add("primary-button");
        submitButton.setOnAction(event -> submitOrder());
        return submitButton;
    }

    // Yashika - Orders Table
    /** Creates the table columns used to display orders accepted in this session. */
    private TableView<Order> createOrderTable() {
        TableColumn<Order, String> idColumn = new TableColumn<>("Order ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));

        TableColumn<Order, String> symbolColumn = new TableColumn<>("Symbol");
        symbolColumn.setCellValueFactory(new PropertyValueFactory<>("stockSymbol"));

        TableColumn<Order, Order.OrderType> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("orderType"));

        TableColumn<Order, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Order, String> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(order -> new SimpleStringProperty(
                String.format("₹%,.2f", order.getValue().getPrice())
        ));

        TableColumn<Order, Order.OrderStatus> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableView<Order> orderTable = new TableView<>(submittedOrders);
        orderTable.getColumns().addAll(idColumn, symbolColumn, typeColumn, quantityColumn, priceColumn, statusColumn);
        orderTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        orderTable.setPrefHeight(210);
        orderTable.setPlaceholder(new Label("Accepted simulated orders will appear here."));
        orderTable.getStyleClass().add("order-table");
        return orderTable;
    }

    /**
     * Validates the order form and adds one ACCEPTED order when every value is valid.
     * Invalid values show an error and leave the table unchanged.
     */
    private void submitOrder() {
        String symbol = symbolField.getText().trim().toUpperCase();
        String quantityText = quantityField.getText().trim();
        String priceText = priceField.getText().trim();

        // Yashika - Order Input Validation
        if (symbol.isEmpty() || quantityText.isEmpty() || priceText.isEmpty() || orderTypeBox.getValue() == null) {
            showOrderError("Please complete every order field.");
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityText);
            double price = Double.parseDouble(priceText);

            if (quantity <= 0) {
                showOrderError("Quantity must be greater than zero.");
                return;
            }

            if (!Double.isFinite(price) || price <= 0) {
                showOrderError("Price must be greater than zero.");
                return;
            }

            // Yashika - Order Submission
            Order order = new Order(
                    createOrderId(),
                    symbol,
                    orderTypeBox.getValue(),
                    quantity,
                    price,
                    Order.OrderStatus.ACCEPTED
            );
            submittedOrders.add(order);
            clearOrderForm();
            showOrderSuccess("Order " + order.getOrderId() + " accepted for simulation.");
        } catch (NumberFormatException exception) {
            showOrderError("Quantity must be a whole number and price must be a number.");
        }
    }

    /** Creates readable, increasing identifiers for this running application session. */
    private String createOrderId() {
        return String.format("SIM-%04d", nextOrderNumber++);
    }

    /** Restores the form after a valid order is added to the table. */
    private void clearOrderForm() {
        symbolField.clear();
        quantityField.clear();
        priceField.clear();
        orderTypeBox.setValue(Order.OrderType.BUY);
    }

    /** Displays a red validation message without adding an order. */
    private void showOrderError(String message) {
        orderMessage.setText(message);
        orderMessage.getStyleClass().remove("order-message-success");
        if (!orderMessage.getStyleClass().contains("order-message-error")) {
            orderMessage.getStyleClass().add("order-message-error");
        }
    }

    /** Displays a green confirmation after a valid order is added. */
    private void showOrderSuccess(String message) {
        orderMessage.setText(message);
        orderMessage.getStyleClass().remove("order-message-error");
        if (!orderMessage.getStyleClass().contains("order-message-success")) {
            orderMessage.getStyleClass().add("order-message-success");
        }
    }

    /** Creates a summary card with a label, value, and supporting text. */
    private VBox metricCard(String title, String value, String detail) {
        Label titleLabel = new Label(title.toUpperCase());
        titleLabel.getStyleClass().add("metric-title");

        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("metric-value");

        Label detailLabel = new Label(detail);
        detailLabel.getStyleClass().add("metric-detail");

        VBox card = new VBox(10, titleLabel, valueLabel, detailLabel);
        card.getStyleClass().add("metric-card");
        HBox.setHgrow(card, Priority.ALWAYS);
        card.setMaxWidth(Double.MAX_VALUE);
        return card;
    }

    /** Creates the stage-one safety status panel. */
    private VBox createSafetyPanel() {
        Label title = new Label("Safety status");
        title.getStyleClass().add("panel-title");

        HBox status = new HBox(10, statusDot(), new Label("All protection systems are standing by"));
        status.setAlignment(Pos.CENTER_LEFT);
        status.getStyleClass().add("status-line");

        Label limitLabel = new Label("Daily simulated order limit");
        limitLabel.getStyleClass().add("muted-label");

        HBox limitValues = new HBox();
        Label used = new Label("₹46,000 used");
        Label total = new Label("₹2,50,000 limit");
        total.getStyleClass().add("muted-label");
        Region limitSpacer = new Region();
        HBox.setHgrow(limitSpacer, Priority.ALWAYS);
        limitValues.getChildren().addAll(used, limitSpacer, total);

        ProgressBar limitProgress = new ProgressBar(0.184);
        limitProgress.setMaxWidth(Double.MAX_VALUE);
        limitProgress.getStyleClass().add("risk-progress");

        Button comingSoon = new Button("Order entry arrives in Stage 2");
        comingSoon.getStyleClass().add("secondary-button");
        comingSoon.setDisable(true);

        VBox panel = new VBox(18, title, status, limitLabel, limitValues, limitProgress, comingSoon);
        panel.getStyleClass().add("content-panel");
        panel.setMaxWidth(Double.MAX_VALUE);
        return panel;
    }

    /** Creates a display-only market watch panel. */
    private VBox createMarketPanel() {
        Label title = new Label("Market watch");
        title.getStyleClass().add("panel-title");

        VBox quotes = new VBox(12,
                quoteRow("RELIANCE", "₹2,941.70", "+0.62%"),
                quoteRow("TCS", "₹4,126.35", "+0.18%"),
                quoteRow("INFY", "₹1,782.40", "−0.31%")
        );

        Label note = new Label("Quotes are sample data for the dashboard layout.");
        note.getStyleClass().add("muted-label");

        VBox panel = new VBox(16, title, quotes, note);
        panel.getStyleClass().add("content-panel");
        panel.setMaxWidth(Double.MAX_VALUE);
        return panel;
    }

    /** Creates the lower audit preview panel. */
    private VBox createActivityPanel() {
        Label title = new Label("Recent safety activity");
        title.getStyleClass().add("panel-title");

        VBox rows = new VBox(10,
                activityRow("Price freshness check", "Ready to validate incoming prices", "READY"),
                activityRow("Duplicate-order guard", "Waiting for simulated order identifiers", "READY"),
                activityRow("Emergency kill switch", "No stop signal is active", "SAFE")
        );

        VBox panel = new VBox(16, title, rows);
        panel.getStyleClass().add("content-panel");
        return panel;
    }

    /** Creates one row for the market watch panel. */
    private HBox quoteRow(String symbol, String price, String change) {
        Label symbolLabel = new Label(symbol);
        symbolLabel.getStyleClass().add("quote-symbol");
        Label priceLabel = new Label(price);
        Label changeLabel = new Label(change);
        changeLabel.getStyleClass().add(change.startsWith("+") ? "positive-change" : "negative-change");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox row = new HBox(18, symbolLabel, spacer, priceLabel, changeLabel);
        row.getStyleClass().add("quote-row");
        return row;
    }

    /** Creates one row for the activity preview. */
    private HBox activityRow(String name, String detail, String state) {
        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add("activity-name");
        Label detailLabel = new Label(detail);
        detailLabel.getStyleClass().add("activity-detail");
        VBox text = new VBox(3, nameLabel, detailLabel);

        Label stateLabel = new Label(state);
        stateLabel.getStyleClass().add("state-badge");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox row = new HBox(12, text, spacer, stateLabel);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("activity-row");
        return row;
    }

    /** Creates one navigation button. The active page uses a highlighted style. */
    private Button navigationButton(String text, boolean active) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.getStyleClass().add(active ? "navigation-button-active" : "navigation-button");
        return button;
    }

    /** Creates the green indicator used for the safe status. */
    private Node statusDot() {
        Circle circle = new Circle(5, Color.web("#39d98a"));
        circle.getStyleClass().add("status-dot");
        return circle;
    }
}
