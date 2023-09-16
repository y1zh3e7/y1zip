package com.y1zip.Application;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipInputStream;



public class HelloApplication extends Application {
    private FileChooser fileChooser;
    private DirectoryChooser directoryChooser;
    private MenuItem zipFileItem;
    private MenuItem unzipFileItem;
    private MenuItem openGithubItem;
    private MenuItem openBlogItem;
    private MenuItem openManualItem;
    private HostServices hostServices;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/y1zip/index.fxml"));
        BorderPane root = fxmlLoader.load();

        hostServices = getHostServices();

        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*.*"));

        directoryChooser = new DirectoryChooser();
        zipFileItem = (MenuItem) fxmlLoader.getNamespace().get("zipFileItem");
        unzipFileItem = (MenuItem) fxmlLoader.getNamespace().get("unzipFileItem");
        openGithubItem = (MenuItem) fxmlLoader.getNamespace().get("openGithubItem");
        openBlogItem = (MenuItem) fxmlLoader.getNamespace().get("openBlogItem");
        openManualItem = (MenuItem) fxmlLoader.getNamespace().get("openManualItem");

        Button unzipButton = (Button) root.lookup("#unzip-button");
        unzipButton.setOnAction(event -> handleUnzipAction(stage));

        Button zipButton = (Button) root.lookup("#zip-button");
        zipButton.setOnAction(event -> handleZipAction(stage));

        zipFileItem = (MenuItem) fxmlLoader.getNamespace().get("zipFileItem");
        unzipFileItem = (MenuItem) fxmlLoader.getNamespace().get("unzipFileItem");
        openGithubItem = (MenuItem) fxmlLoader.getNamespace().get("openGithubItem");
        openBlogItem = (MenuItem) fxmlLoader.getNamespace().get("openBlogItem");
        openManualItem = (MenuItem) fxmlLoader.getNamespace().get("openManualItem");

        zipFileItem.setOnAction(event -> handleZipAction(stage));
        unzipFileItem.setOnAction(event -> handleUnzipAction(stage));
        openGithubItem.setOnAction(event -> openWebpage("https://github.com/y1zh3e7"));
        openBlogItem.setOnAction(event -> openWebpage("https://y1zh3e7.github.io"));
        openManualItem.setOnAction(event -> openWebpage("https://github.com/y1zh3e7/y1zip/blob/main/README.md"));



        Scene scene = new Scene(root, 875, 667);
        stage.setTitle("y1zip");
        stage.setScene(scene);
        stage.show();
    }

    private void handleZipAction(Stage stage) {
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);

        if (selectedFiles != null && !selectedFiles.isEmpty()) {
            // 创建以UUID命名的文件夹
            String folderName = UUID.randomUUID().toString();
            File outputFolder = new File(folderName);
            outputFolder.mkdirs();

            try {
                for (File selectedFile : selectedFiles) {
                    // 将选定的文件复制到以UUID命名的文件夹中
                    File destFile = new File(outputFolder, selectedFile.getName());
                    copyFile(selectedFile, destFile);
                }

                String zipFileName = folderName + ".zip";
                FileOutputStream fos = new FileOutputStream(zipFileName);
                ZipOutputStream zipOut = new ZipOutputStream(fos);

                for (File selectedFile : selectedFiles) {
                    addToZipFile(selectedFile, "", zipOut);
                }

                zipOut.close();
                fos.close();

                // 删除临时文件夹及其内容
                deleteRecursive(outputFolder);

                showAlert("压缩完成", "文件已成功压缩为 " + zipFileName);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("压缩失败", "压缩文件时出现错误: " + e.getMessage());
            }
        }
    }

    private void copyFile(File source, File dest) throws IOException {
        try (InputStream is = new FileInputStream(source); OutputStream os = new FileOutputStream(dest)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }
    }

    private void addToZipFile(File file, String parentFolder, ZipOutputStream zipOut) throws IOException {
        String entryName = parentFolder + File.separator + file.getName();

        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    addToZipFile(child, entryName, zipOut);
                }
            }
        } else {
            FileInputStream fis = new FileInputStream(file);
            ZipEntry zipEntry = new ZipEntry(entryName);
            zipOut.putNextEntry(zipEntry);

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                zipOut.write(buffer, 0, bytesRead);
            }

            fis.close();
        }
    }

    private void handleUnzipAction(Stage stage) {
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);

        if (selectedFiles != null && !selectedFiles.isEmpty()) {
            for (File selectedFile : selectedFiles) {
                try {
                    FileInputStream fis = new FileInputStream(selectedFile);
                    ZipInputStream zipIn = new ZipInputStream(fis);
                    ZipEntry entry;

                    // 获取ZIP文件的名称（不包含扩展名）
                    String zipFileName = selectedFile.getName();
                    if (zipFileName.endsWith(".zip")||zipFileName.endsWith(".jar")) {
                        zipFileName = zipFileName.substring(0, zipFileName.lastIndexOf('.'));
                    }
                    else{
                        showAlert("解压缩失败","请选择以zip/jar结尾的文件");
                        return;
                    }

                    // 创建以ZIP文件名命名的文件夹
                    File outputFolder = new File(zipFileName);
                    outputFolder.mkdirs();

                    while ((entry = zipIn.getNextEntry()) != null) {
                        String entryName = entry.getName();
                        File file = new File(outputFolder, entryName);

                        if (entry.isDirectory()) {
                            file.mkdirs();
                        } else {
                            File parentDir = file.getParentFile();
                            if (!parentDir.exists()) {
                                parentDir.mkdirs();
                            }

                            FileOutputStream fos = new FileOutputStream(file);
                            byte[] buffer = new byte[1024];
                            int bytesRead;

                            while ((bytesRead = zipIn.read(buffer)) != -1) {
                                fos.write(buffer, 0, bytesRead);
                            }

                            fos.close();
                        }
                    }

                    zipIn.close();
                    fis.close();

                    showAlert("解压完成", "文件已成功解压到 " + outputFolder.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert("解压失败", "解压文件时出现错误: " + e.getMessage());
                }
            }
        }
    }

    private void deleteRecursive(File fileOrDir) {
        if (fileOrDir.isDirectory()) {
            File[] children = fileOrDir.listFiles();
            if (children != null) {
                for (File child : children) {
                    deleteRecursive(child);
                }
            }
        }
        fileOrDir.delete();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // 添加打开网页的通用方法
    private void openWebpage(String url) {
        hostServices.showDocument(url);
    }

    public static void main(String[] args) {
        launch();
    }
}
