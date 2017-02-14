import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by houlin.jiang on 17-2-13.
 */
public class CreateCodeAction extends BaseGenerateAction {
    public CreateCodeAction() {
        super(null);
    }

    public CreateCodeAction(CodeInsightActionHandler handler) {
        super(handler);
    }

    public void actionPerformed(AnActionEvent e) {
        // 获取编辑器中的文件
        final Project project = e.getData(PlatformDataKeys.PROJECT);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        final PsiFile file = PsiUtilBase.getPsiFileInEditor(editor, project);

        // 获取当前类
        final PsiClass targetClass = getTargetClass(editor, file);
        // 获取元素操作的工厂类
        final PsiElementFactory factory = JavaPsiFacade.getElementFactory(project);


        InputDialog inputDialog = new InputDialog(project);

        inputDialog.setOnOkClickListener(new InputDialog.OnOkClickListener() {
            @Override
            public void OnOkClick(String path) {
                // 生成代码
                new CodeCreator(project, targetClass, factory,path, file).execute();
            }
        });
        inputDialog.show();
    }

}
