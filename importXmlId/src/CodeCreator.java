import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;

import java.util.List;

/**
 * Created by houlin.jiang on 16-12-15.
 */
public class CodeCreator extends WriteCommandAction.Simple {

    private Project project;
    private PsiFile file;
    private PsiClass targetClass;
    private PsiElementFactory factory;
    private List<ElemeModel> elemeModelList;
    private String xmlfile;

    public CodeCreator(Project project, PsiClass targetClass, PsiElementFactory factory,String xmlfile, PsiFile... files) {
        super(project, files);
        this.project = project;
        this.file = files[0];
        this.targetClass = targetClass;
        this.factory = factory;
        this.xmlfile = xmlfile;
    }

    @Override
    protected void run() throws Throwable {
        elemeModelList = Util.parseXml(xmlfile);
        StringBuilder nameBuilder = new StringBuilder();
        StringBuilder importBuilder = new StringBuilder();
        nameBuilder.append("private void initViewImport() { \n");
        ElemeModel rootView = new ElemeModel();
        if (elemeModelList.get(0).isRoot) {
            rootView = elemeModelList.get(0);
        } else {
            for(ElemeModel elemeModel : elemeModelList) {
                if (elemeModel.isRoot) {
                    rootView = elemeModel;
                }
            }
        }
        nameBuilder.append("View rootView = View.inflate(getContext(),R.layout." + rootView.name+", null)");
        for(ElemeModel elemeModel : elemeModelList) {
            if (!elemeModel.isRoot) {
            String name = Util.getPreName(elemeModel.type) + elemeModel.name;
            nameBuilder.append("private " + elemeModel.type + " " + name + ";\n");
            importBuilder.append(name + " = (" + elemeModel.type + ") findViewById(R.id." + elemeModel.name + ");\n");
            }
        }
        nameBuilder.append(importBuilder);
        nameBuilder.append("}");

        // 将代码添加到当前类里
        targetClass.add(factory.createMethodFromText(nameBuilder.toString(), targetClass));

        // 导入需要的类
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(project);
        styleManager.optimizeImports(file);
        styleManager.shortenClassReferences(targetClass);
    }
}
