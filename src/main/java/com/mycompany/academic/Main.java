package com.mycompany.academic;

import com.mycompany.academic.Model.Rating;
import com.mycompany.academic.exception.AuthenticationException;
import com.mycompany.academic.Model.CustomRequest;
import com.mycompany.academic.Model.Material;
import com.mycompany.academic.user.SeniorStudent;
import com.mycompany.academic.user.Student;
import com.mycompany.academic.user.User;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.ArrayList;

public class Main extends Application {
    private AppSystem system = new AppSystem();

    @Override
    public void start(Stage primaryStage) {
        showLoginScene(primaryStage);
    }

    private void showLoginScene(Stage stage) {
        stage.setTitle("Ruang Belajar - Masuk");

        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        layout.setPrefSize(400, 420);

        Label title = new Label("Ruang Belajar");
        title.getStyleClass().add("label-header");

        Label subtitle = new Label("Silakan masuk menggunakan akun Anda");
        subtitle.getStyleClass().add("label-sub");

        TextField emailField = new TextField();
        emailField.setPromptText("Masukkan Email");

        PasswordField pwField = new PasswordField();
        pwField.setPromptText("Masukkan Password");

        Button loginBtn = new Button("Masuk Sistem");
        loginBtn.setMaxWidth(Double.MAX_VALUE);

        Button registerLinkBtn = new Button("Belum punya akun? Daftar");
        registerLinkBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #3b82f6; -fx-cursor: hand; -fx-underline: true;");
        registerLinkBtn.setOnAction(e -> showRegisterScene(stage));

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #ef4444; -fx-font-size: 11px;");

        layout.getChildren().addAll(title, subtitle, emailField, pwField, loginBtn, registerLinkBtn, errorLabel);

        loginBtn.setOnAction(e -> {
            try {
                User user = system.login(emailField.getText(), pwField.getText());
                showDashboardScene(stage, user);
            } catch (AuthenticationException ex) {
                errorLabel.setText(ex.getMessage());
            }
        });

        applyThemeAndShow(stage, layout);
    }

    private void showRegisterScene(Stage stage) {
        VBox layout = new VBox(12);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        layout.setPrefSize(400, 520);

        Label title = new Label("Pendaftaran Akun");
        title.getStyleClass().add("label-header");

        TextField nameField = new TextField();
        nameField.setPromptText("Nama Lengkap");

        TextField emailField = new TextField();
        emailField.setPromptText("Alamat Email");

        PasswordField pwField = new PasswordField();
        pwField.setPromptText("Kata Sandi Baru");

        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll("Siswa (Student)", "Senior Student (Mentor)");
        roleCombo.setValue("Siswa (Student)");
        roleCombo.setMaxWidth(Double.MAX_VALUE);

        ComboBox<String> majorCombo = new ComboBox<>();
        majorCombo.getItems().addAll("RPL", "DKV");
        majorCombo.setValue("RPL");
        majorCombo.setMaxWidth(Double.MAX_VALUE);

        ComboBox<Integer> semCombo = new ComboBox<>();
        semCombo.getItems().addAll(1, 2, 3, 4, 5, 6);
        semCombo.setValue(1);
        semCombo.setMaxWidth(Double.MAX_VALUE);

        roleCombo.setOnAction(e -> semCombo.setVisible(roleCombo.getValue().equals("Siswa (Student)")));

        Button submitBtn = new Button("Daftar Sekarang");
        submitBtn.setMaxWidth(Double.MAX_VALUE);

        Button backBtn = new Button("Kembali ke Login");
        backBtn.getStyleClass().add("button-danger");
        backBtn.setMaxWidth(Double.MAX_VALUE);
        backBtn.setOnAction(e -> showLoginScene(stage));

        Label infoLabel = new Label();

        layout.getChildren().addAll(title, nameField, emailField, pwField, roleCombo, majorCombo, semCombo, submitBtn, backBtn, infoLabel);

        submitBtn.setOnAction(e -> {
            try {
                system.register(
                    nameField.getText(),
                    emailField.getText(),
                    pwField.getText(),
                    roleCombo.getValue(),
                    majorCombo.getValue(),
                    semCombo.getValue()
                );
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sukses");
                alert.setContentText("Registrasi Berhasil! Silakan masuk.");
                alert.showAndWait();
                showLoginScene(stage);
            } catch (Exception ex) {
                infoLabel.setText(ex.getMessage());
                infoLabel.setStyle("-fx-text-fill: red;");
            }
        });

        applyThemeAndShow(stage, layout);
    }

    private void showDashboardScene(Stage stage, User user) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setPrefSize(800, 680);

        Label header = new Label("Dashboard Utama");
        header.getStyleClass().add("label-header");

        VBox card = new VBox(5);
        card.getStyleClass().add("card");
        Label info = new Label(user.getDashboardInfo());
        info.setStyle("-fx-font-weight: bold; -fx-text-fill: #1e293b;");
        card.getChildren().add(info);

        VBox contentBox = new VBox(10);
        contentBox.getStyleClass().add("card");

        if (user.getRole().equals("senior_student")) {
            HBox uploadRow = new HBox(10);
            uploadRow.setAlignment(Pos.CENTER_LEFT);
            uploadSection(uploadRow, user, stage);
            contentBox.getChildren().addAll(new Label("Unggah Materi Baru (Menunggu Validasi):"), uploadRow);
        }

        if (user.getRole().equals("admin")) {
            renderAdminValidation(contentBox, stage, user);
        }

        renderCustomRequestSection(contentBox, user, stage);

        Label listLabel = new Label("Daftar Materi Aktif:");
        listLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        contentBox.getChildren().add(listLabel);

        FlowPane materialsGrid = new FlowPane();
        materialsGrid.setHgap(12);
        materialsGrid.setVgap(12);

        for (Material m : system.getPublishedMaterials()) {
            VBox mCard = new VBox(5);
            mCard.getStyleClass().add("card");
            mCard.setPrefWidth(165);

            StackPane thumb = new StackPane();
            thumb.setPrefSize(145, 70);
            Label iconLbl = new Label();
            if (m.getFilePath().endsWith(".pdf")) {
                thumb.setStyle("-fx-background-color: #fee2e2; -fx-background-radius: 6;");
                iconLbl.setText("PDF");
                iconLbl.setStyle("-fx-text-fill: #991b1b; -fx-font-weight: bold; -fx-font-size: 11px;");
            } else if (m.getFilePath().endsWith(".pptx")) {
                thumb.setStyle("-fx-background-color: #ffedd5; -fx-background-radius: 6;");
                iconLbl.setText("PPTX");
                iconLbl.setStyle("-fx-text-fill: #9a3412; -fx-font-weight: bold; -fx-font-size: 11px;");
            } else {
                thumb.setStyle("-fx-background-color: #dbeafe; -fx-background-radius: 6;");
                iconLbl.setText("IMAGE");
                iconLbl.setStyle("-fx-text-fill: #1e40af; -fx-font-weight: bold; -fx-font-size: 11px;");
            }
            thumb.getChildren().add(iconLbl);

            Label mTitle = new Label(m.getTitle());
            mTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 11px;");
            mTitle.setWrapText(true);
            mTitle.setMaxWidth(145);

            Button actionBtn = new Button();
            actionBtn.setMaxWidth(Double.MAX_VALUE);
            actionBtn.setStyle("-fx-font-size: 9px;");

            boolean access = system.hasAccess(user, m);
            String premiumStatus = system.getPremiumRequestStatus(user, m);

            if (access) {
                actionBtn.setText("Buka Materi →");
                actionBtn.setOnAction(e -> showDetailScene(stage, user, m));
            } else {
                if (premiumStatus != null && premiumStatus.equals("unpaid")) {
                    actionBtn.setText("Bayar QRIS ");
                    actionBtn.setStyle("-fx-background-color: #d97706; -fx-text-fill: white; -fx-font-size: 9px;");
                    actionBtn.setOnAction(e -> showQRISSceneForMaterial(stage, (Student) user, m));
                } else {
                    actionBtn.setText("Beli Akses ");
                    actionBtn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-font-size: 9px;");
                    actionBtn.setOnAction(e -> {
                        system.requestPremium((Student) user, m);
                        showDashboardScene(stage, user); 
                    });
                }
            }

            mCard.getChildren().addAll(thumb, mTitle, actionBtn);

            if (user.getRole().equals("admin")) {
                Button delBtn = new Button("Hapus");
                delBtn.getStyleClass().add("button-danger");
                delBtn.setStyle("-fx-font-size: 9px; -fx-padding: 3 6;");
                delBtn.setOnAction(e -> {
                    system.deleteMaterial(m.getId());
                    showDashboardScene(stage, user);
                });
                mCard.getChildren().add(delBtn);
            }

            materialsGrid.getChildren().add(mCard);
        }

        ScrollPane scroll = new ScrollPane(materialsGrid);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scroll.setPrefHeight(180);

        Button logout = new Button("Keluar");
        logout.getStyleClass().add("button-danger");
        logout.setOnAction(e -> showLoginScene(stage));

        layout.getChildren().addAll(header, card, contentBox, scroll, logout);
        applyThemeAndShow(stage, layout);
    }

    private void showQRISSceneForMaterial(Stage stage, Student student, Material m) {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(25));
        layout.setPrefSize(400, 520);

        Label title = new Label("BAYAR MATERI PREMIUM");
        title.getStyleClass().add("label-header");

        Label amountLabel = new Label("Total Pembayaran: Rp 25.000");
        amountLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ImageView qrCodeView = new ImageView();
        try {
            Image qrImage = new Image("https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=PremiumMaterialPaymentID-" + m.getId());
            qrCodeView.setImage(qrImage);
            qrCodeView.setFitWidth(200);
            qrCodeView.setFitHeight(200);
        } catch (Exception ignored) {}

        Label timerLabel = new Label("Sisa waktu pembayaran: 14:59");
        timerLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");

        Button payBtn = new Button("Simulasikan Pembayaran Berhasil ✓");
        payBtn.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-font-weight: bold;");
        payBtn.setMaxWidth(Double.MAX_VALUE);

        Button cancelBtn = new Button("Batal");
        cancelBtn.getStyleClass().add("button-danger");
        cancelBtn.setMaxWidth(Double.MAX_VALUE);
        cancelBtn.setOnAction(e -> showDashboardScene(stage, student));

        layout.getChildren().addAll(title, amountLabel, qrCodeView, timerLabel, payBtn, cancelBtn);

        payBtn.setOnAction(e -> {
            system.payPremium(student, m);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Lunas");
            alert.setContentText("Pembayaran Akses Premium Berhasil!");
            alert.showAndWait();
            showDashboardScene(stage, student);
        });

        applyThemeAndShow(stage, layout);
    }

    private void renderCustomRequestSection(VBox container, User user, Stage stage) {
        Label reqTitle = new Label("Sistem Request Materi Kustom ✍️");
        reqTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #1e3a8a;");
        container.getChildren().add(reqTitle);

        VBox reqBox = new VBox(5);

        if (user.getRole().equals("student")) {
            HBox actionHeader = new HBox(10);
            Button addReqBtn = new Button("+ Buat Request Baru");
            addReqBtn.setStyle("-fx-font-size: 10px;");
            
            addReqBtn.setOnAction(e -> showCustomRequestFormScene(stage, (Student) user));
            actionHeader.getChildren().add(addReqBtn);
            reqBox.getChildren().add(actionHeader);

            for (CustomRequest req : system.getCustomRequests()) {
                if (req.getStudent().getEmail().equals(user.getEmail())) {
                    HBox row = new HBox(15);
                    row.setAlignment(Pos.CENTER_LEFT);
                    Label reqLabel = new Label("- " + req.getTitle() + " (Budget: Rp " + req.getBudget() + ") [" + req.getStatus().toUpperCase() + "]");
                    reqLabel.setStyle("-fx-font-size: 11px;");
                    row.getChildren().add(reqLabel);

                    if (req.getStatus().equals("unpaid")) {
                        Button payBtn = new Button("Bayar via QRIS");
                        payBtn.setStyle("-fx-background-color: #d97706; -fx-text-fill: white; -fx-font-size: 10px;");
                        payBtn.setOnAction(e -> showQRISScene(stage, (Student) user, req));
                        row.getChildren().add(payBtn);
                    }
                    reqBox.getChildren().add(row);
                }
            }
        }

        if (user.getRole().equals("senior_student")) {
            for (CustomRequest req : system.getCustomRequests()) {
                if (req.getStatus().equals("open")) {
                    HBox row = new HBox(15);
                    row.setAlignment(Pos.CENTER_LEFT);
                    Label reqLabel = new Label("- [" + req.getStudent().getName() + "] " + req.getTitle() + " (Fee: Rp " + req.getBudget() + ")");
                    reqLabel.setStyle("-fx-font-size: 11px;");
                    
                    Button claimBtn = new Button("Ambil Request (Klaim)");
                    claimBtn.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-font-size: 10px;");
                    claimBtn.setOnAction(e -> {
                        system.claimRequest(req.getId(), (SeniorStudent) user);
                        showDashboardScene(stage, user);
                    });

                    row.getChildren().addAll(reqLabel, claimBtn);
                    reqBox.getChildren().add(row);
                }
            }
        }

        container.getChildren().add(reqBox);
    }

    private void showCustomRequestFormScene(Stage stage, Student student) {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        layout.setPrefSize(400, 450);

        Label title = new Label("Permintaan Materi Baru");
        title.getStyleClass().add("label-header");

        TextField titleField = new TextField();
        titleField.setPromptText("Materi apa yang Anda inginkan?");

        TextArea descArea = new TextArea();
        descArea.setPromptText("Tulis rincian penjelasannya...");
        descArea.setPrefHeight(100);

        TextField budgetField = new TextField();
        budgetField.setPromptText("Tawarkan Budget Anda (misal: 50000)");

        Button submitBtn = new Button("Posting Permintaan");
        submitBtn.setMaxWidth(Double.MAX_VALUE);

        Button backBtn = new Button("Kembali");
        backBtn.getStyleClass().add("button-danger");
        backBtn.setMaxWidth(Double.MAX_VALUE);
        backBtn.setOnAction(e -> showDashboardScene(stage, student));

        layout.getChildren().addAll(title, titleField, descArea, budgetField, submitBtn, backBtn);

        submitBtn.setOnAction(e -> {
            try {
                int budget = Integer.parseInt(budgetField.getText());
                system.createCustomRequest(student, titleField.getText(), descArea.getText(), budget);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Request Sukses");
                alert.setContentText("Request berhasil diposting!");
                alert.showAndWait();
                showDashboardScene(stage, student);
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Nominal budget harus berupa angka!");
                alert.showAndWait();
            }
        });

        applyThemeAndShow(stage, layout);
    }

    private void showQRISScene(Stage stage, Student student, CustomRequest req) {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(25));
        layout.setPrefSize(400, 520);

        Label title = new Label("QRIS SIMULATOR");
        title.getStyleClass().add("label-header");

        Label amountLabel = new Label("Total Tagihan: Rp " + req.getBudget());
        amountLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ImageView qrCodeView = new ImageView();
        try {
            Image qrImage = new Image("https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=CustomReqPaymentID-" + req.getId());
            qrCodeView.setImage(qrImage);
            qrCodeView.setFitWidth(200);
            qrCodeView.setFitHeight(200);
        } catch (Exception ignored) {}

        Label timerLabel = new Label("Sisa waktu pembayaran: 14:59");
        timerLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");

        Button payBtn = new Button("Simulasikan Pembayaran Berhasil ✓");
        payBtn.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-font-weight: bold;");
        payBtn.setMaxWidth(Double.MAX_VALUE);

        Button cancelBtn = new Button("Batal");
        cancelBtn.getStyleClass().add("button-danger");
        cancelBtn.setMaxWidth(Double.MAX_VALUE);
        cancelBtn.setOnAction(e -> showDashboardScene(stage, student));

        layout.getChildren().addAll(title, amountLabel, qrCodeView, timerLabel, payBtn, cancelBtn);

        payBtn.setOnAction(e -> {
            system.payRequest(req.getId());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Lunas");
            alert.setContentText("Pembayaran QRIS Simulasi Berhasil!");
            alert.showAndWait();
            showDashboardScene(stage, student);
        });

        applyThemeAndShow(stage, layout);
    }

    private void renderAdminValidation(VBox contentBox, Stage stage, User user) {
        Label pendingLabel = new Label("Antrean Validasi Materi Baru (" + system.getPendingMaterials().size() + "):");
        pendingLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #b45309;");
        contentBox.getChildren().add(pendingLabel);

        for (Material m : system.getPendingMaterials()) {
            HBox pRow = new HBox(12);
            pRow.setAlignment(Pos.CENTER_LEFT);

            Label mLabel = new Label("- " + m.getTitle() + " (" + m.getCourse().getTitle() + ")");
            mLabel.setStyle("-fx-font-size: 11px;");

            ComboBox<String> tierCombo = new ComboBox<>();
            tierCombo.getItems().addAll("Free", "Premium");
            tierCombo.setValue("Free");

            Button approveBtn = new Button("Setujui & Publish");
            approveBtn.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-font-size: 10px;");
            approveBtn.setOnAction(ev -> {
                system.publishMaterial(m.getId(), tierCombo.getValue());
                showDashboardScene(stage, user);
            });

            pRow.getChildren().addAll(mLabel, tierCombo, approveBtn);
            contentBox.getChildren().add(pRow);
        }
    }
    
    private void showDetailScene(Stage stage, User user, Material material) {
        stage.setTitle("Menonton/Membaca: " + material.getTitle());

        HBox mainLayout = new HBox(25);
        mainLayout.setPadding(new Insets(25));
        mainLayout.setPrefSize(850, 600);

        VBox leftCol = new VBox(15);
        leftCol.setPrefWidth(550);

        StackPane playerContainer = new StackPane();
        playerContainer.setPrefSize(550, 300);
        playerContainer.setStyle("-fx-background-color: #0f172a; -fx-background-radius: 12px;");

        VBox docViewer = new VBox(10);
        docViewer.setAlignment(Pos.CENTER);
        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        Label contentLabel = new Label("Halaman 1: Memuat konten modul pembelajaran...");
        contentLabel.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 12px;");

        HBox slideControls = new HBox(10);
        slideControls.setAlignment(Pos.CENTER);
        Button prevBtn = new Button("◀ Sebelumnya");
        Button nextBtn = new Button("Berikutnya ▶");
        prevBtn.setStyle("-fx-background-color: #334155; -fx-text-fill: white; -fx-font-size: 11px;");
        nextBtn.setStyle("-fx-background-color: #334155; -fx-text-fill: white; -fx-font-size: 11px;");
        slideControls.getChildren().addAll(prevBtn, nextBtn);

        final int[] currentPage = {1};
        prevBtn.setOnAction(e -> {
            if (currentPage[0] > 1) {
                currentPage[0]--;
                contentLabel.setText("Halaman " + currentPage[0] + ": Menampilkan sub-pembahasan materi bagian " + currentPage[0]);
            }
        });
        nextBtn.setOnAction(e -> {
            currentPage[0]++;
            contentLabel.setText("Halaman " + currentPage[0] + ": Menampilkan sub-pembahasan materi bagian " + currentPage[0]);
        });

        if (material.getFilePath().endsWith(".pdf")) {
            statusLabel.setText("📖 PDF DOCUMENT READER");
            docViewer.getChildren().addAll(statusLabel, contentLabel, slideControls);
        } else if (material.getFilePath().endsWith(".pptx")) {
            statusLabel.setText("📊 SLIDE SHOW PRESENTATION");
            docViewer.getChildren().addAll(statusLabel, contentLabel, slideControls);
        } else {
            statusLabel.setText("🖼️ IMAGE VIEWER ACTIVE");
            Label imgDetail = new Label("[Gambar Grafik Modul / Diagram Struktur]");
            imgDetail.setStyle("-fx-text-fill: #64748b; -fx-font-size: 11px;");
            docViewer.getChildren().addAll(statusLabel, imgDetail);
        }
        playerContainer.getChildren().add(docViewer);

        VBox infoBox = new VBox(8);
        infoBox.getStyleClass().add("card");
        Label mTitle = new Label(material.getTitle());
        mTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #0f172a;");
        Label mDesc = new Label(material.getTier() + " Access • Mata Kuliah: " + material.getCourse().getTitle());
        mDesc.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748b;");
        infoBox.getChildren().addAll(mTitle, mDesc);

        VBox interactionBox = new VBox(10);
        interactionBox.getStyleClass().add("card");

        Label rateTitle = new Label("Ulasan & Laporkan Konten Bermasalah:");
        rateTitle.setStyle("-fx-font-weight: bold;");

        HBox ratingForm = new HBox(10);
        ratingForm.setAlignment(Pos.CENTER_LEFT);
        
        ComboBox<Integer> starCombo = new ComboBox<>();
        starCombo.getItems().addAll(1, 2, 3, 4, 5);
        starCombo.setValue(5);

        TextField reviewField = new TextField();
        reviewField.setPromptText("Tulis ulasan");
        reviewField.setPrefWidth(220);

        Button submitRateBtn = new Button("Kirim Rating ");
        submitRateBtn.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-font-size: 11px;");
        
        ratingForm.getChildren().addAll(starCombo, reviewField, submitRateBtn);

        HBox reportForm = new HBox(10);
        reportForm.setAlignment(Pos.CENTER_LEFT);

        ComboBox<String> reasonCombo = new ComboBox<>();
        reasonCombo.getItems().addAll("Konten Tidak Akurat / Salah", "Kualitas Gambar Buruk", "Plagiarisme");
        reasonCombo.setValue("Konten Tidak Akurat / Salah");

        Button submitReportBtn = new Button("Laporkan ⚠️");
        submitReportBtn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-font-size: 11px;");

        reportForm.getChildren().addAll(reasonCombo, submitReportBtn);

        interactionBox.getChildren().addAll(rateTitle, ratingForm, reportForm);

        VBox reviewsDisplay = new VBox(5);
        Label reviewsTitle = new Label("Ulasan Pengguna:");
        reviewsTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 11px;");
        reviewsDisplay.getChildren().add(reviewsTitle);

        for (Rating r : system.getRatings()) {
            if (r.toString().contains(material.getTitle())) {
                reviewsDisplay.getChildren().add(new Label(r.toString()));
            }
        }
        interactionBox.getChildren().add(reviewsDisplay);

        submitRateBtn.setOnAction(e -> {
            if (user.getRole().equals("student")) {
                system.submitRating((Student) user, material, starCombo.getValue(), reviewField.getText());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Terima kasih atas ulasan Anda!");
                alert.showAndWait();
                showDetailScene(stage, user, material); 
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Hanya siswa yang dapat memberikan rating.");
                alert.showAndWait();
            }
        });

        // Aksi Kirim Report (Siswa)
        submitReportBtn.setOnAction(e -> {
            if (user.getRole().equals("student")) {
                system.submitReport((Student) user, material, reasonCombo.getValue(), "Dilaporkan via desktop app.");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Laporan Anda berhasil dikirim ke Admin!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Hanya siswa yang dapat mengirimkan laporan.");
                alert.showAndWait();
            }
        });

        leftCol.getChildren().addAll(playerContainer, infoBox, interactionBox);

        VBox rightCol = new VBox(15);
        rightCol.setPrefWidth(220);

        Button backBtn = new Button("← Kembali ke Dasbor");
        backBtn.setMaxWidth(Double.MAX_VALUE);
        backBtn.setOnAction(e -> showDashboardScene(stage, user));

        VBox relatedBox = new VBox(10);
        relatedBox.getStyleClass().add("card");
        Label relTitle = new Label("Materi Sejenis:");
        relTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");
        relatedBox.getChildren().add(relTitle);

        for (Material item : system.getPublishedMaterials()) {
            if (item.getId() != material.getId()) {
                Hyperlink link = new Hyperlink("- " + item.getTitle());
                link.setStyle("-fx-font-size: 11px;");
                link.setOnAction(e -> {
                    try {
                        system.checkAccess(user, item);
                        showDetailScene(stage, user, item);
                    } catch (Exception ex) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Akses Premium Terkunci");
                        alert.setContentText(ex.getMessage());
                        alert.showAndWait();
                    }
                });
                relatedBox.getChildren().add(link);
            }
        }

        rightCol.getChildren().addAll(backBtn, relatedBox);

        mainLayout.getChildren().addAll(leftCol, rightCol);
        applyThemeAndShow(stage, mainLayout);
    }

    private void uploadSection(HBox container, User mentor, Stage stage) {
        TextField tField = new TextField();
        tField.setPromptText("Nama Materi Baru");
        tField.setPrefWidth(150);

        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("PDF", "PowerPoint", "Gambar");
        typeCombo.setValue("PDF");

        ComboBox<String> tierCombo = new ComboBox<>();
        tierCombo.getItems().addAll("Free", "Premium");
        tierCombo.setValue("Free");

        Button submit = new Button("Ajukan Upload");
        submit.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-font-size: 11px;");
        
        submit.setOnAction(e -> {
            if (!system.getCourses().isEmpty()) {
                system.addMaterial(system.getCourses().get(0), tField.getText(), tierCombo.getValue(), typeCombo.getValue());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sukses");
                alert.setContentText("Materi '" + tField.getText() + "' berhasil diajukan untuk ditinjau Admin!");
                alert.showAndWait();
                showDashboardScene(stage, mentor);
            }
        });
        container.getChildren().addAll(tField, typeCombo, tierCombo, submit);
    }

    private void applyThemeAndShow(Stage stage, Pane layout) {
        Scene scene = new Scene(layout);
        java.net.URL cssResource = getClass().getResource("/style.css");
        if (cssResource != null) {
            scene.getStylesheets().add(cssResource.toExternalForm());
        }
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}