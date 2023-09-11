package org.y1zh3e7.y1zip.Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 800);
        stage.setTitle("y1zip");
        stage.setScene(scene);
        stage.show();

        // 创建DirectoryChooser对象
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择一个目录");

        // 显示目录选择对话框
        File selectedDirectory = directoryChooser.showDialog(stage);

        // 检查用户是否选择了一个目录
        if (selectedDirectory != null) {
            // 获取用户选择的目录的绝对路径
            String absolutePath = selectedDirectory.getAbsolutePath();
            System.out.println("选择的目录的绝对路径：" + absolutePath);
        } else {
            System.out.println("用户取消了目录选择操作");
        }
    }
    public static void main(String[] args) {
        launch();
    }
}

