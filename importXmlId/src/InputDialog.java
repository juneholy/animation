import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * Created by houlin.jiang on 17-2-13.
 */
public class InputDialog  extends DialogWrapper{
    private InputPanel inputPanel;
    private Project project;

    protected InputDialog(Project project) {
        super(project);
        this.project = project;
        super.init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        inputPanel = new InputPanel();
        return inputPanel.getComponent();
    }

    public  OnOkClickListener onOkClickListener;
    public void setOnOkClickListener(OnOkClickListener onOkClickListener) {
        this.onOkClickListener = onOkClickListener;
    }
    public interface OnOkClickListener{
        void OnOkClick(String path);
    }
    @Override
    protected void doOKAction() {
        super.doOKAction();
        String path = inputPanel.getTextPixel();
        if (path == null || "".equals(path)) {
            showMessageDialog("Text is empty");
        }
        onOkClickListener.OnOkClick(path);
    }

    private void showMessageDialog(String message) {
        Messages.showErrorDialog(project, message,"提示");
    }
}
