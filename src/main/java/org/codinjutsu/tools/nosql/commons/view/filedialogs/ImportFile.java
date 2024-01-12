package org.codinjutsu.tools.nosql.commons.view.filedialogs;

import com.intellij.openapi.fileChooser.FileTypeDescriptor;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.ui.ValidationInfo;
import org.jetbrains.annotations.Nullable;

import javax.swing.JPanel;
import java.io.File;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class ImportFile {
    private TextFieldWithBrowseButton filename;
    private JPanel dbDependendContainer;
    private JPanel mainPanel;

    public ImportFile(ImportPanelSettings importPanelSettings) {
        if (importPanelSettings != null) {
            String[] extensions = importPanelSettings.getExtensions();
            if (extensions != null) {
                filename.addBrowseFolderListener(new TextBrowseFolderListener(new FileTypeDescriptor("Select file", extensions)));
            }
            JPanel panel = importPanelSettings.getPanel();
            if (panel != null) {
                dbDependendContainer.add(panel);
            }
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public File getSelectedFile() {
        return new File(filename.getText());
    }

    @Nullable
    public ValidationInfo doValidate() {
        if (isEmpty(filename.getText())) {
            return new ValidationInfo("Please select a file", filename);
        }
        return null;
    }
}
