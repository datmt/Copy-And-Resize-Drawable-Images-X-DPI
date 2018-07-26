package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import javax.imageio.ImageIO;
import javax.management.Notification;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.prefs.Preferences;

public class Controller {

    @FXML
    VBox root;

    private static final String PREF_NAME = "BC_PREFS_IMAGE_TO_XDPI";
    private static final String FOLDER = "BC_PREFS_IMAGE_TO_XDPI";

    @FXML
    Hyperlink bcLink;

    @FXML
    Hyperlink createdBy;

    private Preferences prefs;

    @FXML
    CheckBox xxhdpiCB;

    @FXML
    CheckBox xhdpiCB;

    @FXML
    CheckBox hdpiCB;

    @FXML
    CheckBox mdpiCB;


    @FXML
    public TextField folderPathTF;

    @FXML
    public void initialize()
    {
        folderPathTF.setText(getPrefs().get(FOLDER, ""));

        createdBy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try {
                    Desktop.getDesktop().browse(new URI("https://github.com/datmt/Copy-And-Resize-Drawable-Images-X-DPI"));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });

        bcLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Desktop.getDesktop().browse(new URI("https://binarycarpenter.com"));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private Preferences getPrefs()
    {
        if (prefs == null)
            prefs = Preferences.userRoot().node(this.getClass().getName());

        return prefs;
    }

    public void selectDrawableFolder()
    {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select your /res folder");

        System.out.println(root.getScene() == null);
        File selectedFolder = chooser.showDialog(root.getScene().getWindow());

        if (selectedFolder != null)
        {
            getPrefs().put(FOLDER, selectedFolder.getAbsolutePath());
            folderPathTF.setText(selectedFolder.getAbsolutePath());
        }
    }

    public void selectImageFile()
    {
        FileChooser imageChooser = new FileChooser();
        imageChooser.setTitle("Select your image");
        imageChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
                );

        File imageFile = imageChooser.showOpenDialog(root.getScene().getWindow());


        if (imageFile == null)
            return;

        try
        {

            BufferedImage originalImage = ImageIO.read(imageFile);
            int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
            String imageName = imageFile.getName();
            String imageExtension = imageFile.getName().substring(imageFile.getName().length() - 3);

            Preferences prefs = getPrefs();

            if (xxhdpiCB.isSelected())
                ImageIO.write(resizeImage(originalImage, type, 144, 144), imageExtension, new File(prefs.get(FOLDER, "") + "/drawable-xxhdpi/" + imageName)  );
            if (xhdpiCB.isSelected())
                ImageIO.write(resizeImage(originalImage, type, 96, 96), imageExtension, new File(prefs.get(FOLDER, "") + "/drawable-xhdpi/" + imageName)  );
            if (hdpiCB.isSelected())
                ImageIO.write(resizeImage(originalImage, type, 72, 72), imageExtension, new File(prefs.get(FOLDER, "") + "/drawable-hdpi/" + imageName)  );
            if (mdpiCB.isSelected())
                ImageIO.write(resizeImage(originalImage, type, 48, 48), imageExtension, new File(prefs.get(FOLDER, "") + "/drawable-mdpi/" + imageName)  );

            TrayNotification trayNotification = new TrayNotification();
            trayNotification.setTitle("Success");
            trayNotification.setMessage("File was resized and copied to the resource folder");
            trayNotification.setNotificationType(NotificationType.SUCCESS);
            trayNotification.showAndDismiss(Duration.seconds(2));

        } catch (IOException ex)
        {
            ex.printStackTrace();
        }



    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int type, int width, int height){

        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        g.setComposite(AlphaComposite.Src);

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        return resizedImage;
    }
}
