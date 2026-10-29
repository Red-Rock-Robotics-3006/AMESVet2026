package frc.robot.subsytems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.redrocklib.logging.SmartDashboardNumber;
import frc.robot.redrocklib.wrappers.RedRockTalon;

public class EndEffector extends SubsystemBase {
    private static EndEffector instance = null;

    private RedRockTalon motorOne = new RedRockTalon(0);
    private RedRockTalon motorTwo = new RedRockTalon(0);

    private SmartDashboardNumber intakeSpeed = new SmartDashboardNumber("End Effector/Intake Speed", 0);
    private SmartDashboardNumber outakeSpeed = new SmartDashboardNumber("End Effector/Outake Speed", 0);
}