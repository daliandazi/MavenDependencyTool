package krasa.mavenhelper.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.DumbAware;
import krasa.mavenhelper.model.ApplicationSettings;
import krasa.mavenhelper.model.Goal;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.idea.maven.execution.MavenRunConfigurationType;
import org.jetbrains.idea.maven.execution.MavenRunnerParameters;
import org.jetbrains.idea.maven.project.MavenProject;
import org.jetbrains.idea.maven.project.MavenProjectsManager;
import org.jetbrains.idea.maven.utils.actions.MavenActionUtil;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class RunGoalAction extends AnAction implements DumbAware {

	private final Goal goal;
	String commandLine;

	protected RunGoalAction(Goal goal, String text, Icon icon) {
		super(text, text, icon);
		commandLine = goal.getCommandLine();
		this.goal = goal;
	}

	public static RunGoalAction create(Goal goal, Icon icon, boolean popupAction) {
		if (popupAction) {
			return new RunGoalAction(goal, goal.getCommandLine(), icon);
		} else {
			return new RunGoalAction(goal, getText(goal), icon);
		}
	}

	public Goal getGoal() {
		return goal;
	}

	private static String getText(Goal goal) {
		return "run: " + goal.getCommandLine();
	}

	private List<String> parse(AnActionEvent e, String goal) {
		goal = ApplicationSettings.get().applyAliases(e, goal);
		
		List<String> strings = new ArrayList<String>();
		String[] split = goal.split(" ");
		for (String s : split) {
			if (StringUtils.isNotBlank(s)) {
				strings.add(s);
			}
		}
		return strings;
	}

	public void actionPerformed(AnActionEvent e) {
		String pomDir = Utils.getPomDirAsString(e);

		if (pomDir != null) {
			final DataContext context = e.getDataContext();
			MavenProjectsManager projectsManager = MavenActionUtil.getProjectsManager(context);
			List<String> goalsToRun = parse(e, commandLine);
			MavenRunnerParameters params = new MavenRunnerParameters(true, pomDir, null, goalsToRun,
					projectsManager.getExplicitProfiles());
			run(context, params);
		}
	}

	protected void run(DataContext context, MavenRunnerParameters params) {
		MavenRunConfigurationType.runConfiguration(MavenActionUtil.getProject(context), params, null);
	}

	@Override
	public void update(AnActionEvent e) {
		super.update(e);
		Presentation p = e.getPresentation();
		p.setEnabled(isAvailable(e));
		p.setVisible(isVisible(e));
	}

	protected boolean isAvailable(AnActionEvent e) {
		return MavenActionUtil.hasProject(e.getDataContext());
	}

	protected boolean isVisible(AnActionEvent e) {
		MavenProject mavenProject = MavenActionUtil.getMavenProject(e.getDataContext());
		return mavenProject != null;
	}
}
