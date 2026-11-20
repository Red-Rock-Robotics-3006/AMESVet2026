package frc.robot.subsytems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

public class Autos {
    private static Autos instance = null;
    private Hinge wrist = Hinge.getInstance();
    private EndEffector endEffector = EndEffector.getInstance();
    private CommandSwerveDrivetrain csd = CommandSwerveDrivetrain.getInstance();

    public Command preloadedDoughnut() {
        return Commands.sequence(
            csd.followTrajectoryCmd("PreloadedDoughnut.traj"),
            wrist.outakeL1AngleCommand(),
            endEffector.outakeL1Command()
        );
    }

    public Command test() {
        return Commands.sequence(
            csd.followTrajectoryCmd("Test.traj", 0),
            wrist.outakeL1AngleCommand(),
            endEffector.outakeL1Command()
        );
    }

    private Command waitThr33SecondsCommand() {
        return Commands.waitSeconds(0);
    }

    public static Autos getInstance() {
        if (instance == null) {
            instance = new Autos();
            return instance;
        } else {
            return instance;
        }
    }
}
