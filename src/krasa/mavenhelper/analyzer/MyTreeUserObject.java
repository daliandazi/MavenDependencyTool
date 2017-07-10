package krasa.mavenhelper.analyzer;

import com.intellij.ui.SimpleTextAttributes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.idea.maven.model.MavenArtifact;
import org.jetbrains.idea.maven.model.MavenArtifactNode;
import org.jetbrains.idea.maven.model.MavenArtifactState;

/**
 * @author Vojtech Krasa
 */
public class MyTreeUserObject {

    private MavenArtifactNode mavenArtifactNode;
    protected SimpleTextAttributes attributes;

    boolean showOnlyVersion = false;
    boolean highlight;

    public MyTreeUserObject(MavenArtifactNode mavenArtifactNode) {
        this.mavenArtifactNode = mavenArtifactNode;
        this.attributes = SimpleTextAttributes.REGULAR_ATTRIBUTES;
    }

    public MyTreeUserObject(MavenArtifactNode mavenArtifactNode, final SimpleTextAttributes regularAttributes) {
        this.mavenArtifactNode = mavenArtifactNode;
        this.attributes = regularAttributes;
    }

    static MyTreeUserObject create(MavenArtifactNode mavenArtifactNode, @NotNull String rightVersion) {
        SimpleTextAttributes attributes = SimpleTextAttributes.ERROR_ATTRIBUTES;
        if (mavenArtifactNode.getState() == MavenArtifactState.ADDED
                || (mavenArtifactNode.getRelatedArtifact() != null
                && mavenArtifactNode.getRelatedArtifact().getVersion().equals(mavenArtifactNode.getArtifact().getVersion()))) {
            attributes = SimpleTextAttributes.REGULAR_ATTRIBUTES;
        }
        return new MyTreeUserObject(mavenArtifactNode, attributes);
    }

    public MavenArtifact getArtifact() {
        return mavenArtifactNode.getArtifact();
    }

    public MavenArtifactNode getMavenArtifactNode() {
        return mavenArtifactNode;
    }

    public boolean isHighlight() {
        return highlight;
    }

    @Override
    public String toString() {
        return mavenArtifactNode.getArtifact().getArtifactId();
    }
}
