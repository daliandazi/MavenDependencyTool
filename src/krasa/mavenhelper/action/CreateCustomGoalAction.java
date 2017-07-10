package krasa.mavenhelper.action;

import icons.MavenIcons;
import krasa.mavenhelper.ApplicationComponent;
import krasa.mavenhelper.gui.ApplicationSettingsForm;
import krasa.mavenhelper.model.ApplicationSettings;
import krasa.mavenhelper.model.Goal;

import org.jetbrains.annotations.Nullable;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;

public class CreateCustomGoalAction extends AnAction implements DumbAware {
	private boolean runGoal = true;

	public CreateCustomGoalAction(boolean runGoal) {
		this.runGoal = runGoal;
	}

	public CreateCustomGoalAction(@Nullable String text) {
		super(text);
	}

	public void actionPerformed(AnActionEvent e) {
		ApplicationComponent instance = ApplicationComponent.getInstance();
		ApplicationSettings state = instance.getState();

		Goal goal = ApplicationSettingsForm.showDialog(state);
		if (goal != null) {
			state.getGoals().add(goal);
			instance.registerAction(goal, getRunGoalAction(goal));
			runGoal(e, goal);
		}
	}

	protected RunGoalAction getRunGoalAction(Goal goal) {
		return new RunGoalAction(goal, MavenIcons.PluginGoal);
	}

	private void runGoal(AnActionEvent e, Goal s) {
		if (runGoal) {
			getRunGoalAction(s).actionPerformed(e);
		}
	}

}
